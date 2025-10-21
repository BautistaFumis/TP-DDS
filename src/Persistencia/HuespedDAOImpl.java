package Persistencia;

import Logica.Dominio.Direccion;
import Logica.Dominio.Huesped;
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

    @Override
    public void modificarHuesped(Huesped huespedModificado) {
        try {
            List<String> lineas = Files.readAllLines(Paths.get(RUTA_ARCHIVO));
            List<String> lineasActualizadas = lineas.stream()
                    .map(linea -> {
                        String[] datos = linea.split(",", -1);
                        if (datos.length > 3 && datos[2].equals(huespedModificado.getTipoDocumento()) && datos[3].equals(huespedModificado.getDocumento())) {
                            return convertirHuespedEnCSV(huespedModificado);
                        }
                        return linea;
                    })
                    .collect(Collectors.toList());
            Files.write(Paths.get(RUTA_ARCHIVO), lineasActualizadas, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error al modificar el archivo de huéspedes: " + e.getMessage());
        }
    }

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

    private Huesped convertirCSVAHuesped(String[] datos) {
        // Verifica el número correcto de campos esperado
        if (datos == null || datos.length < 19) {
            System.err.println("Advertencia: Se ignoró una línea CSV con longitud incorrecta: " + (datos != null ? datos.length : "null"));
            return null;
        }
        try {
            Huesped huesped = new Huesped();
            Direccion direccion = new Direccion();

            huesped.setApellido(datos[0]);
            huesped.setNombre(datos[1]);
            huesped.setTipoDocumento(datos[2]);
            huesped.setDocumento(datos[3]);
            huesped.setCuit(datos[4]);
            huesped.setCategoriaIVA(datos[5]);
            // Maneja fecha vacía
            if (datos[6] != null && !datos[6].isEmpty()) {
                huesped.setFechaNacimiento(LocalDate.parse(datos[6], FORMATO_FECHA));
            } else {
                huesped.setFechaNacimiento(null);
            }

            direccion.setCalle(datos[7]);
            // Convierte a Integer, manejando vacío
            if (datos[8] != null && !datos[8].isEmpty()) direccion.setNumero(Integer.parseInt(datos[8]));
            direccion.setDepartamento(datos[9]);
            // Convierte a Integer, manejando vacío
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
}