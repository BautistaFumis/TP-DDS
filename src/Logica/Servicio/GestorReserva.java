package Logica.Servicio;

import DTO.CrearReservaDTO;
import DTO.ReservaBusquedaDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GestorReserva {

    @Autowired private ReservaDAO reservaRepository;
    @Autowired private HabitacionDAO habitacionRepository;
    @Autowired private EstadiaDAO estadiaRepository;

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

    // --- NUEVO: CU06 Cancelar Reserva ---

    public List<ReservaBusquedaDTO> buscarReservas(String nombre, String apellido) {
        List<Reserva> reservas = reservaRepository.buscarPorNombreYApellido(nombre, apellido);

        return reservas.stream().map(r -> {
            ReservaBusquedaDTO dto = new ReservaBusquedaDTO();
            dto.setIdReserva(r.getId());
            dto.setNombre(r.getNombre());
            dto.setApellido(r.getApellido());
            dto.setFechaInicio(r.getFechaInicio());
            dto.setFechaFin(r.getFechaFin());

            if (r.getEstadia() != null && r.getEstadia().getHabitacion() != null) {
                dto.setNumeroHabitacion(r.getEstadia().getHabitacion().getNumero());
                dto.setTipoHabitacion(r.getEstadia().getHabitacion().getClass().getSimpleName());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void cancelarReservas(List<Long> idsReservas) {
        for (Long id : idsReservas) {
            Reserva reserva = reservaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

            // Cambiamos estado de la reserva
            reserva.setEstado(EstadoReserva.CANCELADA);

            // Liberamos la estadía asociada (o la eliminamos/cancelamos para liberar fechas)
            if (reserva.getEstadia() != null) {
                Estadia estadia = reserva.getEstadia();
                estadia.setTipoEstado(TipoEstadoEstadia.CANCELADA); // Esto libera la habitación en el buscador
                estadiaRepository.save(estadia);
            }
            reservaRepository.save(reserva);
        }
    }
}