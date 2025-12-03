package Persistencia.Repositorios;

import Logica.Dominio.Entidades.Habitacion;
import Logica.Dominio.Enum.EstadoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionDAO extends JpaRepository<Habitacion, Long> {

    Optional<Habitacion> findByNumero(String numero);

    List<Habitacion> findByEstado(EstadoHabitacion estado);


    List<Habitacion> findByCostoNocheLessThanEqual(Float costoMaximo);


}