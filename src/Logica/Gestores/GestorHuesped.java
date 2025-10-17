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
        // --- VALIDACIÓN COMPLETA DE CAMPOS OBLIGATORIOS ---
        if(huesped.getCategoriaIVA() == null  || huesped.getCategoriaIVA().trim().isEmpty()) huesped.setCategoriaIVA("Consumidor Final");

        if (huesped.getApellido() == null || huesped.getApellido().trim().isEmpty() ||
                huesped.getNombre() == null || huesped.getNombre().trim().isEmpty() ||
                huesped.getTipoDocumento() == null || huesped.getTipoDocumento().trim().isEmpty() ||
                huesped.getDocumento() == null || huesped.getDocumento().trim().isEmpty() ||
                huesped.getFechaNacimiento() == null ||
                huesped.getDireccion() == null ||
                huesped.getDireccion().getCalle() == null || huesped.getDireccion().getCalle().trim().isEmpty() ||
                huesped.getDireccion().getNumero() <= 0 || // Un número de calle no puede ser 0 o negativo
                huesped.getDireccion().getCodigoPostal() <= 0 || // El código postal tampoco
                huesped.getDireccion().getLocalidad() == null || huesped.getDireccion().getLocalidad().trim().isEmpty() ||
                huesped.getDireccion().getProvincia() == null || huesped.getDireccion().getProvincia().trim().isEmpty() ||
                huesped.getDireccion().getPais() == null || huesped.getDireccion().getPais().trim().isEmpty()) {

            // Si falta algún dato, se lanza la excepción con un mensaje claro.
            throw new CamposObligatoriosException("Debe completar todos los campos obligatorios (*).");
        }

        // Verifica si el documento ya existe
        if (huespedDAO.buscarHuesped(huesped.getTipoDocumento(), huesped.getDocumento()).isPresent()) {
            throw new DocumentoDuplicadoException("¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
        }

        // Si todas las validaciones pasan, se procede a dar el alta.
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

    public void modificarHuesped(Huesped huesped) throws CamposObligatoriosException, DocumentoDuplicadoException {

        if (huesped.getApellido() == null || huesped.getApellido().trim().isEmpty() ||
                huesped.getNombre() == null || huesped.getNombre().trim().isEmpty() ||
                huesped.getTipoDocumento() == null || huesped.getTipoDocumento().trim().isEmpty() ||
                huesped.getDocumento() == null || huesped.getDocumento().trim().isEmpty() ||
                huesped.getFechaNacimiento() == null ||
                huesped.getDireccion() == null ||
                huesped.getDireccion().getCalle() == null || huesped.getDireccion().getCalle().trim().isEmpty() ||
                huesped.getDireccion().getNumero() <= 0 || // Un número de calle no puede ser 0 o negativo
                huesped.getDireccion().getCodigoPostal() <= 0 || // El código postal tampoco
                huesped.getDireccion().getLocalidad() == null || huesped.getDireccion().getLocalidad().trim().isEmpty() ||
                huesped.getDireccion().getProvincia() == null || huesped.getDireccion().getProvincia().trim().isEmpty() ||
                huesped.getDireccion().getPais() == null || huesped.getDireccion().getPais().trim().isEmpty()) {

            // Si falta algún dato, se lanza la excepción con un mensaje claro.
            throw new CamposObligatoriosException("Debe completar todos los campos obligatorios (*).");
        }

        if(huesped.getCategoriaIVA() == null  || huesped.getCategoriaIVA().trim().isEmpty()) huesped.setCategoriaIVA("Consumidor Final");

        huespedDAO.modificarHuesped(huesped);
    }
}
