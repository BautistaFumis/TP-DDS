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
        // 1. Validar datos obligatorios
        if (huesped.getApellido() == null || huesped.getApellido().isEmpty() ||
                huesped.getNombre() == null || huesped.getNombre().isEmpty() ||
                huesped.getDocumento() == 0) {
            throw new CamposObligatoriosException("Debe completar todos los campos obligatorios (*).");
        }

        // 2. Verificar si el documento ya existe
        if (huespedDAO.buscarHuesped(huesped.getTipoDocumento(), huesped.getDocumento()).isPresent()) {
            throw new DocumentoDuplicadoException("¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
        }

        // 3. Si no hay error, damos de alta el huesped en el csv
        huespedDAO.altaHuesped(huesped);
    }

    public void registrarHuespedAceptandoDuplicado(Huesped huesped) {
        huespedDAO.altaHuesped(huesped);
    }
    /**
     * Orquesta la búsqueda de huéspedes según criterios.
     * @param apellido Criterio para el apellido.
     * @param nombre Criterio para el nombre.
     * @return Lista de huéspedes encontrados.
     */
    public List<Huesped> buscarHuespedes(String apellido, String nombre) {
        return huespedDAO.buscarPorCriterios(apellido, nombre);
    }
}