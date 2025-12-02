package Logica.Servicio;

import DTO.CeldaEstadoDTO;
import DTO.FilaGrillaDTO;
import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Entidades.Habitacion;
import Logica.Dominio.Enum.TipoEstadoEstadia;
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

    @Autowired private HabitacionDAO habitacionRepository;
    @Autowired private EstadiaDAO estadiaRepository;

    public List<FilaGrillaDTO> consultarEstadoHabitaciones(LocalDate desde, LocalDate hasta) {
        List<FilaGrillaDTO> grilla = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // 1. Obtener Habitaciones ordenadas por número
        List<Habitacion> habitaciones = habitacionRepository.findAll(Sort.by(Sort.Direction.ASC, "numero"));

        // 2. Obtener Estadias en el rango
        List<Estadia> estadias = estadiaRepository.buscarPorRango(desde, hasta);

        LocalDate diaActual = desde;

        // 3. Recorrer Días
        while (!diaActual.isAfter(hasta)) {
            List<CeldaEstadoDTO> celdasFila = new ArrayList<>();

            // 4. Recorrer Habitaciones para este día
            for (Habitacion hab : habitaciones) {
                String estadoCodigo = "LIBRE";
                String texto = "Disponible";

                // Buscamos si hay alguna estadía que ocupe esta habitación en este día
                for (Estadia e : estadias) {
                    // Condición de solapamiento: Habitación coincide Y día está en rango [CheckIn, CheckOut)
                    if (e.getHabitacion().getId().equals(hab.getId()) &&
                            !diaActual.isBefore(e.getFechaCheckin()) && // fecha >= inicio
                            diaActual.isBefore(e.getFechaCheckout())) {     // fecha < fin

                        // --- LÓGICA DE ESTADOS CORREGIDA ---
                        if (e.getTipoEstado() == TipoEstadoEstadia.ACTIVA) {
                            estadoCodigo = "OCUPADA";
                            texto = "OCUPADA"; // Requerimiento explícito: texto "OCUPADA"
                        } else if (e.getTipoEstado() == TipoEstadoEstadia.RESERVADA) {
                            estadoCodigo = "RESERVADA";
                            // Intentamos mostrar el apellido del responsable si existe
                            if (e.getHuespedes() != null && !e.getHuespedes().isEmpty()) {
                                texto = e.getHuespedes().get(0).getApellido();
                            } else {
                                texto = "Reservado";
                            }
                        }

                        // Si encontramos una ocupación, dejamos de buscar otras estadías para esta celda
                        break;
                    }
                }

                celdasFila.add(new CeldaEstadoDTO(
                        hab.getId().toString(),
                        hab.getNumero(),
                        estadoCodigo,
                        texto,
                        hab.getNombreTipo()
                ));
            }
            grilla.add(new FilaGrillaDTO(diaActual.format(formatter), celdasFila));
            diaActual = diaActual.plusDays(1);
        }
        return grilla;
    }
}