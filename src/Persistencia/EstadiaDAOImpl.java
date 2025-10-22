package Persistencia;

import Logica.Dominio.Direccion;
import Logica.Dominio.Huesped;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementación de {@link EstadiaDAO} que maneja la persistencia en un archivo CSV.
 *
 */
public class EstadiaDAOImpl implements EstadiaDAO {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String RUTA_ARCHIVO = "Estadia.csv";

    /**
     * Verifica si un huésped tiene estadías registradas, ya sea como huésped principal o como acompañante.
     *
     * @param huesped El huésped a verificar.
     * @return {@code true} si el huésped se encuentra en al menos una estadía, {@code false} de lo contrario.
     */
    @Override
    public boolean tieneEstadias(Huesped huesped) {
        // 1. Validación de entrada (Programación Defensiva)
        if (huesped == null || huesped.getDocumento() == null || huesped.getDocumento().trim().isEmpty() ||
                huesped.getTipoDocumento() == null || huesped.getTipoDocumento().trim().isEmpty()) {
            System.err.println("Advertencia: Intento de verificar estadías para un huésped inválido (datos nulos o vacíos).");
            return false; // No se puede verificar si los datos del huésped son inválidos
        }

        File archivoEstadias = new File(RUTA_ARCHIVO);
        if (!archivoEstadias.exists()) {
            // Si no existe el archivo de estadías, nadie tiene estadías.
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue; // Saltar líneas vacías
                }
                String[] campos = linea.split(",", -1); // Usa -1 para no descartar campos vacíos al final

                // 2. Verificar longitud mínima esperada (hasta documento principal + acompañantes)
                // Ajusta el 22 si tu número final de columnas es diferente
                if (campos.length < 22) {
                    System.err.println("Advertencia: Se ignoró una línea corta o malformada en estadias.csv");
                    continue; // Saltar línea incorrecta
                }

                // 3. Extraer datos relevantes de la línea CSV
                // Los índices se basan en la estructura que definimos:
                // 0: FechaIn, 1: FechaOut, 2: Apellido, 3: Nombre, 4: TipoDoc, 5: NumDoc, ... 21: Acompanantes
                String tipoDocPrincipal = campos[4];
                String numDocPrincipal = campos[5];
                String docsAcompanantesStr = campos[21]; // Última columna esperada

                // 4. Comparar con el huésped principal
                if (numDocPrincipal.equals(huesped.getDocumento()) && tipoDocPrincipal.equalsIgnoreCase(huesped.getTipoDocumento())) {
                    return true; // ¡Encontrado como huésped principal! Termina la búsqueda.
                }

                // 5. Comparar con la lista de acompañantes (si existe)
                if (docsAcompanantesStr != null && !docsAcompanantesStr.isEmpty()) {
                    // Divide la cadena "doc1;doc2;doc3" en una lista
                    List<String> listaDocsAcompanantes = Arrays.asList(docsAcompanantesStr.split(";"));
                    // Verifica si el documento del huésped está en la lista de acompañantes
                    if (listaDocsAcompanantes.contains(huesped.getDocumento())) {
                        return true; // ¡Encontrado como acompañante! Termina la búsqueda.
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de estadías: " + e.getMessage());
            // En caso de error de lectura, es más seguro asumir que sí tiene estadías
            // para prevenir borrados accidentales.
            return true;
        }

        return false;
    }

}