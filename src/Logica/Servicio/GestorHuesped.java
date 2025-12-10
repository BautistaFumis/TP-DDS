package Logica.Servicio;

import DTO.DireccionDTO;
import DTO.HuespedAltaDTO;
import Logica.Dominio.Entidades.Direccion;
import Logica.Dominio.Entidades.Huesped;
import Logica.Excepciones.CamposObligatoriosException;
import Logica.Excepciones.DocumentoDuplicadoException;
import Logica.Excepciones.EntidadNoEncontradaException;
import Logica.Excepciones.OperacionNoPermitidaException;
import Persistencia.Repositorios.EstadiaDAO;
import Persistencia.Repositorios.HuespedDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GestorHuesped {

    private final HuespedDAO huespedRepository;
    private final EstadiaDAO estadiaRepository;

    @Autowired
    public GestorHuesped(HuespedDAO huespedRepository, EstadiaDAO estadiaRepository) {
        this.huespedRepository = huespedRepository;
        this.estadiaRepository = estadiaRepository;
    }

    // --- CONVERSIONES Y VALIDACIONES ---

    public Huesped convertirHuesped(HuespedAltaDTO dto) {
        Huesped huesped = new Huesped();
        huesped.setNombre(dto.getNombre());
        huesped.setApellido(dto.getApellido());
        huesped.setEmail(dto.getEmail());
        huesped.setTipoDocumento(dto.getTipoDocumento());
        huesped.setDocumento(dto.getDocumento());
        huesped.setTelefono(dto.getTelefono());
        huesped.setFechaNacimiento(dto.getFechaNacimiento());
        huesped.setOcupacion(dto.getOcupacion());
        huesped.setNacionalidad(dto.getNacionalidad());
        huesped.setCuit(dto.getCuit());

        if (dto.getCategoriaIVA() == null || dto.getCategoriaIVA().trim().isEmpty()) {
            huesped.setCategoriaIVA("Consumidor Final");
        } else {
            huesped.setCategoriaIVA(dto.getCategoriaIVA());
        }

        if (dto.getDireccion() != null) {
            DireccionDTO dirDto = dto.getDireccion();
            Direccion direccion = new Direccion();
            direccion.setCalle(dirDto.getCalle());
            direccion.setNumero(dirDto.getNumero());
            direccion.setDepartamento(dirDto.getDepartamento());
            direccion.setPiso(dirDto.getPiso());
            direccion.setCodigoPostal(dirDto.getCodigoPostal());
            direccion.setLocalidad(dirDto.getLocalidad());
            direccion.setProvincia(dirDto.getProvincia());
            direccion.setPais(dirDto.getPais());
            huesped.setDireccion(direccion);
        }
        return huesped;
    }

    private void validarCamposObligatorios(Huesped huesped) throws CamposObligatoriosException {
        if (huesped == null) throw new CamposObligatoriosException("Los datos del huésped no pueden ser nulos.");
        if (esNuloOVacio(huesped.getNombre())) throw new CamposObligatoriosException("El campo 'Nombre' es obligatorio.");
        if (esNuloOVacio(huesped.getApellido())) throw new CamposObligatoriosException("El campo 'Apellido' es obligatorio.");
        if (esNuloOVacio(huesped.getTipoDocumento())) throw new CamposObligatoriosException("El campo 'Tipo de Documento' es obligatorio.");
        if (esNuloOVacio(huesped.getDocumento())) throw new CamposObligatoriosException("El campo 'Documento' es obligatorio.");
        if (esNuloOVacio(huesped.getTelefono())) throw new CamposObligatoriosException("El campo 'Teléfono' es obligatorio.");
        if (esNuloOVacio(huesped.getNacionalidad())) throw new CamposObligatoriosException("El campo 'Nacionalidad' es obligatorio.");
        if (esNuloOVacio(huesped.getOcupacion())) throw new CamposObligatoriosException("El campo 'Ocupación' es obligatorio.");
        if (huesped.getFechaNacimiento() == null) throw new CamposObligatoriosException("El campo 'Fecha de Nacimiento' es obligatorio.");

        if (huesped.getDireccion() == null) throw new CamposObligatoriosException("La 'Dirección' es obligatoria.");

        Direccion dir = huesped.getDireccion();
        if (esNuloOVacio(dir.getCalle())) throw new CamposObligatoriosException("El campo 'Calle' es obligatorio.");
        if (dir.getNumero() == null) throw new CamposObligatoriosException("El campo 'Número' es obligatorio.");
        if (esNuloOVacio(dir.getLocalidad())) throw new CamposObligatoriosException("El campo 'Localidad' es obligatoria.");
        if (esNuloOVacio(dir.getProvincia())) throw new CamposObligatoriosException("El campo 'Provincia' es obligatorio.");
        if (esNuloOVacio(dir.getPais())) throw new CamposObligatoriosException("El campo 'País' es obligatorio.");
        if (esNuloOVacio(dir.getCodigoPostal())) throw new CamposObligatoriosException("El campo 'Código Postal' es obligatorio.");
    }

    private boolean esNuloOVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    // --- ALTA (REGISTRAR) ---

    public void registrarNuevoHuesped(Huesped huesped) throws CamposObligatoriosException, DocumentoDuplicadoException {
        validarCamposObligatorios(huesped);
        if (huesped.getCategoriaIVA() == null || huesped.getCategoriaIVA().trim().isEmpty())
            huesped.setCategoriaIVA("Consumidor Final");

        if (huespedRepository.findByTipoDocumentoAndDocumento(huesped.getTipoDocumento(), huesped.getDocumento()).isPresent()) {
            throw new DocumentoDuplicadoException("¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
        }
        huespedRepository.save(huesped);
    }

    public void registrarHuespedAceptandoDuplicado(Huesped huesped) throws CamposObligatoriosException {
        validarCamposObligatorios(huesped);
        if (huesped.getCategoriaIVA() == null || huesped.getCategoriaIVA().trim().isEmpty())
            huesped.setCategoriaIVA("Consumidor Final");
        huespedRepository.save(huesped);
    }

    // --- BÚSQUEDA ---

    public List<Huesped> buscarHuesped() {
        return huespedRepository.findAll();
    }

    public List<Huesped> buscarHuespedes(String apellido, String nombre, String tipoDocumento, String documento) {
        boolean todosNulos = (apellido == null || apellido.isEmpty()) &&
                (nombre == null || nombre.isEmpty()) &&
                (tipoDocumento == null || tipoDocumento.isEmpty()) &&
                (documento == null || documento.isEmpty());

        if (todosNulos) {
            return buscarHuesped();
        } else {
            return huespedRepository.buscarPorCriterios(apellido, nombre, tipoDocumento, documento);
        }
    }

    public Huesped obtenerHuespedPorId(Long id) throws EntidadNoEncontradaException {
        return huespedRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("El huésped no existe."));
    }

    // --- MODIFICACIÓN (CU10) ---

    public Huesped modificarHuesped(Long id, HuespedAltaDTO dto, boolean forzarDuplicado)
            throws EntidadNoEncontradaException, CamposObligatoriosException, DocumentoDuplicadoException {

        Huesped huespedExistente = huespedRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("El huésped con ID " + id + " no existe."));

        Huesped datosNuevos = convertirHuesped(dto);
        validarCamposObligatorios(datosNuevos);

        // Verificamos si existe OTRO usuario con el mismo documento
        Optional<Huesped> posibleDuplicado = huespedRepository.findByTipoDocumentoAndDocumento(
                datosNuevos.getTipoDocumento(),
                datosNuevos.getDocumento()
        );

        if (posibleDuplicado.isPresent()) {
            // Si el ID del encontrado NO es el mismo que estamos editando, hay conflicto
            if (!posibleDuplicado.get().getId().equals(id)) {
                if (!forzarDuplicado) {
                    throw new DocumentoDuplicadoException("¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
                }
            }
        }

        actualizarCamposHuesped(huespedExistente, datosNuevos);
        return huespedRepository.save(huespedExistente);
    }

    // --- BAJA (CU11) ---

    public void darDeBajaHuesped(Long id) throws EntidadNoEncontradaException, OperacionNoPermitidaException {
        Huesped huesped = huespedRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("El huésped no existe."));

        if (estadiaRepository.existsByHuespedes(huesped)) {
            throw new OperacionNoPermitidaException("El huésped no puede ser eliminado pues se ha alojado en el Hotel en alguna oportunidad.");
        }

        huespedRepository.delete(huesped);
    }

    private void actualizarCamposHuesped(Huesped destino, Huesped origen) {
        destino.setNombre(origen.getNombre());
        destino.setApellido(origen.getApellido());
        destino.setEmail(origen.getEmail());
        destino.setTipoDocumento(origen.getTipoDocumento());
        destino.setDocumento(origen.getDocumento());
        destino.setTelefono(origen.getTelefono());
        destino.setFechaNacimiento(origen.getFechaNacimiento());
        destino.setOcupacion(origen.getOcupacion());
        destino.setNacionalidad(origen.getNacionalidad());
        destino.setCuit(origen.getCuit());
        destino.setCategoriaIVA(origen.getCategoriaIVA());

        if (destino.getDireccion() != null && origen.getDireccion() != null) {
            Direccion dirDest = destino.getDireccion();
            Direccion dirOrig = origen.getDireccion();
            dirDest.setCalle(dirOrig.getCalle());
            dirDest.setNumero(dirOrig.getNumero());
            dirDest.setDepartamento(dirOrig.getDepartamento());
            dirDest.setPiso(dirOrig.getPiso());
            dirDest.setCodigoPostal(dirOrig.getCodigoPostal());
            dirDest.setLocalidad(dirOrig.getLocalidad());
            dirDest.setProvincia(dirOrig.getProvincia());
            dirDest.setPais(dirOrig.getPais());
        } else if (origen.getDireccion() != null) {
            destino.setDireccion(origen.getDireccion());
        }
    }
}