package Persistencia.Repositorios;

import Logica.Dominio.Entidades.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaDAO extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findByCodigoReserva(String codigoReserva);

    @Query("SELECT r FROM Reserva r WHERE " +
            "(:apellido IS NULL OR LOWER(r.apellido) LIKE LOWER(CONCAT(:apellido, '%'))) AND " +
            "(:nombre IS NULL OR LOWER(r.nombre) LIKE LOWER(CONCAT(:nombre, '%'))) AND " +
            "r.estado = 'RESERVADA'") // Solo buscamos reservas activas
    List<Reserva> buscarPorNombreYApellido(
            @Param("nombre") String nombre,
            @Param("apellido") String apellido
    );
}