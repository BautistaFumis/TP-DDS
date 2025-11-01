package Persistencia.Repositorios;

import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Entidades.Huesped; // Asegúrate de que el import sea al paquete .Entidades
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Long> {

    /**
     * Reemplaza a EstadiaDAO.tieneEstadias()
     * * ¡AQUÍ ESTÁ LA CORRECCIÓN!
     * El nombre del método DEBE coincidir con el nombre del campo
     * "huespedPrincipal" en la Entidad Estadia.
     */
    boolean existsByHuespedPrincipal(Huesped huespedPrincipal);
}