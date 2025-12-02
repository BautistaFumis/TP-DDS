package Logica.Servicio;

import DTO.CrearOcupacionDTO;
import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Entidades.Habitacion;
import Logica.Dominio.Entidades.Huesped;
import Logica.Dominio.Enum.TipoEstadoEstadia;
import Persistencia.Repositorios.EstadiaDAO;
import Persistencia.Repositorios.HabitacionDAO;
import Persistencia.Repositorios.HuespedDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GestorEstadia {

    @Autowired private HabitacionDAO habitacionRepository;
    @Autowired private EstadiaDAO estadiaRepository;
    @Autowired private HuespedDAO huespedRepository;

    @Transactional
    public void registrarOcupacion(CrearOcupacionDTO dto) {
        // 1. Obtener la Habitación
        Habitacion habitacion = habitacionRepository.findById(dto.getIdHabitacion())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

        // 2. Obtener los Huéspedes (Responsable + Acompañantes seleccionados en el front)
        List<Huesped> listaHuespedes = huespedRepository.findAllById(dto.getIdsHuespedes());
        if (listaHuespedes.isEmpty()) {
            throw new RuntimeException("Debe existir al menos un huésped para registrar la ocupación.");
        }

        // 3. Buscar conflictos de agenda para esta habitación en esas fechas
        List<Estadia> conflictos = estadiaRepository.buscarPorRango(dto.getFechaInicio(), dto.getFechaFin());

        // Filtramos para obtener solo la estadía que conflicto con ESTA habitación
        Estadia estadiaConflicto = conflictos.stream()
                .filter(e -> e.getHabitacion().getId().equals(habitacion.getId()))
                .findFirst()
                .orElse(null);

        // --- LÓGICA CORE DEL CU15 ---
        if (estadiaConflicto != null) {

            // CASO A: La habitación ya está ocupada físicamente (Check-in ya hecho)
            if (estadiaConflicto.getTipoEstado() == TipoEstadoEstadia.ACTIVA) {
                throw new RuntimeException("La habitación ya se encuentra OCUPADA y activa. No se puede volver a ocupar.");
            }

            // CASO B: La habitación está RESERVADA
            if (estadiaConflicto.getTipoEstado() == TipoEstadoEstadia.RESERVADA) {
                if (dto.isEsOverrideReserva()) {
                    // IMPORTANTE: REUTILIZAMOS LA RESERVA Y LA ACTIVAMOS
                    // Actualizamos el estado a ACTIVA (Check-in realizado)
                    estadiaConflicto.setTipoEstado(TipoEstadoEstadia.ACTIVA);

                    // Actualizamos las fechas reales de ocupación (pueden variar levemente de la reserva original)
                    estadiaConflicto.setFechaCheckin(dto.getFechaInicio());
                    estadiaConflicto.setFechaCheckout(dto.getFechaFin());

                    // Actualizamos la lista de huéspedes (Agregamos acompañantes que no estaban en la reserva)
                    estadiaConflicto.setHuespedes(listaHuespedes);

                    estadiaRepository.save(estadiaConflicto);
                    return; // Fin del flujo exitoso (Reserva transformada en Ocupación)
                } else {
                    throw new RuntimeException("La habitación tiene una reserva vigente y no se confirmó la acción 'Ocupar Igual'.");
                }
            }
        }

        // CASO C: No hay conflictos (Habitación LIBRE) -> Creamos nueva estadía
        Estadia nuevaEstadia = new Estadia();
        nuevaEstadia.setHabitacion(habitacion);
        nuevaEstadia.setFechaCheckin(dto.getFechaInicio());
        nuevaEstadia.setFechaCheckout(dto.getFechaFin());
        nuevaEstadia.setTipoEstado(TipoEstadoEstadia.ACTIVA); // Estado ACTIVA = OCUPADA en la grilla
        nuevaEstadia.setHuespedes(listaHuespedes);

        estadiaRepository.save(nuevaEstadia);
    }
}