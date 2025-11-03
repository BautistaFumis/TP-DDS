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
            System.out.println(">>> Base de datos de huéspedes vacía. Cargando 10 huéspedes iniciales...");

            List<Huesped> huespedesACargar = Arrays.asList(
                    crearHuesped("Fumis", "Bautista", "DNI", "45828019", "20-45828019-4", "Consumidor Final", "28/05/2004", "Saavedra", "2444", "", "9", "3000", "Santa Fe", "Santa Fe", "Argentina", "3425112233", "bautistafumis@gmail.com", "Estudiante", "Argentina"),
                    crearHuesped("Fernandez", "Juan", "LE", "8123456", "20-8123456-9", "Monotributista", "11/12/1950", "Bv. Pellegrini", "3201", "", "2", "3000", "Santa Fe", "Santa Fe", "Argentina", "3424111222", "juan.fernandez@example.com", "Jubilado", "Argentina"),
                    crearHuesped("Garcia", "Sofia", "DNI", "41987654", "27-41987654-5", "Consumidor Final", "19/09/1999", "Alvear", "1530", "", "", "2000", "Rosario", "Santa Fe", "Argentina", "3415876543", "sofia.garcia@example.com", "Diseñadora", "Argentina"),
                    crearHuesped("Lopez", "Martin", "DNI", "35123456", "", "Consumidor Final", "18/02/1990", "Urquiza", "1850", "", "", "3000", "Santa Fe", "Santa Fe", "Argentina", "3425443322", "martin.lopez@example.com", "Programador", "Argentina"),
                    crearHuesped("Gomez", "Ana", "PASAPORTE", "DEF45678", "", "Monotributista", "25/06/1992", "Rivadavia", "2899", "", "", "3100", "Parana", "Entre Rios", "Argentina", "3434556677", "ana.gomez@example.com", "Abogada", "Argentina"),
                    crearHuesped("Perez", "Juana", "DNI", "38765432", "", "Consumidor Final", "30/11/1995", "J. J. Paso", "4500", "", "", "3000", "Santa Fe", "Santa Fe", "Argentina", "3425998877", "juana.perez@example.com", "Medica", "Argentina"),
                    crearHuesped("Locatelli", "Cristian", "DNI", "44292971", "20-44292971-9", "Consumidor Final", "07/11/2002", "Salta", "2200", "A", "1", "3000", "Santa Fe", "Santa Fe", "Argentina", "3482257103", "criatanlocatelli02@gmail.com", "Estudiante", "Argentina"),
                    crearHuesped("Cavani", "Edison", "LE", "912", "", "Consumidor Final", "10/10/2010", "LA BOCA", "8990", "", "", "3000", "BSAS", "Buenos Aires", "Argentina", "", "", "Futbolista", "Uruguayo"),
                    crearHuesped("Messi", "Leo", "DNI", "124", "", "Consumidor Final", "10/10/2010", "Barcelona", "30", "", "10", "2000", "Catalunia", "Espain", "Espain", "", "", "GOAT", "Otro Planeta"),
                    crearHuesped("Martinez", "Matias", "LE", "32144231", "20-32144231-9", "Consumidor Final", "22/05/2003", "Jujuy", "516", "", "", "3100", "Parana", "Entre Rios", "Arg", "", "", "", ""),
                    crearHuesped("Gomez", "Carlos", "PASAPORTE", "ABC98765", "20-28765432-1", "Responsable Inscripto", "22/07/1980", "Av. Corrientes", "950", "A", "5", "1043", "Buenos Aires", "CABA", "Argentina", "1155667788", "carlos.martinez@work.com", "Arquitecto", "Uruguaya")
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
                        crearEstadia("01/11/2025", "10/11/2025", "PASAPORTE", "ABC98765"), // Martinez, Carlos
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

    private Huesped crearHuesped(String apellido, String nombre, String tipoDoc, String doc, String cuit, String iva, String fechaNac,
                                 String calle, String numero, String depto, String piso, String cp, String localidad, String prov, String pais,
                                 String tel, String email, String ocup, String nac) {

        Direccion dir = new Direccion();
        dir.setCalle(calle);
        dir.setNumero(parseInteger(numero));
        dir.setDepartamento(parseString(depto));
        dir.setPiso(parseInteger(piso));
        dir.setCodigoPostal(cp);
        dir.setLocalidad(localidad);
        dir.setProvincia(prov);
        dir.setPais(pais);

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
        h.setDireccion(dir);

        return h;
    }

    private Estadia crearEstadia(String checkin, String checkout, String tipoDoc, String numDoc) {
        System.out.println(">>> [DEBUG] Buscando huésped: " + tipoDoc + " " + numDoc);
        Optional<Huesped> huespedOpt = huespedRepository.findByTipoDocumentoAndDocumento(tipoDoc, numDoc);

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

    // --- Métodos Ayudantes de parseo ---
    private Integer parseInteger(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        return Integer.parseInt(valor.trim());
    }

    private String parseString(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        return valor.trim();
    }

    private LocalDate parseLocalDate(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        return LocalDate.parse(valor.trim(), formatter);
    }
}
