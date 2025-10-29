package Persistencia.Implementaciones;

import Logica.Dominio.Direccion;
import Logica.Dominio.Huesped;
import Persistencia.HuespedDAO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación de {@link HuespedDAO} que maneja la persistencia de los datos de huéspedes en un archivo CSV.
 *
 */

public class HuespedDAOImpl implements HuespedDAO {
    private final String RUTA_ARCHIVO = "huespedes.csv";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Persiste un nuevo huésped en la fuente de datos.
     *
     * @param huesped El objeto {@link Huesped} que se va a agregar. Debe contener todos los datos necesarios.
     */

    @Override
    public void altaHuesped(Huesped huesped) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            pw.println(convertirHuespedEnCSV(huesped));
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    /**
     * Busca un único huésped por su tipo y número de documento.
     * El uso de {@link Optional} es para manejar de forma segura el caso en que
     * no se encuentre ningún huésped con los datos proporcionados, evitando NullPointerExceptions.
     *
     * @param tipoDocumento El tipo de documento del huésped a buscar (ej: "DNI").
     * @param documento El número de documento del huésped a buscar.
     * @return Un {@link Optional} que contiene el objeto {@link Huesped} si se encuentra,
     * o un Optional vacío si no hay coincidencias.
     */

    @Override
    public Optional<Huesped> buscarHuesped(String tipoDocumento, String documento) {
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",", -1);
                if (datos.length > 3 && datos[2].equalsIgnoreCase(tipoDocumento) && datos[3].equals(documento)) {
                    return Optional.ofNullable(convertirCSVAHuesped(datos));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de huéspedes: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Busca una lista de huéspedes que coincidan con múltiples criterios de búsqueda.
     * Si un criterio se proporciona como nulo o vacío, se ignora en el filtro.
     * La búsqueda por apellido y nombre es sensible a mayúsculas/minúsculas y busca por prefijo ("empieza con").
     *
     * @param apellido Criterio de búsqueda para el apellido.
     * @param nombre Criterio de búsqueda para el nombre.
     * @param tipoDocumento Criterio de búsqueda para el tipo de documento (comparación exacta, ignorando mayúsculas/minúsculas).
     * @param numeroDocumento Criterio de búsqueda para el número de documento (comparación exacta).
     * @return Una {@link List} de objetos {@link Huesped} que coinciden con los criterios.
     * Si no se encuentran coincidencias, devuelve una lista vacía.
     */

    @Override
    public List<Huesped> buscarPorCriterios(String apellido, String nombre, String tipoDocumento, String numeroDocumento) {
        List<Huesped> huespedesEncontrados = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] datos = linea.split(",", -1);
                Huesped huesped = convertirCSVAHuesped(datos);
                if (huesped != null) {
                    boolean coincideApellido = (apellido == null || apellido.isEmpty()) || huesped.getApellido().toLowerCase().startsWith(apellido.toLowerCase());
                    boolean coincideNombre = (nombre == null || nombre.isEmpty()) || huesped.getNombre().toLowerCase().startsWith(nombre.toLowerCase());
                    boolean coincideTipoDoc = (tipoDocumento == null || tipoDocumento.isEmpty()) || huesped.getTipoDocumento().equalsIgnoreCase(tipoDocumento);
                    boolean coincideNumeroDoc = (numeroDocumento == null || numeroDocumento.isEmpty()) || huesped.getDocumento().equals(numeroDocumento);
                    if (coincideApellido && coincideNombre && coincideTipoDoc && coincideNumeroDoc) {
                        huespedesEncontrados.add(huesped);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de huéspedes: " + e.getMessage());
        }
        return huespedesEncontrados;
    }

    /**
     * Actualiza la información de un huésped existente en la fuente de datos.
     * La implementación buscará al huésped basándose en su tipo y número de documento
     * y reemplazará sus datos con los del objeto proporcionado.
     *
     */

    @Override
    public void modificarHuesped(String tipoDocumentoOriginal, String documentoOriginal, Huesped huespedConNuevosDatos) {
        try {
            List<String> lineas = Files.readAllLines(Paths.get(RUTA_ARCHIVO));
            List<String> lineasActualizadas = lineas.stream()
                    .map(linea -> {
                        String[] datos = linea.split(",", -1);
                        // --- CORRECCIÓN: Compara con los identificadores ORIGINALES ---
                        if (datos.length > 3 &&
                                datos[2].equalsIgnoreCase(tipoDocumentoOriginal) && // Compara con TIPO original
                                datos[3].equals(documentoOriginal)) {              // Compara con NUMERO original
                            // Si encuentra la línea, la reemplaza con los NUEVOS datos formateados
                            return convertirHuespedEnCSV(huespedConNuevosDatos);
                        }
                        // Si no es la línea, la devuelve sin cambios
                        return linea;
                    })
                    .collect(Collectors.toList());
            // Sobrescribe el archivo completo con la lista actualizada
            Files.write(Paths.get(RUTA_ARCHIVO), lineasActualizadas, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error al modificar el archivo de huéspedes: " + e.getMessage());
        }
    }

    /**
     * Elimina un huésped de la fuente de datos utilizando su número de documento como identificador único.
     *
     * @param documento El número de documento del huésped que se desea eliminar.
     */

    @Override
    public void eliminarHuesped(String documento) {
        try {
            List<String> lineas = Files.readAllLines(Paths.get(RUTA_ARCHIVO));
            List<String> nuevasLineas = lineas.stream()
                    .filter(linea -> {
                        String[] datos = linea.split(",", -1);
                        return datos.length <= 3 || !datos[3].equals(documento);
                    })
                    .collect(Collectors.toList());
            Files.write(Paths.get(RUTA_ARCHIVO), nuevasLineas, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error al eliminar el huésped: " + e.getMessage());
        }
    }

    /**
     * Convierte un objeto {@link Huesped} a su representación en formato CSV.
     * Maneja los valores nulos para campos opcionales, representándolos como cadenas vacías.
     *
     * @param huesped El objeto Huesped a convertir.
     * @return Una cadena de texto formateada como una línea CSV.
     */

    private String convertirHuespedEnCSV(Huesped huesped) {
        Direccion dir = huesped.getDireccion();
        String cuitStr = huesped.getCuit() != null ? huesped.getCuit() : "";
        String deptoStr = dir.getDepartamento() != null ? dir.getDepartamento() : "";
        String pisoStr = (dir.getPiso() == null) ? "" : String.valueOf(dir.getPiso());
        String telefonoStr = huesped.getTelefono() != null ? huesped.getTelefono() : "";
        String emailStr = huesped.getEmail() != null ? huesped.getEmail() : "";
        String ocupacionStr = huesped.getOcupacion() != null ? huesped.getOcupacion() : "";
        String nacionalidadStr = huesped.getNacionalidad() != null ? huesped.getNacionalidad() : "";
        
        return String.join(",",
                huesped.getApellido(),
                huesped.getNombre(),
                huesped.getTipoDocumento(),
                huesped.getDocumento(),
                cuitStr,
                huesped.getCategoriaIVA(),
                huesped.getFechaNacimiento() != null ? huesped.getFechaNacimiento().format(FORMATO_FECHA) : "",
                dir.getCalle(),
                (dir.getNumero() == null) ? "" : String.valueOf(dir.getNumero()),
                deptoStr,
                pisoStr,
                (dir.getCodigoPostal() == null) ? "" : String.valueOf(dir.getCodigoPostal()),
                dir.getLocalidad(),
                dir.getProvincia(),
                dir.getPais(),
                telefonoStr,
                emailStr,
                ocupacionStr,
                nacionalidadStr
        );
    }

    /**
     * Convierte un array de strings (una línea CSV) en un objeto {@link Huesped}.
     * Es robusto frente a líneas malformadas o con datos incorrectos, retornando {@code null}
     * en esos casos y registrando una advertencia.
     *
     * @param datos El array de strings obtenido al dividir una línea del CSV.
     * @return Un objeto {@link Huesped} si la conversión es exitosa, o {@code null} si falla.
     */


    private Huesped convertirCSVAHuesped(String[] datos) {
        if (datos == null || datos.length < 19) {
            System.err.println("Advertencia: Se ignoró una línea CSV con longitud incorrecta: " + (datos != null ? datos.length : "null"));
            return null;
        }
        try {
            return getHuesped(datos);
        } catch (DateTimeParseException e) {
            System.err.println("Advertencia: Se ignoró una línea con formato de fecha incorrecto en huespedes.csv.");
            return null;
        } catch (NumberFormatException e) {
            System.err.println("Advertencia: Se ignoró una línea con formato de número incorrecto (Numero, Piso o CP) en huespedes.csv.");
            return null;
        } catch (Exception e) {
            System.err.println("Advertencia: Se ignoró una línea con formato incorrecto general en huespedes.csv. Error: " + e.getMessage());
            return null;
        }
    }

    private Huesped getHuesped(String[] datos) {
        Huesped huesped = new Huesped();
        Direccion direccion = new Direccion();
        huesped.setApellido(datos[0]);
        huesped.setNombre(datos[1]);
        huesped.setTipoDocumento(datos[2]);
        huesped.setDocumento(datos[3]);
        huesped.setCuit(datos[4]);
        huesped.setCategoriaIVA(datos[5]);
        if (datos[6] != null && !datos[6].isEmpty()) {
            huesped.setFechaNacimiento(LocalDate.parse(datos[6], FORMATO_FECHA));
        } else {
            huesped.setFechaNacimiento(null);
        }
        direccion.setCalle(datos[7]);
        if (datos[8] != null && !datos[8].isEmpty()) direccion.setNumero(Integer.parseInt(datos[8]));
        direccion.setDepartamento(datos[9]);
        if (datos[10] != null && !datos[10].isEmpty()) direccion.setPiso(Integer.parseInt(datos[10]));
        direccion.setCodigoPostal(datos[11]);
        direccion.setLocalidad(datos[12]);
        direccion.setProvincia(datos[13]);
        direccion.setPais(datos[14]);
        huesped.setDireccion(direccion);
        huesped.setTelefono(datos[15]);
        huesped.setEmail(datos[16]);
        huesped.setOcupacion(datos[17]);
        huesped.setNacionalidad(datos[18]);
        return huesped;
    }
}