package Persistencia;

import Logica.Dominio.Huesped;
import java.util.List;
import java.util.Optional;

/**
 * Define el contrato para las operaciones de acceso a datos (DAO) de la entidad {@link Huesped}.
 * Esta interfaz abstrae la lógica de persistencia, permitiendo que las implementaciones
 * concretas manejen cómo se guardan, leen, modifican y eliminan los datos de los huéspedes.
 *
 */
public interface HuespedDAO {

    /**
     * Persiste un nuevo huésped en la fuente de datos.
     *
     * @param huesped El objeto {@link Huesped} que se va a agregar. Debe contener todos los datos necesarios.
     */
    void altaHuesped(Huesped huesped);

    /**
     * Busca un único huésped por su tipo y número de documento.
     * El uso de {@link Optional} es para manejar de forma segura el caso en que
     * no se encuentre ningún huésped con los datos proporcionados, evitando NullPointerExceptions.
     *
     * @param tipoDocumento El tipo de documento del huésped a buscar (ej: "DNI").
     * @param documento El número de documento del huésped a buscar.
     * @return Un {@link Optional} que contiene el objeto {@link Huesped} si se encuentra,
     * o un Optional vacío si no hay coincidencias.
     */
    Optional<Huesped> buscarHuesped(String tipoDocumento, String documento);

    /**
     * Actualiza la información de un huésped existente.
     * Busca al huésped usando los identificadores originales y reemplaza sus datos.
     * @param tipoDocumentoOriginal El tipo de documento ANTES de la modificación.
     * @param documentoOriginal El número de documento ANTES de la modificación.
     * @param huespedConNuevosDatos El objeto {@link Huesped} con todos los datos actualizados.
     */
    void modificarHuesped(String tipoDocumentoOriginal, String documentoOriginal, Huesped huespedConNuevosDatos); // <-- Firma modificada
    /**
     * Elimina un huésped de la fuente de datos utilizando su número de documento como identificador único.
     *
     * @param documento El número de documento del huésped que se desea eliminar.
     */

    void eliminarHuesped(String documento);

    /**
     * Busca una lista de huéspedes que coincidan con múltiples criterios de búsqueda.
     * Si un criterio se proporciona como nulo o vacío, se ignora en el filtro.
     * La búsqueda por apellido y nombre es sensible a mayúsculas/minúsculas y busca por prefijo ("empieza con").
     *
     * @param apellido Criterio de búsqueda para el apellido.
     * @param nombre Criterio de búsqueda para el nombre.
     * @param tipoDocumento Criterio de búsqueda para el tipo de documento (comparación exacta, ignorando mayúsculas/minúsculas).
     * @param numeroDocumento Criterio de búsqueda para el número de documento (comparación exacta).
     * @return Una {@link List} de objetos {@link Huesped} que coinciden con los criterios.
     * Si no se encuentran coincidencias, devuelve una lista vacía.
     */
    List<Huesped> buscarPorCriterios(String apellido, String nombre, String tipoDocumento, String numeroDocumento);
}