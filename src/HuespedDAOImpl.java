import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class HuespedDAOImpl implements HuespedDAO {

    private final String RUTA_ARCHIVO = "C://Users//bauti//OneDrive//Documentos//UTN//3//Desarrollo//TP//huespedes.csv/";

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
        try (Stream<String> lineas = Files.lines(Paths.get(RUTA_ARCHIVO))) {
            return lineas
                    .map(linea -> linea.split(","))
                    .filter(datos -> datos.length > 4 && datos[3].equalsIgnoreCase(tipoDocumento) && Integer.parseInt(datos[4]) == documento)
                    .findFirst()
                    .map(this::convertirCSVAHuesped);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al leer o parsear el archivo: " + e.getMessage());
            return Optional.empty();
        }
        // medio inchequeable este metodo para buscar
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

    private String convertirHuespedEnCSV(Huesped huesped) {
        return huesped.getApellido() + "," +
                huesped.getNombre() + "," +
                huesped.getEmail() + "," +
                huesped.getTipoDocumento() + "," +
                huesped.getDocumento() + "," +
                huesped.getTelefono();
    }

    private Huesped convertirCSVAHuesped(String[] datos) {
        String apellido = datos[0];
        String nombre = datos[1];
        String email = datos[2];
        String tipoDoc = datos[3];
        int nroDoc = Integer.parseInt(datos[4]);
        int telefono = Integer.parseInt(datos[5]);

        Direccion direccion = new Direccion(); // Ahora funciona gracias al constructor vacío

        return new Huesped(nombre, apellido, email, tipoDoc, telefono, nroDoc, direccion);
    }
}