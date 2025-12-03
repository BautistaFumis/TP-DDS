package Persistencia.Repositorios;

import Logica.Dominio.Entidades.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservaDAO extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findByCodigoReserva(String codigoReserva);

}