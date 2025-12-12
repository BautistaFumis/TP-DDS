package Persistencia.Repositorios;

import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Entidades.Habitacion; // Asegúrate de tener este import
import Logica.Dominio.Entidades.Huesped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstadiaDAO extends JpaRepository<Estadia, Long> {

    // Método existente (asumo que ya lo tenías por el contexto anterior)
    boolean existsByHuespedes(Huesped huesped);

    // Método existente del GestorReserva anterior
    @Query("SELECT e FROM Estadia e WHERE " +
            "(e.fechaCheckin < :fechaFin AND e.fechaCheckout > :fechaInicio) AND " +
            "e.tipoEstado <> 'CANCELADA'")
    List<Estadia> buscarPorRango(@Param("fechaInicio") LocalDate fechaInicio,
                                 @Param("fechaFin") LocalDate fechaFin);

    // NUEVO: Para facturación (buscar estadía ocupada actual por habitación)
    @Query("SELECT e FROM Estadia e WHERE e.habitacion.numero = :numeroHabitacion AND e.tipoEstado = 'ACTIVA'")
    Optional<Estadia> findEstadiaActivaPorHabitacion(@Param("numeroHabitacion") String numeroHabitacion);
}