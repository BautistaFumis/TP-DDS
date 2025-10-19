package Persistencia;

import Logica.Dominio.Huesped;
import java.util.List;
import java.util.Optional;

/**
 * Define el contrato para las operaciones de acceso a datos (DAO) de la entidad {@link Huesped}.
 *
 */
public interface HuespedDAO {

    void altaHuesped(Huesped huesped);
    Optional<Huesped> buscarHuesped(String tipoDocumento, String documento);
    void modificarHuesped(Huesped huesped);
    void eliminarHuesped(String documento);
    List<Huesped> buscarPorCriterios(String apellido, String nombre, String tipoDocumento, String documento);
}