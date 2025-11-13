package Persistencia.Repositorios;

import Logica.Dominio.Entidades.Huesped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HuespedDAO extends JpaRepository<Huesped, Long> { // Maneja Huesped, ID es Long

    Optional<Huesped> findByTipoDocumentoAndDocumento(String tipoDocumento, String documento);

    @Query("SELECT h FROM Huesped h WHERE " +
            "(:apellido IS NULL OR :apellido = '' OR LOWER(h.apellido) LIKE LOWER(CONCAT(:apellido, '%'))) AND " +
            "(:nombre IS NULL OR :nombre = '' OR LOWER(h.nombre) LIKE LOWER(CONCAT(:nombre, '%'))) AND " +
            "(:tipoDocumento IS NULL OR :tipoDocumento = '' OR h.tipoDocumento = :tipoDocumento) AND " +
            "(:documento IS NULL OR :documento = '' OR h.documento = :documento)")
    List<Huesped> buscarPorCriterios(
            @Param("apellido") String apellido,
            @Param("nombre") String nombre,
            @Param("tipoDocumento") String tipoDocumento,
            @Param("documento") String documento
    );
}