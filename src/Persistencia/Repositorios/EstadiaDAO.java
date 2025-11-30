package Persistencia.Repositorios;

import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Entidades.Huesped; // Asegurate de tener este import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadiaDAO extends JpaRepository<Estadia, Long> {

    // CAMBIO AQUI: Antes era existsByHuespedPrincipal
    // Al llamarse 'huespedes' la lista en la entidad, Spring entiende que debe buscar si el huesped está en esa lista.
    boolean existsByHuespedes(Huesped huesped);

    // Si tenías otros métodos buscando por el viejo nombre, actualízalos también, por ejemplo:
    // List<Estadia> findByHuespedes(Huesped huesped);
}