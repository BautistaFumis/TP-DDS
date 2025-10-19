package Logica.Gestores;

import Logica.Dominio.Huesped;
import Logica.Excepciones.CamposObligatoriosException;
import Logica.Excepciones.DocumentoDuplicadoException;
import Persistencia.EstadiaDAO;
import Persistencia.HuespedDAO;
import java.util.List;

/**
 * Gestiona la lógica de negocio para las operaciones relacionadas con los huéspedes.
 */
public class GestorHuesped {

    private final HuespedDAO huespedDAO;
    private final EstadiaDAO estadiaDAO;

    /**
     * Constructor que inyecta las dependencias de los DAO de Huésped y Estadía.
     *
     * @param huespedDAO El DAO para operaciones de huésped.
     * @param estadiaDAO El DAO para consultar estadías.
     */
    public GestorHuesped(HuespedDAO huespedDAO, EstadiaDAO estadiaDAO) {
        this.huespedDAO = huespedDAO;
        this.estadiaDAO = estadiaDAO;
    }

    /**
     * Valida y registra un nuevo huésped en el sistema, tiene en cuenta los campos obligatorios.
     *
     * @param huesped El objeto Huesped con los datos a registrar.
     * @throws CamposObligatoriosException Si alguno de los campos requeridos está vacío.
     * @throws DocumentoDuplicadoException Si ya existe un huésped con el mismo tipo y número de documento.
     */
    public void registrarNuevoHuesped(Huesped huesped) throws CamposObligatoriosException, DocumentoDuplicadoException {
        if (huesped.getCategoriaIVA() == null || huesped.getCategoriaIVA().trim().isEmpty())
            huesped.setCategoriaIVA("Consumidor Final");
        if (huesped.getApellido() == null || huesped.getApellido().trim().isEmpty() ||
                huesped.getNombre() == null || huesped.getNombre().trim().isEmpty() ||
                huesped.getTipoDocumento() == null || huesped.getTipoDocumento().trim().isEmpty() ||
                huesped.getDocumento() == null || huesped.getDocumento().trim().isEmpty() ||
                huesped.getFechaNacimiento() == null ||
                huesped.getDireccion() == null ||
                huesped.getDireccion().getCalle() == null || huesped.getDireccion().getCalle().trim().isEmpty() ||
                huesped.getDireccion().getNumero() <= 0 || // Un número de calle no puede ser 0 o negativo
                huesped.getDireccion().getCodigoPostal() == null || huesped.getDireccion().getCodigoPostal().trim().isEmpty() ||
                huesped.getDireccion().getLocalidad() == null || huesped.getDireccion().getLocalidad().trim().isEmpty() ||
                huesped.getDireccion().getProvincia() == null || huesped.getDireccion().getProvincia().trim().isEmpty() ||
                huesped.getDireccion().getPais() == null || huesped.getDireccion().getPais().trim().isEmpty()) {

            throw new CamposObligatoriosException("Debe completar todos los campos obligatorios (*).");
        }
        if (huespedDAO.buscarHuesped(huesped.getTipoDocumento(), huesped.getDocumento()).isPresent()) {
            throw new DocumentoDuplicadoException("¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
        }
        huespedDAO.altaHuesped(huesped);
    }

    /**
     * Registra un huésped sin realizar la validación de documento duplicado.
     *
     * @param huesped El objeto Huesped con los datos a registrar.
     */
    public void registrarHuespedAceptandoDuplicado(Huesped huesped) {
        huespedDAO.altaHuesped(huesped);
    }

    /**
     * Orquesta la búsqueda de huéspedes según múltiples criterios de filtrado.
     *
     * @return Una lista de objetos Huesped que coinciden con los criterios.
     */
    public List<Huesped> buscarHuespedes(String apellido, String nombre, String tipoDocumento, String documento) {
        return huespedDAO.buscarPorCriterios(apellido, nombre, tipoDocumento, documento);
    }

    /**
     * Valida los datos modificados de un huésped y solicita su actualización, teniendo en cuenta campos obligatorios.
     *
     * @param huesped El objeto Huesped con la información ya actualizada.
     * @throws CamposObligatoriosException Si alguno de los campos requeridos se deja en blanco.
     */
    public void modificarHuesped(Huesped huesped) throws CamposObligatoriosException {
        if (huesped.getApellido() == null || huesped.getApellido().trim().isEmpty() ||
                huesped.getNombre() == null || huesped.getNombre().trim().isEmpty() ||
                huesped.getTipoDocumento() == null || huesped.getTipoDocumento().trim().isEmpty() ||
                huesped.getDocumento() == null || huesped.getDocumento().trim().isEmpty() ||
                huesped.getFechaNacimiento() == null ||
                huesped.getDireccion() == null ||
                huesped.getDireccion().getCalle() == null || huesped.getDireccion().getCalle().trim().isEmpty() ||
                huesped.getDireccion().getNumero() <= 0 || // Un número de calle no puede ser 0 o negativo
                huesped.getDireccion().getCodigoPostal() == null || huesped.getDireccion().getCodigoPostal().trim().isEmpty() ||
                huesped.getDireccion().getLocalidad() == null || huesped.getDireccion().getLocalidad().trim().isEmpty() ||
                huesped.getDireccion().getProvincia() == null || huesped.getDireccion().getProvincia().trim().isEmpty() ||
                huesped.getDireccion().getPais() == null || huesped.getDireccion().getPais().trim().isEmpty()) {
                throw new CamposObligatoriosException("Debe completar todos los campos obligatorios (*).");
            }
            if (huesped.getCategoriaIVA() == null || huesped.getCategoriaIVA().trim().isEmpty()) {
                huesped.setCategoriaIVA("Consumidor Final"); //Si modificamos a vacio la categoria del IVA, la asignamos como CF
            }
            huespedDAO.modificarHuesped(huesped);
        }

        /**
         * Gestiona la eliminación de un huésped, verificando primero si tiene estadías asociadas.
         * @param huesped El huésped que se desea eliminar.
         * @return {@code true} si el huésped fue eliminado, {@code false} si no se pudo eliminar.
         */


    public boolean darDeBajaHuesped(Huesped huesped) {
        if (estadiaDAO.tieneEstadias(huesped)) {
            return false;
        }
        huespedDAO.eliminarHuesped(huesped.getDocumento());
        return true;
    }

}
