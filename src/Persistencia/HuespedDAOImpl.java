package Persistencia;

import Logica.Dominio.Direccion;
import Logica.Dominio.Huesped;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HuespedDAOImpl implements HuespedDAO {

    private final String RUTA_ARCHIVO = "huespedes.csv";

    @Override
    public void altaHuesped(Huesped huesped) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            pw.println(convertirHuespedEnCSV(huesped));
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    @Override
    public Optional<Huesped> buscarHuesped(String tipoDocumento, int documento) {
        // 1. Abre el archivo de forma segura
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            // 2. Lee cada línea del archivo hasta el final
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(","); // 3. Divide la línea en un array

                // 4. Comprueba si la línea tiene suficientes datos y si el tipo de documento coincide
                if (datos.length > 4 && datos[3].equalsIgnoreCase(tipoDocumento)) {
                    try {
                        // 5. Convierte el documento del archivo a número
                        int documentoEnArchivo = Integer.parseInt(datos[4]);

                        // 6. Si el número de documento también coincide, ¡lo encontramos!
                        if (documentoEnArchivo == documento) {
                            // 7. Convierte el array de datos a un objeto Huesped y lo devuelve
                            return Optional.of(convertirCSVAHuesped(datos));
                        }
                    } catch (NumberFormatException e) {
                        // Si el documento en esta línea no es un número válido,
                        // simplemente se ignora y el bucle continúa con la siguiente línea.
                    }
                }
            }
        } catch (IOException e) {
            // 8. Si hay un error al leer el archivo, se informa
            System.err.println("Error al leer el archivo de huéspedes: " + e.getMessage());
        }

        // 9. Si el bucle termina y no se encontró nada, devuelve un Optional vacío
        return Optional.empty();
    }

    @Override
    public void modificarHuesped(Huesped huesped) {
        // no hecho
        System.out.println("Método modificarHuesped no implementado.");
    }

    @Override
    public void bajaHuesped(int documento) {
        // no hecho
        System.out.println("Método bajaHuesped no implementado.");
    }

    @Override
    public List<Huesped> buscarPorCriterios(String apellido, String nombre) {
        try (Stream<String> lineas = Files.lines(Paths.get(RUTA_ARCHIVO))) {
            return lineas
                    .map(linea -> linea.split(","))
                    .map(this::convertirCSVAHuesped)
                    // --- FILTRO ADICIONAL ---
                    // Se asegura de que no se procesen objetos nulos.
                    .filter(java.util.Objects::nonNull)
                    .filter(huesped -> {
                        boolean coincideApellido = (apellido == null || apellido.isEmpty()) ||
                                huesped.getApellido().toLowerCase().startsWith(apellido.toLowerCase());
                        boolean coincideNombre = (nombre == null || nombre.isEmpty()) ||
                                huesped.getNombre().toLowerCase().startsWith(nombre.toLowerCase());
                        return coincideApellido && coincideNombre;
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de huéspedes: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    private String convertirHuespedEnCSV(Huesped huesped) {
        return huesped.getApellido() + "," +
                huesped.getNombre() + "," +
                huesped.getEmail() + "," +
                huesped.getTipoDocumento() + "," +
                huesped.getDocumento() + "," +
                huesped.getTelefono();
    }

    private Huesped convertirCSVAHuesped(String[] datos) {
        // --- VALIDACIÓN DE SEGURIDAD ---
        // Si el array no tiene la cantidad mínima de columnas, devuelve null.
        // Ajusta el '6' al número de columnas que esperas como mínimo.
        if (datos == null || datos.length < 6) {
            return null; // Ignora la línea malformada en lugar de fallar
        }

        try {
            Huesped huesped = new Huesped();
            huesped.setApellido(datos[0]);
            huesped.setNombre(datos[1]);
            huesped.setEmail(datos[2]);
            huesped.setTipoDocumento(datos[3]);
            huesped.setDocumento(Integer.parseInt(datos[4]));
            huesped.setTelefono(Integer.parseInt(datos[5]));
            return huesped;
        } catch (NumberFormatException e) {
            System.err.println("Advertencia: Se ignoró una línea con formato de número incorrecto.");
            return null;
        }
    }
}