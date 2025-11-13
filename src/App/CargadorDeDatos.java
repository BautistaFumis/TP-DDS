package App;

import Logica.Dominio.Entidades.Direccion;
import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Entidades.Huesped;
import Logica.Dominio.Entidades.Usuario;
import Persistencia.Repositorios.EstadiaDAO;
import Persistencia.Repositorios.HuespedDAO;
import Persistencia.Repositorios.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Order(1) // le decimos a Spring que esto es lo primero
public class CargadorDeDatos implements CommandLineRunner {

    @Autowired
    private UsuarioDAO usuarioRepository;
    @Autowired
    private HuespedDAO huespedRepository;
    @Autowired
    private EstadiaDAO estadiaRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    @Transactional // <-- 4. ASEGURAR QUE TODO SEA UNA SOLA TRANSACCIÓN
    public void run(String... args) throws Exception {

        if (usuarioRepository.count() == 0) {
            System.out.println(">>> Base de datos de usuarios vacía. Cargando usuario por defecto...");
            usuarioRepository.save(new Usuario("Conserje", "conserje123"));
            System.out.println(">>> Usuario 'Conserje' cargado exitosamente.");
        } else {
            System.out.println(">>> La base de datos de usuarios ya tiene datos.");
        }

        if (huespedRepository.count() == 0) {
            System.out.println(">>> Base de datos de huéspedes vacía. Cargando 10 huéspedes iniciales (EN MAYÚSCULAS)...");

            // --- DATOS EN MAYÚSCULAS ---
            List<Huesped> huespedesACargar = Arrays.asList(
                    crearHuesped("FUMIS", "BAUTISTA", "DNI", "45828019", "20-45828019-4", "CONSUMIDOR FINAL", "28/05/2004", "SAAVEDRA", "2444", "", "9", "3000", "SANTA FE", "SANTA FE", "ARGENTINA", "3425112233", "bautistafumis@gmail.com", "ESTUDIANTE", "ARGENTINA"),
                    crearHuesped("FERNANDEZ", "JUAN", "LE", "8123456", "20-8123456-9", "MONOTRIBUTISTA", "11/12/1950", "BV. PELLEGRINI", "3201", "", "2", "3000", "SANTA FE", "SANTA FE", "ARGENTINA", "3424111222", "juan.fernandez@example.com", "JUBILADO", "ARGENTINA"),
                    crearHuesped("GARCIA", "SOFIA", "DNI", "41987654", "27-41987654-5", "CONSUMIDOR FINAL", "19/09/1999", "ALVEAR", "1530", "", "", "2000", "ROSARIO", "SANTA FE", "ARGENTINA", "3415876543", "sofia.garcia@example.com", "DISEÑADORA", "ARGENTINA"),
                    crearHuesped("LOPEZ", "MARTIN", "DNI", "35123456", "", "CONSUMIDOR FINAL", "18/02/1990", "URQUIZA", "1850", "", "", "3000", "SANTA FE", "SANTA FE", "ARGENTINA", "3425443322", "martin.lopez@example.com", "PROGRAMADOR", "ARGENTINA"),
                    crearHuesped("GOMEZ", "ANA", "PASAPORTE", "DEF45678", "", "MONOTRIBUTISTA", "25/06/1992", "RIVADAVIA", "2899", "", "", "3100", "PARANA", "ENTRE RIOS", "ARGENTINA", "3434556677", "ana.gomez@example.com", "ABOGADA", "ARGENTINA"),
                    crearHuesped("PEREZ", "JUANA", "DNI", "38765432", "", "CONSUMIDOR FINAL", "30/11/1995", "J. J. PASO", "4500", "", "", "3000", "SANTA FE", "SANTA FE", "ARGENTINA", "3425998877", "juana.perez@example.com", "MEDICA", "ARGENTINA"),
                    crearHuesped("LOCATELLI", "CRISTIAN", "DNI", "44292971", "20-44292971-9", "CONSUMIDOR FINAL", "07/11/2002", "SALTA", "2200", "A", "1", "3000", "SANTA FE", "SANTA FE", "ARGENTINA", "3482257103", "criatanlocatelli02@gmail.com", "ESTUDIANTE", "ARGENTINA"),
                    crearHuesped("CAVANI", "EDISON", "LE", "912", "", "CONSUMIDOR FINAL", "10/10/2010", "LA BOCA", "8990", "", "", "3000", "BSAS", "BUENOS AIRES", "ARGENTINA", "", "", "FUTBOLISTA", "URUGUAYO"),
                    crearHuesped("MESSI", "LEO", "DNI", "124", "", "CONSUMIDOR FINAL", "10/10/2010", "BARCELONA", "30", "", "10", "2000", "CATALUNIA", "ESPAIN", "ESPAIN", "", "", "GOAT", "OTRO PLANETA"),
                    crearHuesped("MARTINEZ", "MATIAS", "LE", "32144231", "20-32144231-9", "CONSUMIDOR FINAL", "22/05/2003", "JUJUY", "516", "", "", "3100", "PARANA", "ENTRE RIOS", "ARG", "", "", "", ""),
                    crearHuesped("GOMEZ", "CARLOS", "PASAPORTE", "ABC98765", "20-28765432-1", "RESPONSABLE INSCRIPTO", "22/07/1980", "AV. CORRIENTES", "950", "A", "5", "1043", "BUENOS AIRES", "CABA", "ARGENTINA", "1155667788", "carlos.martinez@work.com", "ARQUITECTO", "URUGUAYA")
            );

            huespedRepository.saveAllAndFlush(huespedesACargar);
            System.out.println(">>> " + huespedesACargar.size() + " huéspedes cargados exitosamente.");
        } else {
            System.out.println(">>> La base de datos de huéspedes ya tiene datos.");
        }

        if (estadiaRepository.count() == 0) {
            System.out.println(">>> Base de datos de estadias vacía. Cargando 5 estadias iniciales...");

            try {
                System.out.println(">>> [DEBUG] Creando lista de estadías...");
                List<Estadia> estadiasACargar = Arrays.asList(
                        crearEstadia("15/10/2025", "20/10/2025", "DNI", "45828019"), // Fumis
                        crearEstadia("01/11/2025", "10/11/2025", "PASAPORTE", "ABC98765"), // Gomez, Carlos
                        crearEstadia("12/11/2025", "15/11/2025", "LE", "8123456"), // Fernandez, Juan
                        crearEstadia("20/11/2025", "22/11/2025", "DNI", "41987654"), // Garcia, Sofia
                        crearEstadia("01/12/2025", "05/12/2025", "PASAPORTE", "DEF45678") // Gomez, Ana
                );

                System.out.println(">>> [DEBUG] Guardando estadías en la BD...");
                estadiaRepository.saveAll(estadiasACargar);
                System.out.println(">>> " + estadiasACargar.size() + " estadías cargadas exitosamente.");

            } catch (Exception e) {
                System.err.println("ERROR al cargar estadias iniciales: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println(">>> La base de datos de estadias ya tiene datos.");
        }
    }

    /**
     * ==========================================================
     * --- SECCIÓN CORREGIDA ---
     * ==========================================================
     */
    private Huesped crearHuesped(String apellido, String nombre, String tipoDoc, String doc, String cuit, String iva, String fechaNac,
                                 String calle, String numero, String depto, String piso, String cp, String localidad, String prov, String pais,
                                 String tel, String email, String ocup, String nac) {

        Direccion dir = new Direccion();

        // --- Campos de texto en Mayúsculas ---
        dir.setCalle(parseStringMayus(calle));
        dir.setDepartamento(parseStringMayus(depto));
        dir.setCodigoPostal(parseStringMayus(cp));
        dir.setLocalidad(parseStringMayus(localidad));
        dir.setProvincia(parseStringMayus(prov));
        dir.setPais(parseStringMayus(pais));

        // --- CORRECCIÓN: Usamos parseInteger para los campos numéricos ---
        // Esto es seguro y maneja valores vacíos ("") devolviendo null.
        dir.setNumero(parseInteger(numero));
        dir.setPiso(parseInteger(piso));

        Huesped h = new Huesped();
        // --- Campos de texto en Mayúsculas ---
        h.setApellido(parseStringMayus(apellido));
        h.setNombre(parseStringMayus(nombre));
        h.setTipoDocumento(parseStringMayus(tipoDoc));
        h.setDocumento(parseStringMayus(doc));
        h.setCuit(parseStringMayus(cuit));
        h.setCategoriaIVA(parseStringMayus(iva));
        h.setTelefono(parseStringMayus(tel));
        h.setOcupacion(parseStringMayus(ocup));
        h.setNacionalidad(parseStringMayus(nac));

        // --- Campos especiales (sin mayúsculas o con parseo diferente) ---
        h.setFechaNacimiento(parseLocalDate(fechaNac));
        h.setEmail(parseString(email)); // <-- EMAIL SE QUEDA CON parseString (sin mayúsculas)
        h.setDireccion(dir);

        return h;
    }

    private Estadia crearEstadia(String checkin, String checkout, String tipoDoc, String numDoc) {
        System.out.println(">>> [DEBUG] Buscando huésped: " + tipoDoc + " " + numDoc);

        // --- CAMBIO: Nos aseguramos de buscar en mayúsculas (como se guardaron) ---
        Optional<Huesped> huespedOpt = huespedRepository.findByTipoDocumentoAndDocumento(
                parseStringMayus(tipoDoc),
                parseStringMayus(numDoc)
        );

        if (huespedOpt.isEmpty()) {
            System.err.println(">>> [DEBUG] ¡FALLÓ! No se encontró al huésped: " + tipoDoc + " " + numDoc);
            throw new RuntimeException("No se pudo cargar la estadía. Huésped no encontrado: " + tipoDoc + " " + numDoc);
        }

        System.out.println(">>> [DEBUG] ¡Éxito! Huésped encontrado: " + huespedOpt.get().getApellido());
        Huesped huespedPrincipal = huespedOpt.get();
        Estadia estadia = new Estadia(parseLocalDate(checkin), huespedPrincipal);

        LocalDate fechaCheckout = parseLocalDate(checkout);
        if (fechaCheckout != null) {
            estadia.setFechaCheckout(fechaCheckout);
            estadia.cerrar();
        }

        return estadia;
    }

    // --- Métodos Ayudantes de parseo (Robustos para evitar errores) ---

    /**
     * Parsea un Integer. Devuelve null si está vacío o no es un número.
     */
    private Integer parseInteger(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null; // Esto es seguro y no causa el crash
        }
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            System.err.println("Advertencia: no se pudo parsear el NÚMERO '" + valor + "'. Se guardará como null.");
            return null;
        }
    }

    /**
     * Parsea un String, lo trimea. Devuelve null si está vacío.
     * (Usado para email o campos que no deban ir en mayúsculas)
     */
    private String parseString(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return valor.trim();
    }

    /**
     * Parsea un String, lo trimea y lo pasa a MAYÚSCULAS.
     * Devuelve null si está vacío.
     */
    private String parseStringMayus(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return valor.trim().toUpperCase();
    }


    /**
     * Parsea una Fecha. Devuelve null si está vacía o tiene formato incorrecto.
     */
    private LocalDate parseLocalDate(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(valor.trim(), formatter);
        } catch (Exception e) {
            System.err.println("Advertencia: no se pudo parsear la FECHA '" + valor + "'. Se guardará como null.");
            return null;
        }
    }
}