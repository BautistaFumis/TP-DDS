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
@Service // NUEVO
public class GestorHuesped {
    // inyeccion de repositorios
    private final HuespedDAO huespedRepository;
    private final EstadiaDAO estadiaRepository;

    /**
     * Constructor con Inyección de Dependencias (@Autowired).
     * Spring se encarga de pasarnos los repositorios.
     */
    @Autowired // NUEVO
    public GestorHuesped(HuespedDAO huespedRepository, EstadiaDAO estadiaRepository) {
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

        // CAMBIO: Usamos el repositorio
        if (huespedRepository.findByTipoDocumentoAndDocumento(huesped.getTipoDocumento(), huesped.getDocumento()).isPresent()) {
            throw new DocumentoDuplicadoException("¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
        }
        // usamos .save() en lugar de .altaHuesped()
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
        // usamos el metodo del repositorio
        return huespedRepository.buscarPorCriterios(apellido, nombre, tipoDocumento, documento);
    }

    /**
     * Valida y actualiza un huésped.
     */
    public void modificarHuesped(String tipoDocumentoOriginal, String documentoOriginal, Huesped huespedConNuevosDatos)  throws  DocumentoDuplicadoException {

        if (huespedConNuevosDatos.getCategoriaIVA() == null || huespedConNuevosDatos.getCategoriaIVA().trim().isEmpty()) {
            huespedConNuevosDatos.setCategoriaIVA("Consumidor Final");
        }

        boolean documentoModificado = !huespedConNuevosDatos.getTipoDocumento().equalsIgnoreCase(tipoDocumentoOriginal) ||
                !huespedConNuevosDatos.getDocumento().equals(documentoOriginal);

        if (documentoModificado) {
            // usamos el repositorio
            Optional<Huesped> otroHuespedConEseDoc = huespedRepository.findByTipoDocumentoAndDocumento(
                    huespedConNuevosDatos.getTipoDocumento(),
                    huespedConNuevosDatos.getDocumento());

            if (otroHuespedConEseDoc.isPresent()) {
                // asegurarnos que no sea él mismo (aunque tu lógica original no lo hacía, esto es más seguro)
                // if (!otroHuespedConEseDoc.get().getId().equals(huespedConNuevosDatos.getId())) {
                throw new DocumentoDuplicadoException("¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
                // }
            }
        }

        huespedRepository.save(huespedConNuevosDatos);
    }

    /**
     * Modifica un huésped sin validación de duplicados.
     */
    public void modificarHuespedAceptandoDuplicado(String tipoDocumentoOriginal, String documentoOriginal, Huesped huespedConNuevosDatos){
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

}
