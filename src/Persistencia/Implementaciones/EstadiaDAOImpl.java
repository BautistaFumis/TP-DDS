package Persistencia.Implementaciones;

import Logica.Dominio.Huesped;
import Persistencia.EstadiaDAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 * Implementación de {@link EstadiaDAO} que maneja la persistencia en un archivo CSV.
 *
 */
public class EstadiaDAOImpl implements EstadiaDAO {

    private static final String RUTA_ARCHIVO = "Estadia.csv";

    /**
     * Verifica si un huésped tiene estadías registradas, ya sea como huésped principal o como acompañante.
     *
     * @param huesped El huésped a verificar.
     * @return {@code true} si el huésped se encuentra en al menos una estadía, {@code false} de lo contrario.
     */
    @Override
    public boolean tieneEstadias(Huesped huesped) {
        if (huesped == null || huesped.getDocumento() == null || huesped.getDocumento().trim().isEmpty() ||
                huesped.getTipoDocumento() == null || huesped.getTipoDocumento().trim().isEmpty()) {
            System.err.println("Advertencia: Intento de verificar estadías para un huésped inválido (datos nulos o vacíos).");
            return false;
        }
        File archivoEstadias = new File(RUTA_ARCHIVO);
        if (!archivoEstadias.exists()) {
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] campos = linea.split(",", -1); // Usamos -1 para no descartar campos vacíos al final
                if (campos.length < 22) {
                    System.err.println("Advertencia: Se ignoró una línea con error en formato en estadias.csv");
                    continue;
                }
                String tipoDocPrincipal = campos[4];
                String numDocPrincipal = campos[5];
                if (numDocPrincipal.equals(huesped.getDocumento()) && tipoDocPrincipal.equalsIgnoreCase(huesped.getTipoDocumento())) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de estadías: " + e.getMessage());
            return true;
        }
        return false;
    }

}