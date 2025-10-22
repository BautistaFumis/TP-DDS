package Persistencia;

import Logica.Dominio.Estadia;
import Logica.Dominio.Huesped;

/**
 * Define el contrato para las operaciones de acceso a datos (DAO) relacionadas con la entidad {@link Estadia}.
 *
 */
public interface EstadiaDAO {

    /**
     * Verifica si un huésped específico tiene registrada al menos una estadía en la fuente de datos.
     * Este metodo es crucial para lógicas de negocio como la eliminación de un huésped.
     *
     * @param huesped El objeto {@link Huesped} que se desea consultar.
     * @return {@code true} si el huésped tiene una o más estadías registradas, {@code false} en caso contrario.
     */

    boolean tieneEstadias(Huesped huesped);
}