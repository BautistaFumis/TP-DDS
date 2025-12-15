package Logica.Servicio;

import DTO.DatosFacturacionDTO;
import DTO.GenerarFacturaDTO;
import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Entidades.Habitacion;
import Logica.Dominio.Enum.TipoEstadoEstadia;
import Persistencia.Repositorios.EstadiaDAO;
import Persistencia.Repositorios.HuespedDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestorFacturaTest {

    @Mock
    private EstadiaDAO estadiaDAO;

    @Mock
    private HuespedDAO huespedDAO;

    @InjectMocks
    private GestorFactura gestorFactura;

    // ✅ TEST 1: Obtener datos de facturación correctamente
    @Test
    void obtenerDatosFacturacion_estadiaActiva_devuelveDTO() {
        // Arrange
        Habitacion habitacion = mock(Habitacion.class);
        when(habitacion.getCostoNoche()).thenReturn(1000f);


        Estadia estadia = new Estadia();
        estadia.setId(1L);
        estadia.setHabitacion(habitacion);
        estadia.setFechaCheckin(LocalDate.now().minusDays(2));
        estadia.setFechaCheckout(LocalDate.now());

        when(estadiaDAO.findEstadiaActivaPorHabitacion("101"))
                .thenReturn(Optional.of(estadia));

        // Act
        DatosFacturacionDTO dto = gestorFactura.obtenerDatosFacturacion("101");

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getIdEstadia());
        assertFalse(dto.getItems().isEmpty());
        verify(estadiaDAO, times(1))
                .findEstadiaActivaPorHabitacion("101");
    }

    // ❌ TEST 2: No hay estadía activa → excepción
    @Test
    void obtenerDatosFacturacion_sinEstadia_lanzaExcepcion() {
        // Arrange
        when(estadiaDAO.findEstadiaActivaPorHabitacion("101"))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> gestorFactura.obtenerDatosFacturacion("101")
        );

        assertTrue(ex.getMessage().contains("No hay una estadía activa"));
    }

    // ✅ TEST 3: Generar factura cierra la estadía
    @Test
    void generarFactura_cierraEstadia() {
        // Arrange
        Estadia estadia = new Estadia();
        estadia.setId(1L);
        estadia.setTipoEstado(TipoEstadoEstadia.ACTIVA);

        GenerarFacturaDTO dto = new GenerarFacturaDTO();
        dto.setIdEstadia(1L);

        when(estadiaDAO.findById(1L))
                .thenReturn(Optional.of(estadia));

        // Act
        gestorFactura.generarFactura(dto);

        // Assert
        assertEquals(TipoEstadoEstadia.CERRADA, estadia.getTipoEstado());
        verify(estadiaDAO, times(1)).save(estadia);
    }

    // ❌ TEST 4: Estadía inexistente al generar factura
    @Test
    void generarFactura_estadiaInexistente_lanzaExcepcion() {
        // Arrange
        GenerarFacturaDTO dto = new GenerarFacturaDTO();
        dto.setIdEstadia(99L);

        when(estadiaDAO.findById(99L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                RuntimeException.class,
                () -> gestorFactura.generarFactura(dto)
        );
    }
}
