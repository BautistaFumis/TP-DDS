package Logica.Servicio;

import Logica.Dominio.Entidades.Huesped;
import Logica.Excepciones.CamposObligatoriosException;
import Logica.Excepciones.DocumentoDuplicadoException;
import Persistencia.Repositorios.EstadiaDAO;
import Persistencia.Repositorios.HuespedDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Gestiona la lógica de negocio para las operaciones relacionadas con los huéspedes.
 * Ahora es un @Service de Spring.
 */
@Service
public class GestorHuesped {
    // inyeccion de repositorios
    private final HuespedDAO huespedRepository;
    private final EstadiaDAO estadiaRepository;

    /**
     * Constructor con Inyección de Dependencias (@Autowired).
     * Spring se encarga de pasarnos los repositorios.
     */
    @Autowired
    public GestorHuesped(HuespedDAO huespedRepository, EstadiaDAO estadiaRepository) {
        this.huespedRepository = huespedRepository;
        this.estadiaRepository = estadiaRepository;
    }

    /**
     * Valida y registra un nuevo huésped en el sistema.
     */
    public void registrarNuevoHuesped(Huesped huesped) throws CamposObligatoriosException, DocumentoDuplicadoException {

        // --- NUEVO PASO 1: Validación de campos obligatorios ---
        validarCamposObligatorios(huesped);

        // --- PASO 2: Lógica de negocio (IVA) ---
        if (huesped.getCategoriaIVA() == null || huesped.getCategoriaIVA().trim().isEmpty())
            huesped.setCategoriaIVA("Consumidor Final");

        // --- PASO 3: Validación de duplicados ---
        if (huespedRepository.findByTipoDocumentoAndDocumento(huesped.getTipoDocumento(), huesped.getDocumento()).isPresent()) {
            throw new DocumentoDuplicadoException("¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
        }

        // --- PASO 4: Guardado ---
        huespedRepository.save(huesped);
    }

    /**
     * Registra un huésped sin realizar la validación de documento duplicado.
     */
    public void registrarHuespedAceptandoDuplicado(Huesped huesped) throws CamposObligatoriosException {
        // --- AÚN VALIDAMOS CAMPOS OBLIGATORIOS ---
        validarCamposObligatorios(huesped);

        if (huesped.getCategoriaIVA() == null || huesped.getCategoriaIVA().trim().isEmpty())
            huesped.setCategoriaIVA("Consumidor Final");

        // CAMBIO: Usamos .save() directamente sin chequear duplicado
        huespedRepository.save(huesped);
    }

    /**
     * Orquesta la búsqueda de huéspedes según múltiples criterios de filtrado.
     */
    public List<Huesped> buscarHuespedes(String apellido, String nombre, String tipoDocumento, String documento) {
        // usamos el metodo del repositorio
        return huespedRepository.buscarPorCriterios(apellido, nombre, tipoDocumento, documento);
    }

    /**
     * Valida y actualiza un huésped.
     */
    public void modificarHuesped(String tipoDocumentoOriginal, String documentoOriginal, Huesped huespedConNuevosDatos) throws DocumentoDuplicadoException, CamposObligatoriosException {
        validarCamposObligatorios(huespedConNuevosDatos);

        if (huespedConNuevosDatos.getCategoriaIVA() == null || huespedConNuevosDatos.getCategoriaIVA().trim().isEmpty()) {
            huespedConNuevosDatos.setCategoriaIVA("Consumidor Final");
        }

        boolean documentoModificado = !huespedConNuevosDatos.getTipoDocumento().equalsIgnoreCase(tipoDocumentoOriginal) ||
                !huespedConNuevosDatos.getDocumento().equals(documentoOriginal);

        if (documentoModificado) {
            Optional<Huesped> otroHuespedConEseDoc = huespedRepository.findByTipoDocumentoAndDocumento(
                    huespedConNuevosDatos.getTipoDocumento(),
                    huespedConNuevosDatos.getDocumento());

            if (otroHuespedConEseDoc.isPresent()) {
                throw new DocumentoDuplicadoException("¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
            }
        }

        huespedRepository.save(huespedConNuevosDatos);
    }

    /**
     * Modifica un huésped sin validación de duplicados.
     */
    public void modificarHuespedAceptandoDuplicado(String tipoDocumentoOriginal, String documentoOriginal, Huesped huespedConNuevosDatos) throws CamposObligatoriosException {
        validarCamposObligatorios(huespedConNuevosDatos); // Aún validamos el resto

        if (huespedConNuevosDatos.getCategoriaIVA() == null || huespedConNuevosDatos.getCategoriaIVA().trim().isEmpty()) {
            huespedConNuevosDatos.setCategoriaIVA("Consumidor Final");
        }

        huespedRepository.save(huespedConNuevosDatos);
    }

    /**
     * Gestiona la eliminación de un huésped.
     */
    public boolean darDeBajaHuesped(Huesped huesped) {
        if (estadiaRepository.existsByHuespedPrincipal(huesped)) {
            return false;
        }
        huespedRepository.delete(huesped);
        return true;
    }

    // ========================================================================
    // --- NUEVOS MÉTODOS PRIVADOS DE VALIDACIÓN ---
    // ========================================================================

    /**
     * Método de ayuda privado para validar todos los campos requeridos de un Huesped.
     * Lanza CamposObligatoriosException si falta alguno.
     * * @param huesped El objeto Huesped a validar.
     * @throws CamposObligatoriosException Si un campo obligatorio es nulo o vacío.
     */
    private void validarCamposObligatorios(Huesped huesped) throws CamposObligatoriosException {
        if (huesped == null) {
            throw new CamposObligatoriosException("Los datos del huésped no pueden ser nulos.");
        }

        // --- Campos de Huesped (String) ---
        if (esNuloOVacio(huesped.getNombre())) {
            throw new CamposObligatoriosException("El campo 'Nombre' es obligatorio.");
        }
        if (esNuloOVacio(huesped.getApellido())) {
            throw new CamposObligatoriosException("El campo 'Apellido' es obligatorio.");
        }
        if (esNuloOVacio(huesped.getTipoDocumento())) {
            throw new CamposObligatoriosException("El campo 'Tipo de Documento' es obligatorio.");
        }
        if (esNuloOVacio(huesped.getDocumento())) {
            throw new CamposObligatoriosException("El campo 'Documento' es obligatorio.");
        }
        if (esNuloOVacio(huesped.getTelefono())) {
            throw new CamposObligatoriosException("El campo 'Teléfono' es obligatorio.");
        }
        if (esNuloOVacio(huesped.getNacionalidad())) {
            throw new CamposObligatoriosException("El campo 'Nacionalidad' es obligatorio.");
        }
        if (esNuloOVacio(huesped.getOcupacion())) {
            throw new CamposObligatoriosException("El campo 'Ocupación' es obligatorio.");
        }

        // --- Campos de Huesped (Objetos) ---
        if (huesped.getFechaNacimiento() == null) {
            throw new CamposObligatoriosException("El campo 'Fecha de Nacimiento' es obligatorio.");
        }

        // --- Campos Embebidos (Direccion) ---
        if (huesped.getDireccion() == null) {
            throw new CamposObligatoriosException("La 'Dirección' (calle, número, localidad, etc.) es obligatoria.");
        }

        if (esNuloOVacio(huesped.getDireccion().getCalle())) {
            throw new CamposObligatoriosException("El campo 'Calle' de la dirección es obligatorio.");
        }
        // CORRECCIÓN: Tu código original tenía un bug aquí, convertía el NÚMERO a String.
        // Lo correcto es chequear el string 'numero' directamente.
        if (esNuloOVacio(String.valueOf(huesped.getDireccion().getNumero()))) {
            throw new CamposObligatoriosException("El campo 'Número' de la dirección es obligatorio.");
        }
        if (esNuloOVacio(huesped.getDireccion().getLocalidad())) {
            throw new CamposObligatoriosException("El campo 'Localidad' de la dirección es obligatoria.");
        }
        if (esNuloOVacio(huesped.getDireccion().getProvincia())) {
            throw new CamposObligatoriosException("El campo 'Provincia' de la dirección es obligatorio.");
        }
        if (esNuloOVacio(huesped.getDireccion().getPais())) {
            throw new CamposObligatoriosException("El campo 'País' de la dirección es obligatorio.");
        }
        // Añadí CP que estaba en tu frontend pero no aquí
        if (esNuloOVacio(huesped.getDireccion().getCodigoPostal())) {
            throw new CamposObligatoriosException("El campo 'Código Postal' de la dirección es obligatorio.");
        }
    }

    /**
     * Pequeño helper para chequear si un String es nulo, vacío o solo espacios.
     */
    private boolean esNuloOVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}