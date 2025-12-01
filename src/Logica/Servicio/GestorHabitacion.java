package Logica.Servicio;

import DTO.CeldaEstadoDTO;
import DTO.FilaGrillaDTO;
import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Entidades.Habitacion;
import Logica.Dominio.Enum.EstadoHabitacion;
import Persistencia.Repositorios.EstadiaDAO;
import Persistencia.Repositorios.HabitacionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GestorHabitacion {

    @Autowired
    private HabitacionDAO habitacionRepository;
    @Autowired
    private EstadiaDAO estadiaRepository;

    public List<FilaGrillaDTO> consultarEstadoHabitaciones(LocalDate desde, LocalDate hasta) {
        List<FilaGrillaDTO> grilla = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // 1. Traer habitaciones ordenadas (01, 02, 03...)
        List<Habitacion> habitaciones = habitacionRepository.findAll(Sort.by(Sort.Direction.ASC, "numero"));

        // esto nos lo olvidamos en el diagrama de secuencia
        List<Estadia> estadias = estadiaRepository.buscarPorRango(desde, hasta);

        // 3. Barrido por Fechas
        LocalDate diaActual = desde;
        while (!diaActual.isAfter(hasta)) {
            List<CeldaEstadoDTO> celdasFila = new ArrayList<>();

            // 4. Barrido por Habitaciones
            for (Habitacion hab : habitaciones) {
                String estadoCodigo = "LIBRE";
                String texto = "LIBRE";

                    for (Estadia e : estadias) {
                        // Coincide habitación y la fecha cae dentro de la estadía
                        if (e.getHabitacion().getId().equals(hab.getId()) &&
                                !diaActual.isBefore(e.getFechaCheckin()) &&
                                diaActual.isBefore(e.getFechaCheckout())) {

                            estadoCodigo = "OCUPADA";
                            texto = "OCUPADA";
                            break;
                        }
                    }


                // Agregamos la celda a la fila
                celdasFila.add(new CeldaEstadoDTO(
                        hab.getId().toString(),
                        hab.getNumero(),
                        estadoCodigo,
                        texto
                ));
            }

            grilla.add(new FilaGrillaDTO(diaActual.format(formatter), celdasFila));
            diaActual = diaActual.plusDays(1);
        }

        return grilla;
    }
}