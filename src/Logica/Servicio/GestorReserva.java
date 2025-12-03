package Logica.Servicio;

import DTO.CrearReservaDTO;
import DTO.ReservaItemDTO;
import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Entidades.Habitacion;
import Logica.Dominio.Entidades.Reserva;
import Logica.Dominio.Enum.EstadoReserva;
import Logica.Dominio.Enum.TipoEstadoEstadia;
import Persistencia.Repositorios.EstadiaDAO;
import Persistencia.Repositorios.HabitacionDAO;
import Persistencia.Repositorios.ReservaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class GestorReserva {

    @Autowired private ReservaDAO reservaRepository;
    @Autowired private HabitacionDAO habitacionRepository;
    @Autowired private EstadiaDAO estadiaRepository; // <--- IMPRESCINDIBLE

    @Transactional
    public void procesarReservas(CrearReservaDTO dto) {
        String codigoGrupo = "GRP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        for (ReservaItemDTO item : dto.getItems()) {
            Habitacion habitacion = habitacionRepository.findById(item.getIdHabitacion())
                    .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

            List<Estadia> ocupaciones = estadiaRepository.buscarPorRango(item.getFechaInicio(), item.getFechaFin());
            boolean ocupada = ocupaciones.stream().anyMatch(e -> e.getHabitacion().getId().equals(habitacion.getId()));

            if (ocupada) {
                throw new RuntimeException("La habitación " + habitacion.getNumero() + " ya no está disponible.");
            }

             Estadia estadia = new Estadia(
                    item.getFechaInicio(),
                    item.getFechaFin(),
                    habitacion,
                    TipoEstadoEstadia.RESERVADA
            );

            estadia = estadiaRepository.save(estadia);

            Reserva reserva = new Reserva();
            reserva.setCodigoReserva(codigoGrupo);
            reserva.setFechaReserva(LocalDate.now());
            reserva.setFechaInicio(item.getFechaInicio());
            reserva.setFechaFin(item.getFechaFin());
            reserva.setEstado(EstadoReserva.RESERVADA);
            reserva.setNombre(dto.getNombre());
            reserva.setApellido(dto.getApellido());
            reserva.setTelefono(dto.getTelefono());

            reserva.setEstadia(estadia);

            reservaRepository.save(reserva);
        }
    }
}