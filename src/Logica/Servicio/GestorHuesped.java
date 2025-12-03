package Logica.Servicio;

import DTO.DireccionDTO;
import DTO.HuespedAltaDTO;
import Logica.Dominio.Entidades.Direccion;
import Logica.Dominio.Entidades.Huesped;
import Logica.Excepciones.CamposObligatoriosException;
import Logica.Excepciones.DocumentoDuplicadoException;
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


    public void darDeBajaHuesped(Huesped huesped) {
        if (!estadiaRepository.existsByHuespedes(huesped)) {
            huespedRepository.delete(huesped);
        } else {
            System.out.println("No se puede borrar huésped con historial.");
        }
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
}