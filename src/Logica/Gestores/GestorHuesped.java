package Logica.Gestores;

import Logica.Dominio.Entidades.Huesped;
import Logica.Excepciones.CamposObligatoriosException;
import Logica.Excepciones.DocumentoDuplicadoException;
// ADIÓS: import Persistencia.DAOFactory;
// ADIÓS: import Persistencia.EstadiaDAO;
// ADIÓS: import Persistencia.HuespedDAO;

// NUEVO: Importamos los repositorios
import Persistencia.Repositorios.EstadiaRepository;
import Persistencia.Repositorios.HuespedRepository;

// NUEVO: Importaciones de Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Gestiona la lógica de negocio para las operaciones relacionadas con los huéspedes.
 * Ahora es un @Service de Spring.
 */
@Service // NUEVO
public class GestorHuesped {

    // NUEVO: Inyectamos los repositorios
    private final HuespedRepository huespedRepository;
    private final EstadiaRepository estadiaRepository;

    /**
     * Constructor con Inyección de Dependencias (@Autowired).
     * Spring se encarga de pasarnos los repositorios.
     */
    @Autowired // NUEVO
    public GestorHuesped(HuespedRepository huespedRepository, EstadiaRepository estadiaRepository) {
        this.huespedRepository = huespedRepository;
        this.estadiaRepository = estadiaRepository;
    }

    /**
     * Valida y registra un nuevo huésped en el sistema.
     * (La lógica interna es la misma, solo cambia la llamada final)
     */
    public void registrarNuevoHuesped(Huesped huesped) throws CamposObligatoriosException, DocumentoDuplicadoException {
        if (huesped.getCategoriaIVA() == null || huesped.getCategoriaIVA().trim().isEmpty())
            huesped.setCategoriaIVA("Consumidor Final");
        completarCampos(huesped);

        // CAMBIO: Usamos el repositorio
        if (huespedRepository.findByTipoDocumentoAndDocumento(huesped.getTipoDocumento(), huesped.getDocumento()).isPresent()) {
            throw new DocumentoDuplicadoException("¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
        }
        // CAMBIO: Usamos .save() en lugar de .altaHuesped()
        huespedRepository.save(huesped);
    }

    /**
     * Registra un huésped sin realizar la validación de documento duplicado.
     */
    public void registrarHuespedAceptandoDuplicado(Huesped huesped) {
        // CAMBIO: Usamos .save()
        huespedRepository.save(huesped);
    }

    /**
     * Orquesta la búsqueda de huéspedes según múltiples criterios de filtrado.
     */
    public List<Huesped> buscarHuespedes(String apellido, String nombre, String tipoDocumento, String documento) {
        // CAMBIO: Usamos el método del repositorio
        return huespedRepository.buscarPorCriterios(apellido, nombre, tipoDocumento, documento);
    }

    /**
     * Valida y actualiza un huésped.
     */
    public void modificarHuesped(String tipoDocumentoOriginal, String documentoOriginal, Huesped huespedConNuevosDatos)  throws CamposObligatoriosException, DocumentoDuplicadoException {

        completarCampos(huespedConNuevosDatos);
        if (huespedConNuevosDatos.getCategoriaIVA() == null || huespedConNuevosDatos.getCategoriaIVA().trim().isEmpty()) {
            huespedConNuevosDatos.setCategoriaIVA("Consumidor Final");
        }

        boolean documentoModificado = !huespedConNuevosDatos.getTipoDocumento().equalsIgnoreCase(tipoDocumentoOriginal) ||
                !huespedConNuevosDatos.getDocumento().equals(documentoOriginal);

        if (documentoModificado) {
            // CAMBIO: Usamos el repositorio
            Optional<Huesped> otroHuespedConEseDoc = huespedRepository.findByTipoDocumentoAndDocumento(
                    huespedConNuevosDatos.getTipoDocumento(),
                    huespedConNuevosDatos.getDocumento());

            if (otroHuespedConEseDoc.isPresent()) {
                // Pequeña mejora: asegurarnos que no sea él mismo (aunque tu lógica original no lo hacía, esto es más seguro)
                // if (!otroHuespedConEseDoc.get().getId().equals(huespedConNuevosDatos.getId())) {
                throw new DocumentoDuplicadoException("¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
                // }
            }
        }

        // CAMBIO: .save() maneja la modificación automáticamente si el Huesped ya tiene un ID.
        // La lógica original de pasar el documento original ya no es necesaria si tenemos el ID.
        // Asumimos que "huespedConNuevosDatos" tiene el ID del original.
        huespedRepository.save(huespedConNuevosDatos);
    }

    /**
     * Modifica un huésped sin validación de duplicados.
     */
    public void modificarHuespedAceptandoDuplicado(String tipoDocumentoOriginal, String documentoOriginal, Huesped huespedConNuevosDatos){
        // CAMBIO: .save()
        huespedRepository.save(huespedConNuevosDatos);
    }

    /**
     * Gestiona la eliminación de un huésped.
     */
    public boolean darDeBajaHuesped(Huesped huesped) {
        // CAMBIO: Usamos el repositorio de estadia
        if (estadiaRepository.existsByHuespedPrincipal(huesped)) {
            return false;
        }
        // CAMBIO: Usamos .delete()
        huespedRepository.delete(huesped);
        return true;
    }
    /**
     * Gestiona la validacion de campos para un huesped. Una funcion privada al gestor que utilizamos en dos metos y nos viene
     * bien reutilizarla
     * @param huesped El objeto Huesped que desea verificar.
     * @throws CamposObligatoriosException Si alguno de los campos requeridos se deja en blanco..
     */

    private void completarCampos(Huesped huesped) throws CamposObligatoriosException  {
        if (huesped.getApellido() == null || huesped.getApellido().trim().isEmpty() ||
                huesped.getNombre() == null || huesped.getNombre().trim().isEmpty() ||
                huesped.getTipoDocumento() == null || huesped.getTipoDocumento().trim().isEmpty() ||
                huesped.getDocumento() == null || huesped.getDocumento().trim().isEmpty() ||
                huesped.getFechaNacimiento() == null ||
                huesped.getDireccion() == null ||
                huesped.getDireccion().getCalle() == null || huesped.getDireccion().getCalle().trim().isEmpty() ||
                huesped.getDireccion().getNumero() == null || huesped.getDireccion().getNumero() <= 0 ||
                huesped.getDireccion().getCodigoPostal() == null || huesped.getDireccion().getCodigoPostal().trim().isEmpty() ||
                huesped.getDireccion().getLocalidad() == null || huesped.getDireccion().getLocalidad().trim().isEmpty() ||
                huesped.getDireccion().getProvincia() == null || huesped.getDireccion().getProvincia().trim().isEmpty() ||
                huesped.getDireccion().getPais() == null || huesped.getDireccion().getPais().trim().isEmpty()) {
            throw new CamposObligatoriosException("Debe completar todos los campos obligatorios (*).");
        }
    }
}
