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

        // 1. Obtener Habitaciones ordenadas
        List<Habitacion> habitaciones = habitacionRepository.findAll(Sort.by(Sort.Direction.ASC, "numero"));

        // 2. Obtener Estadias en el rango (asegúrate que tu DAO tenga este método)
        List<Estadia> estadias = estadiaRepository.buscarPorRango(desde, hasta);

        LocalDate diaActual = desde;

        // 3. Recorrer Días
        while (!diaActual.isAfter(hasta)) {
            List<CeldaEstadoDTO> celdasFila = new ArrayList<>();

            // 4. Recorrer Habitaciones
            for (Habitacion hab : habitaciones) {
                String estadoCodigo = "LIBRE";
                String texto = "Disponible";

                for (Estadia e : estadias) {
                    // Verificar si la habitación coincide y el día cae dentro de la estadía
                    // IMPORTANTE: checkin <= dia < checkout (usualmente checkout es a las 10am, asi que el día de salida queda libre para entrar)
                    if (e.getHabitacion().getId().equals(hab.getId()) &&
                            !diaActual.isBefore(e.getFechaCheckin()) &&
                            diaActual.isBefore(e.getFechaCheckout())) {

                            estadoCodigo = "OCUPADA";   // Se pintará Rosa
                            texto = "Ocupada";

                        break; // Ya encontramos ocupación, no seguir buscando
                    }
                }

                celdasFila.add(new CeldaEstadoDTO(
                        hab.getId().toString(),
                        hab.getNumero(),
                        estadoCodigo,
                        texto,
                        hab.getNombreTipo() // <--- Asegúrate que tu entidad Habitacion tenga acceso al nombre del tipo
                ));
            }
            grilla.add(new FilaGrillaDTO(diaActual.format(formatter), celdasFila));
            diaActual = diaActual.plusDays(1);
        }
        return grilla;
    }
}