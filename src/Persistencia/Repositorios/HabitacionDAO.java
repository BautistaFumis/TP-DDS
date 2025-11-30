package Persistencia.Repositorios;

import Logica.Dominio.Entidades.Habitacion;
import Logica.Dominio.Enum.EstadoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionDAO extends JpaRepository<Habitacion, Long> {

    /**
     * Busca una habitación por su número visible (ej: "101", "202").
     * Devuelve Optional porque puede no existir.
     */
    Optional<Habitacion> findByNumero(String numero);

    /**
     * Devuelve todas las habitaciones que estén en un estado específico.
     * Ej: Buscar todas las "DISPONIBLES".
     */
    List<Habitacion> findByEstado(EstadoHabitacion estado);

    /**
     * (Opcional) Buscar habitaciones por precio máximo.
     * Útil para filtrar presupuestos.
     */
    List<Habitacion> findByCostoNocheLessThanEqual(Float costoMaximo);

    // Nota: El ABM (save, delete, etc.) ya viene incluido por JpaRepository.
}