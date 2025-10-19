package Persistencia;

import Logica.Dominio.Direccion;
import Logica.Dominio.Huesped;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    private Huesped convertirCSVAHuesped(String[] datos) {
        if (datos == null || datos.length < 19) {
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
            huesped.setFechaNacimiento(LocalDate.parse(datos[6], FORMATO_FECHA));
            direccion.setCalle(datos[7]);
            if(datos[8] != null && !datos[8].isEmpty()) direccion.setNumero(Integer.parseInt(datos[8]));
            direccion.setDepartamento(datos[9]);
            if(datos[10] != null && !datos[10].isEmpty()) direccion.setPiso(datos[10]);
            if(datos[11] != null && !datos[11].isEmpty()) direccion.setCodigoPostal(datos[11]);
            direccion.setLocalidad(datos[12]);
            direccion.setProvincia(datos[13]);
            direccion.setPais(datos[14]);
            huesped.setDireccion(direccion);
            huesped.setTelefono(datos[15]);
            huesped.setEmail(datos[16]);
            huesped.setOcupacion(datos[17]);
            huesped.setNacionalidad(datos[18]);
            return huesped;
        } catch (Exception e) {
            System.err.println("Advertencia: Se ignoró una línea con formato incorrecto en huespedes.csv.");
            return null;
        }
    }
}