package Persistencia;

import Logica.Dominio.Direccion;
import Logica.Dominio.Huesped;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HuespedDAOImpl implements HuespedDAO {

    private final String RUTA_ARCHIVO = "huespedes.csv";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void altaHuesped(Huesped huesped) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            pw.println(convertirHuespedEnCSV(huesped));
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    @Override
    public Optional<Huesped> buscarHuesped(String tipoDocumento, String documento) {
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
                        String documentoEnArchivo = (datos[4]);

                        // 6. Si el número de documento también coincide, ¡lo encontramos!
                        if (documentoEnArchivo.equals(documento)) {
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
    public List<Huesped> buscarPorCriterios(String apellido, String nombre, String tipoDocumento, String numeroDocumento) {
        // 1. Crea una lista vacía para guardar los resultados.
        List<Huesped> huespedesEncontrados = new ArrayList<>();

        // 2. Lee el archivo de forma segura.
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;

            // 3. Lee el archivo línea por línea hasta que se acabe.
            while ((linea = reader.readLine()) != null) {

                // Si la línea está vacía, la salta y continúa con la siguiente.
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(",");
                Huesped huesped = convertirCSVAHuesped(datos); // Reutilizamos tu conversor

                // 4. Si la línea se pudo convertir en un huésped válido...
                if (huesped != null) {

                    // 5. Comprueba cada criterio de búsqueda.
                    // Si el usuario no ingresó un criterio (está vacío), se considera una coincidencia.

                    boolean coincideApellido = (apellido == null || apellido.isEmpty()) ||
                            huesped.getApellido().toLowerCase().startsWith(apellido.toLowerCase());

                    boolean coincideNombre = (nombre == null || nombre.isEmpty()) ||
                            huesped.getNombre().toLowerCase().startsWith(nombre.toLowerCase());

                    boolean coincideTipoDoc = (tipoDocumento == null || tipoDocumento.isEmpty()) ||
                            huesped.getTipoDocumento().equalsIgnoreCase(tipoDocumento);

                    // Para el número, solo comparamos si el usuario ingresó algo.
                    boolean coincideNumeroDoc = (numeroDocumento == null || numeroDocumento.isEmpty()) ||
                            String.valueOf(huesped.getDocumento()).equals(numeroDocumento);

                    // 6. Si TODOS los criterios coinciden, añade el huésped a la lista.
                    if (coincideApellido && coincideNombre && coincideTipoDoc && coincideNumeroDoc) {
                        huespedesEncontrados.add(huesped);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de huéspedes: " + e.getMessage());
        }

        // 7. Devuelve la lista de huéspedes que coincidieron.
        return huespedesEncontrados;
    }


    /**
     * Convierte un objeto Huesped completo a una línea de texto CSV.
     * Actualizado para incluir todos los 19 campos.
     */
    private String convertirHuespedEnCSV(Huesped huesped) {
        // Usamos String.join para manejar las comas de forma más limpia
        return String.join(",",
                huesped.getApellido(),
                huesped.getNombre(),
                huesped.getTipoDocumento(),
                huesped.getDocumento(),
                huesped.getCuit(),
                huesped.getCategoriaIVA(),
                huesped.getFechaNacimiento().format(FORMATO_FECHA), // Formatea la fecha a texto
                huesped.getDireccion().getCalle(),
                String.valueOf(huesped.getDireccion().getNumero()),
                huesped.getDireccion().getDepartamento(),
                String.valueOf(huesped.getDireccion().getPiso()),
                String.valueOf(huesped.getDireccion().getCodigoPostal()),
                huesped.getDireccion().getLocalidad(),
                huesped.getDireccion().getProvincia(),
                huesped.getDireccion().getPais(),
                String.valueOf(huesped.getTelefono()),
                huesped.getEmail(),
                huesped.getOcupacion(),
                huesped.getNacionalidad()
        );
    }

    /**
     * Convierte una línea de texto CSV (un array de datos) a un objeto Huesped.
     * Actualizado para leer todos los campos y construir el objeto Direccion.
     */
    private Huesped convertirCSVAHuesped(String[] datos) {
        // Se actualiza la validación al nuevo número de columnas
        if (datos == null || datos.length < 19) {
            return null; // Ignora la línea malformada
        }
        try {
            Huesped huesped = new Huesped();
            Direccion direccion = new Direccion();

            // Asigna los datos del huésped
            huesped.setApellido(datos[0]);
            huesped.setNombre(datos[1]);
            huesped.setTipoDocumento(datos[2]);
            huesped.setDocumento(datos[3]);
            huesped.setCuit(datos[4]);
            huesped.setCategoriaIVA(datos[5]);
            huesped.setFechaNacimiento(LocalDate.parse(datos[6], FORMATO_FECHA)); // Convierte el texto a fecha

            // Asigna los datos de la dirección
            direccion.setCalle(datos[7]);
            direccion.setNumero(Integer.parseInt(datos[8]));
            direccion.setDepartamento(datos[9]);
            direccion.setPiso(datos[10]);
            direccion.setCodigoPostal(Integer.parseInt(datos[11]));
            direccion.setLocalidad(datos[12]);
            direccion.setProvincia(datos[13]);
            direccion.setPais(datos[14]);

            // Asigna el objeto Dirección al Huésped
            huesped.setDireccion(direccion);

            // Continúa con los datos restantes
            huesped.setTelefono(datos[15]);
            huesped.setEmail(datos[16]);
            huesped.setOcupacion(datos[17]);
            huesped.setNacionalidad(datos[18]);

            return huesped;
        } catch (Exception e) { // Captura cualquier error de formato (número, fecha)
            System.err.println("Advertencia: Se ignoró una línea con formato incorrecto.");
            return null;
        }
    }

}