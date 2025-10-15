package Logica.Gestores;

import Logica.Dominio.Huesped;
import Logica.Excepciones.CamposObligatoriosException;
import Logica.Excepciones.DocumentoDuplicadoException;
import Persistencia.HuespedDAO;

import java.util.List;

public class GestorHuesped {

    private final HuespedDAO huespedDAO;

    public GestorHuesped(HuespedDAO huespedDAO) {
        this.huespedDAO = huespedDAO;
    }

    public void registrarNuevoHuesped(Huesped huesped) throws CamposObligatoriosException, DocumentoDuplicadoException {
        // --- VALIDACIÓN ACTUALIZADA ---
        // Se comprueban todos los campos marcados con (*) en la consola.
        if (huesped.getApellido() == null || huesped.getApellido().isEmpty() ||
                huesped.getNombre() == null || huesped.getNombre().isEmpty() ||
                huesped.getTipoDocumento() == null || huesped.getTipoDocumento().isEmpty() ||
                huesped.getDocumento() == null || huesped.getDocumento().isEmpty() ||
                huesped.getFechaNacimiento() == null ||
                huesped.getDireccion() == null || // Se verifica que el objeto Dirección exista
                huesped.getDireccion().getCalle() == null || huesped.getDireccion().getCalle().isEmpty() ||
                huesped.getDireccion().getNumero() <= 0 || // Los números deben ser positivos
                huesped.getDireccion().getCodigoPostal() <= 0 ||
                huesped.getDireccion().getLocalidad() == null || huesped.getDireccion().getLocalidad().isEmpty() ||
                huesped.getDireccion().getProvincia() == null || huesped.getDireccion().getProvincia().isEmpty() ||
                huesped.getDireccion().getPais() == null || huesped.getDireccion().getPais().isEmpty()) {

            throw new CamposObligatoriosException("Debe completar todos los campos obligatorios (*).");
        }

        // Se busca por el documento que ahora es un String
        if (huespedDAO.buscarHuesped(huesped.getTipoDocumento(), huesped.getDocumento()).isPresent()) {
            throw new DocumentoDuplicadoException("¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
        }

        huespedDAO.altaHuesped(huesped);
    }
public void registrarHuespedAceptandoDuplicado(Huesped huesped){
        huespedDAO.altaHuesped(huesped);
}
    /**
     * Orquesta la búsqueda de huéspedes según criterios.
     * @param apellido Criterio para el apellido.
     * @param nombre Criterio para el nombre.
     * @param tipoDocumento Criterio para el tipo del documento.
     * @param documento Criterio para el numero de documento.
     * @return Lista de huéspedes encontrados.
     */
    public List<Huesped> buscarHuespedes(String apellido, String nombre, String tipoDocumento, String documento) {
        return huespedDAO.buscarPorCriterios(apellido, nombre, tipoDocumento, documento);
    }
}