package App;

import Logica.Dominio.Entidades.Direccion;
import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Entidades.Huesped;
import Logica.Dominio.Entidades.Reserva;
import Logica.Dominio.Entidades.Usuario;
import Logica.Dominio.Enum.EstadoReserva; // <--- IMPORTANTE
import Persistencia.Repositorios.EstadiaDAO;
import Persistencia.Repositorios.HuespedDAO;
import Persistencia.Repositorios.ReservaDAO;
import Persistencia.Repositorios.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(1)
public class CargadorDeDatos implements CommandLineRunner {

    @Autowired
    private UsuarioDAO usuarioRepository;
    @Autowired
    private HuespedDAO huespedRepository;
    @Autowired
    private EstadiaDAO estadiaRepository;
    @Autowired
    private ReservaDAO reservaRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (usuarioRepository.count() == 0) {
            System.out.println(">>> Cargando usuario por defecto...");
            usuarioRepository.save(new Usuario("Conserje", "conserje123"));
        }

        if (huespedRepository.count() == 0) {
            System.out.println(">>> Cargando huéspedes iniciales...");
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
        }

        if (estadiaRepository.count() == 0) {
            System.out.println(">>> Cargando estadías y reservas...");
            List<Estadia> estadiasACargar = new ArrayList<>();
            try {
                // Fumis (Con reserva)
                estadiasACargar.add(crearEstadia("15/10/2025", "20/10/2025", "DNI", "45828019", true));
                // Gomez Carlos (Walk-in, sin reserva)
                estadiasACargar.add(crearEstadia("01/11/2025", "10/11/2025", "PASAPORTE", "ABC98765", false));
                // Fernandez Juan (Con reserva)
                estadiasACargar.add(crearEstadia("12/11/2025", "15/11/2025", "LE", "8123456", true));
                // Garcia Sofia (Con reserva)
                estadiasACargar.add(crearEstadia("20/11/2025", "22/11/2025", "DNI", "41987654", true));
                // Gomez Ana (Walk-in)
                estadiasACargar.add(crearEstadia("01/12/2025", "05/12/2025", "PASAPORTE", "DEF45678", false));

                estadiaRepository.saveAll(estadiasACargar);
                System.out.println(">>> " + estadiasACargar.size() + " estadías cargadas exitosamente.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private Huesped crearHuesped(String apellido, String nombre, String tipoDoc, String doc, String cuit, String iva, String fechaNac,
                                 String calle, String numero, String depto, String piso, String cp, String localidad, String prov, String pais,
                                 String tel, String email, String ocup, String nac) {
        Direccion dir = new Direccion();
        dir.setCalle(parseStringMayus(calle));
        dir.setDepartamento(parseStringMayus(depto));
        dir.setCodigoPostal(parseStringMayus(cp));
        dir.setLocalidad(parseStringMayus(localidad));
        dir.setProvincia(parseStringMayus(prov));
        dir.setPais(parseStringMayus(pais));
        dir.setNumero(parseInteger(numero));
        dir.setPiso(parseInteger(piso));

        Huesped h = new Huesped();
        h.setApellido(parseStringMayus(apellido));
        h.setNombre(parseStringMayus(nombre));
        h.setTipoDocumento(parseStringMayus(tipoDoc));
        h.setDocumento(parseStringMayus(doc));
        h.setCuit(parseStringMayus(cuit));
        h.setCategoriaIVA(parseStringMayus(iva));
        h.setTelefono(parseStringMayus(tel));
        h.setOcupacion(parseStringMayus(ocup));
        h.setNacionalidad(parseStringMayus(nac));
        h.setFechaNacimiento(parseLocalDate(fechaNac));
        h.setEmail(parseString(email));
        h.setDireccion(dir);
        return h;
    }

    private Estadia crearEstadia(String checkin, String checkout, String tipoDoc, String numDoc, boolean conReserva) {

        Optional<Huesped> huespedOpt = huespedRepository.findByTipoDocumentoAndDocumento(
                parseStringMayus(tipoDoc),
                parseStringMayus(numDoc)
        );

        if (huespedOpt.isEmpty()) throw new RuntimeException("Huésped no encontrado: " + numDoc);

        Huesped principal = huespedOpt.get();
        // Creamos un ArrayList modificable envolviendo al huesped
        List<Huesped> huespedes = new ArrayList<>(Collections.singletonList(principal));

        LocalDate fechaIn = parseLocalDate(checkin);
        LocalDate fechaOut = parseLocalDate(checkout);
        Estadia estadia = new Estadia(fechaIn, huespedes);

        if (conReserva) {
            Reserva reserva = new Reserva();
            reserva.setFechaInicio(fechaIn);
            reserva.setFechaFin(fechaOut);
            reserva.setFechaReserva(fechaIn.minusDays(15));

            // CAMBIO IMPORTANTE: Usamos el Enum
            reserva.setEstado(EstadoReserva.RESERVADA);

            String codigo = "RES-" + principal.getApellido().substring(0, Math.min(3, principal.getApellido().length()))
                    + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            reserva.setCodigoReserva(codigo);
            reserva.setNombre(principal.getNombre());
            reserva.setApellido(principal.getApellido());
            reserva.setTelefono(principal.getTelefono());

            reserva = reservaRepository.save(reserva);
            estadia.setReserva(reserva);
        }

        if (fechaOut != null) {
            estadia.setFechaCheckout(fechaOut);
            estadia.cerrar();
        }

        return estadia;
    }

    private Integer parseInteger(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        try { return Integer.parseInt(valor.trim()); } catch (NumberFormatException e) { return null; }
    }
    private String parseString(String valor) { return (valor == null || valor.trim().isEmpty()) ? null : valor.trim(); }
    private String parseStringMayus(String valor) { return (valor == null || valor.trim().isEmpty()) ? null : valor.trim().toUpperCase(); }
    private LocalDate parseLocalDate(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        try { return LocalDate.parse(valor.trim(), formatter); } catch (Exception e) { return null; }
    }
}