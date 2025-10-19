package Persistencia;

import Logica.Dominio.Huesped;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Implementación de {@link EstadiaDAO} que maneja la persistencia en un archivo CSV.
 *
 */
public class EstadiaDAOImpl implements EstadiaDAO {

    private static final String RUTA_ARCHIVO = "Estadias.csv";

    /**
     * Verifica si un huésped tiene estadías registradas, ya sea como huésped principal o como acompañante.
     *
     * @param huesped El huésped a verificar.
     * @return {@code true} si el huésped se encuentra en al menos una estadía, {@code false} de lo contrario.
     */
    @Override
    public boolean tieneEstadias(Huesped huesped) {
        File archivoEstadias = new File(RUTA_ARCHIVO);
        if (!archivoEstadias.exists()) {
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            return br.lines()
                    .anyMatch(linea -> {
                        String[] campos = linea.split(",", -1);
                        if (campos.length < 20) return false;

                        // 1. Verifica si es el huésped principal
                        String tipoDocPrincipal = campos[4];
                        String numDocPrincipal = campos[5];
                        if (numDocPrincipal.equals(huesped.getDocumento()) && tipoDocPrincipal.equals(huesped.getTipoDocumento())) {
                            return true;
                        }

                        // 2. Verifica si está en la lista de acompañantes (última columna)
                        String docsAcompanantesStr = campos[campos.length - 1];
                        if (!docsAcompanantesStr.isEmpty()) {
                            List<String> listaDocsAcompanantes = Arrays.asList(docsAcompanantesStr.split(";"));
                            return listaDocsAcompanantes.contains(huesped.getDocumento());
                        }

                        return false;
                    });
        } catch (IOException e) {
            System.err.println("Error al verificar estadías: " + e.getMessage());
            return true; // En caso de error, es más seguro asumir que sí tiene estadías para no borrar por accidente.
        }
    }
}