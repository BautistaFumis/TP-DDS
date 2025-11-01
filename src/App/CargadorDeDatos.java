package App;

import Logica.Dominio.Entidades.Direccion;
import Logica.Dominio.Entidades.Huesped;
import Logica.Dominio.Entidades.Usuario;
import Persistencia.Repositorios.HuespedRepository;
import Persistencia.Repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Esta clase se ejecuta automáticamente al iniciar Spring Boot.
 * Carga datos iniciales (de usuarios y huéspedes) si las tablas están vacías.
 */
@Component
public class CargadorDeDatos implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HuespedRepository huespedRepository;

    // Definimos el formato de fecha que usa tu CSV
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void run(String... args) throws Exception {

        // --- Bloque de Carga de Usuarios ---
        if (usuarioRepository.count() == 0) {
            System.out.println(">>> Base de datos de usuarios vacía. Cargando usuario por defecto...");
            Usuario conserje = new Usuario("Conserje", "conserje123");
            usuarioRepository.save(conserje);
            System.out.println(">>> Usuario 'Conserje' cargado exitosamente.");
        } else {
            System.out.println(">>> La base de datos de usuarios ya tiene datos.");
        }

        // --- NUEVO: Bloque de Carga de Huéspedes ---
        if (huespedRepository.count() == 0) {
            System.out.println(">>> Base de datos de huéspedes vacía. Cargando 10 huéspedes iniciales...");

            // Creamos los huéspedes uno por uno llamando a un metodo ayudante
            Huesped h1 = crearHuesped("Fumis", "Bautista", "DNI", "45828019", "20-45828019-4", "Consumidor Final", "28/05/2004", "Saavedra", "2444", "", "9", "3000", "Santa Fe", "Santa Fe", "Argentina", "3425112233", "bautistafumis@gmail.com", "Estudiante", "Argentina");
            Huesped h2 = crearHuesped("Fernandez", "Juan", "LE", "8123456", "20-8123456-9", "Monotributista", "11/12/1950", "Bv. Pellegrini", "3201", "", "2", "3000", "Santa Fe", "Santa Fe", "Argentina", "3424111222", "juan.fernandez@example.com", "Jubilado", "Argentina");
            Huesped h3 = crearHuesped("Garcia", "Sofia", "DNI", "41987654", "27-41987654-5", "Consumidor Final", "19/09/1999", "Alvear", "1530", "", "", "2000", "Rosario", "Santa Fe", "Argentina", "3415876543", "sofia.garcia@example.com", "Diseñadora", "Argentina");
            Huesped h4 = crearHuesped("Lopez", "Martin", "DNI", "35123456", "", "Consumidor Final", "18/02/1990", "Urquiza", "1850", "", "", "3000", "Santa Fe", "Santa Fe", "Argentina", "3425443322", "martin.lopez@example.com", "Programador", "Argentina");
            Huesped h5 = crearHuesped("Gomez", "Ana", "PASAPORTE", "DEF45678", "", "Monotributista", "25/06/1992", "Rivadavia", "2899", "", "", "3100", "Parana", "Entre Rios", "Argentina", "3434556677", "ana.gomez@example.com", "Abogada", "Argentina");
            Huesped h6 = crearHuesped("Perez", "Juana", "DNI", "38765432", "", "Consumidor Final", "30/11/1995", "J. J. Paso", "4500", "", "", "3000", "Santa Fe", "Santa Fe", "Argentina", "3425998877", "juana.perez@example.com", "Medica", "Argentina");
            Huesped h7 = crearHuesped("Locatelli", "Cristian", "DNI", "45828019", "20-44292971-9", "Consumidor Final", "07/11/2002", "Salta", "2200", "A", "1", "3000", "Santa Fe", "Santa Fe", "Argentina", "3482257103", "criatanlocatelli02@gmail.com", "Estudiante", "Argentina");
            Huesped h8 = crearHuesped("Cavani", "Edison", "LE", "912", "", "Consumidor Final", "10/10/2010", "LA BOCA", "8990", "", "", "3000", "BSAS", "Buenos Aires", "Argentina", "", "", "Futbolista", "Uruguayo");
            Huesped h9 = crearHuesped("Messi", "Leo", "DNI", "124", "", "Consumidor Final", "10/10/2010", "Barcelona", "30", "", "10", "2000", "Catalunia", "Espain", "Espain", "", "", "GOAT", "Otro Planeta");
            Huesped h10 = crearHuesped("Martinez", "Matias", "LE", "32144231", "20-32144231-9", "Consumidor Final", "22/05/2003", "Jujuy", "516", "", "", "3100", "Parana", "Entre Rios", "Arg", "", "", "", "");

            // Creamos una lista con todos los huéspedes
            List<Huesped> huespedesACargar = Arrays.asList(h1, h2, h3, h4, h5, h6, h7, h8, h9, h10);

            // Usamos .saveAll() para guardar la lista completa en una sola operación
            huespedRepository.saveAll(huespedesACargar);

            System.out.println(">>> " + huespedesACargar.size() + " huéspedes cargados exitosamente.");
        } else {
            System.out.println(">>> La base de datos de huéspedes ya tiene datos.");
        }
    }


    /**
     * Crea un objeto Huesped completo a partir de strings,
     * manejando los campos vacíos.
     */
    private Huesped crearHuesped(String apellido, String nombre, String tipoDoc, String doc, String cuit, String iva, String fechaNac,
                                 String calle, String numero, String depto, String piso, String cp, String localidad, String prov, String pais,
                                 String tel, String email, String ocup, String nac) {

        // 1. Crear la Dirección
        Direccion dir = new Direccion();
        dir.setCalle(calle);
        dir.setNumero(parseInteger(numero));
        dir.setDepartamento(parseString(depto));
        dir.setPiso(parseInteger(piso));
        dir.setCodigoPostal(cp);
        dir.setLocalidad(localidad);
        dir.setProvincia(prov);
        dir.setPais(pais);

        // 2. Crear el Huésped
        Huesped h = new Huesped();
        h.setApellido(apellido);
        h.setNombre(nombre);
        h.setTipoDocumento(tipoDoc);
        h.setDocumento(doc);
        h.setCuit(parseString(cuit));
        h.setCategoriaIVA(iva);
        h.setFechaNacimiento(parseLocalDate(fechaNac));
        h.setTelefono(parseString(tel));
        h.setEmail(parseString(email));
        h.setOcupacion(parseString(ocup));
        h.setNacionalidad(parseString(nac));
        h.setDireccion(dir); // Asignamos la dirección al huésped

        return h;
    }

    /**
     * Convierte un String a Integer. Si el string es vacío o nulo, devuelve null.
     */
    private Integer parseInteger(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return Integer.parseInt(valor.trim());
    }

    /**
     * Convierte un String a String. Si el string es vacío o nulo, devuelve null.
     */
    private String parseString(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return valor.trim();
    }

    /**
     * Convierte un String (formato dd/MM/yyyy) a LocalDate.
     * Si el string es vacío o nulo, devuelve null.
     */
    private LocalDate parseLocalDate(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(valor.trim(), formatter);
    }
}