package Gestores;

import Clases.Huesped;
import Excepciones.CamposObligatoriosException;
import Excepciones.DocumentoDuplicadoException;
import Persistencia.HuespedDAO;

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
        // En este flujo no validamos la existencia, solo guardamos.
        huespedDAO.altaHuesped(huesped);
    }
}