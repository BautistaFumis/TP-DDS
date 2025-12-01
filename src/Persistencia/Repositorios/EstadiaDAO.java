package Persistencia.Repositorios;

import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Entidades.Huesped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EstadiaDAO extends JpaRepository<Estadia, Long> {
    boolean existsByHuespedes(Huesped huesped);

    // Busca estadías activas que chocan con el rango
    // Una estadía ocupa el día si CheckIn <= dia < CheckOut
    @Query("SELECT e FROM Estadia e WHERE e.fechaCheckin < :hasta AND e.fechaCheckout > :desde")
    List<Estadia> buscarPorRango(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
}