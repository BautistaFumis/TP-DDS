package Tests;

import DTO.CrearOcupacionDTO;
import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Entidades.Habitacion;
import Logica.Dominio.Entidades.Huesped;
import Logica.Dominio.Entidades.IndividualEstandar;
import Logica.Dominio.Enum.EstadoHabitacion;
import Logica.Dominio.Enum.TipoEstadoEstadia;
import Logica.Servicio.GestorEstadia;
import Repositorios.EstadiaDAO;
import Repositorios.HabitacionDAO;
import Repositorios.HuespedDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestorEstadiaTest {

    @Mock
    private HabitacionDAO habitacionRepository;

    @Mock
    private EstadiaDAO estadiaRepository;

    @Mock
    private HuespedDAO huespedRepository;

    @InjectMocks
    private GestorEstadia gestorEstadia;

    private CrearOcupacionDTO dto;
    private Habitacion habitacion;
    private Huesped huesped;

    @BeforeEach
    void setUp() {
        dto = new CrearOcupacionDTO();
        dto.setIdHabitacion(1L);
        dto.setFechaInicio(LocalDate.now());
        dto.setFechaFin(LocalDate.now().plusDays(2));
        dto.setIdsHuespedes(List.of(1L));
        dto.setEsOverrideReserva(false);

        habitacion = new IndividualEstandar(
                "101",
                EstadoHabitacion.LIBRE,
                15000f,
                1
        );
        habitacion.setId(1L);


        huesped = new Huesped();
        huesped.setId(1L);
    }

    // CASO 1: Ocupación correcta

    @Test
    void registrarOcupacion_ok() {
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(habitacion));
        when(huespedRepository.findAllById(List.of(1L))).thenReturn(List.of(huesped));
        when(estadiaRepository.buscarPorRango(any(), any())).thenReturn(List.of());

        gestorEstadia.registrarOcupacion(dto);

        verify(estadiaRepository, times(1)).save(any(Estadia.class));
    }

    // CASO 2: Habitación inexistente

    @Test
    void registrarOcupacion_habitacionNoExiste() {
        when(habitacionRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gestorEstadia.registrarOcupacion(dto));

        assertEquals("Habitación no encontrada", ex.getMessage());
    }

    // CASO 3: No hay huéspedes
    @Test
    void registrarOcupacion_sinHuespedes() {
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(habitacion));
        when(huespedRepository.findAllById(List.of(1L))).thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gestorEstadia.registrarOcupacion(dto));

        assertEquals(
                "Debe existir al menos un huésped para registrar la ocupación.",
                ex.getMessage()
        );
    }

    // CASO 4: Habitación ya ACTIVA (ocupada)

    @Test
    void registrarOcupacion_habitacionOcupadaActiva() {
        Estadia estadiaActiva = new Estadia();
        estadiaActiva.setHabitacion(habitacion);
        estadiaActiva.setTipoEstado(TipoEstadoEstadia.ACTIVA);

        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(habitacion));
        when(huespedRepository.findAllById(List.of(1L))).thenReturn(List.of(huesped));
        when(estadiaRepository.buscarPorRango(any(), any())).thenReturn(List.of(estadiaActiva));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gestorEstadia.registrarOcupacion(dto));

        assertTrue(ex.getMessage().contains("OCUPADA"));
    }

    // CASO 5: Reserva existente con override

    @Test
    void registrarOcupacion_reservaConOverride() {
        dto.setEsOverrideReserva(true);

        Estadia estadiaReservada = new Estadia();
        estadiaReservada.setHabitacion(habitacion);
        estadiaReservada.setTipoEstado(TipoEstadoEstadia.RESERVADA);

        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(habitacion));
        when(huespedRepository.findAllById(List.of(1L))).thenReturn(List.of(huesped));
        when(estadiaRepository.buscarPorRango(any(), any()))
                .thenReturn(List.of(estadiaReservada));

        gestorEstadia.registrarOcupacion(dto);

        assertEquals(TipoEstadoEstadia.ACTIVA, estadiaReservada.getTipoEstado());
        verify(estadiaRepository, times(1)).save(estadiaReservada);
    }
}
