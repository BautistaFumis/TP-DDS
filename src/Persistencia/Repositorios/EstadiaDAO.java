package Persistencia.Repositorios;

import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Entidades.Huesped; // Asegúrate de que el import sea al paquete .Entidades
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadiaDAO extends JpaRepository<Estadia, Long> {

    boolean existsByHuespedPrincipal(Huesped huespedPrincipal);
}