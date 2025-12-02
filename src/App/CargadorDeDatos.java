package App;

import Logica.Dominio.Entidades.*;
import Logica.Dominio.Enum.EstadoHabitacion;
import Logica.Dominio.Enum.EstadoReserva;
import Logica.Dominio.Enum.TipoEstadoEstadia;
import Persistencia.Repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    @Autowired
    private HabitacionDAO habitacionRepository; // <--- NUEVO REPOSITORIO

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // --- 1. USUARIOS ---
        if (usuarioRepository.count() == 0) {
            System.out.println(">>> Cargando usuario por defecto...");
            usuarioRepository.save(new Usuario("Conserje", "conserje123"));
        }

        // --- 2. HABITACIONES (NUEVO: HERENCIA) ---
        if (habitacionRepository.count() == 0) {
            System.out.println(">>> Cargando habitaciones (Probando Herencia)...");
            List<Habitacion> habitaciones = new ArrayList<>();

            // 1. Individual Estándar (1 Cama Indiv)
            habitaciones.add(new IndividualEstandar("101", EstadoHabitacion.LIBRE, 40000.0f, 1));

            // 2. Doble Estándar (1 Doble, 0 Indiv)
            habitaciones.add(new DobleEstandar("102", EstadoHabitacion.OCUPADA, 60000.0f, 1, 0));

            // 3. Doble Superior (1 Doble, 1 Indiv, 1 King)
            habitaciones.add(new DobleSuperior("201", EstadoHabitacion.LIBRE, 90000.0f, 1, 1, 1));

            // 4. Suite Doble (2 Dobles, 0 Indiv)
            habitaciones.add(new SuiteDoble("202", EstadoHabitacion.OCUPADA, 120000.0f, 2, 0));

            // 5. Superior Family Plan (2 Dobles, 2 Indiv)
            habitaciones.add(new SuperiorFamilyPlan("301", EstadoHabitacion.LIBRE, 150000.0f, 2, 2));

            habitacionRepository.saveAll(habitaciones);
            System.out.println(">>> " + habitaciones.size() + " habitaciones cargadas.");
        }

        // --- 3. HUÉSPEDES ---
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

        // --- 4. ESTADÍAS, RESERVAS Y SERVICIOS ---
        if (estadiaRepository.count() == 0) {
            System.out.println(">>> Cargando estadías, reservas y asignando servicios...");
            List<Estadia> estadiasACargar = new ArrayList<>();

            // Obtenemos las habitaciones para asignarlas
            List<Habitacion> habitaciones = habitacionRepository.findAll();

            try {
                // Fumis (Con reserva) - Hab 101
                Estadia e1 = crearEstadia("15/10/2025", "20/10/2025", "DNI", "45828019", true, habitaciones.get(0));
                e1.agregarServicio(new Servicio("Coca Cola", 2500f));
                e1.agregarServicio(new Servicio("Papas Fritas", 3500f));
                estadiasACargar.add(e1);

                // Gomez Carlos (Walk-in) - Hab 102
                Estadia e2 = crearEstadia("01/11/2025", "10/11/2025", "PASAPORTE", "ABC98765", false, habitaciones.get(1));
                e2.agregarServicio(new Servicio("Desayuno Continental", 8000f));
                estadiasACargar.add(e2);

                // Fernandez Juan (Con reserva) - Hab 201
                Estadia e3 = crearEstadia("12/12/2025", "15/12/2025", "LE", "8123456", true, habitaciones.get(2));
                e3.agregarServicio(new Servicio("Lavandería", 5000f));
                estadiasACargar.add(e3);

                // Garcia Sofia (Con reserva) - Hab 202
                estadiasACargar.add(crearEstadia("20/12/2025", "22/12/2025", "DNI", "41987654", true, habitaciones.get(3)));

                // Gomez Ana (Walk-in) - Hab 301
                estadiasACargar.add(crearEstadia("01/12/2025", "05/12/2025", "PASAPORTE", "DEF45678", false, habitaciones.get(4)));

                estadiaRepository.saveAll(estadiasACargar);
                System.out.println(">>> " + estadiasACargar.size() + " estadías cargadas exitosamente.");

            } catch (Exception e) {
                System.err.println("ERROR al cargar estadías: " + e.getMessage());
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

    // CAMBIO IMPORTANTE: Ahora recibe la 'habitacion' por parámetro
    private Estadia crearEstadia(String checkin, String checkout, String tipoDoc, String numDoc, boolean conReserva, Habitacion habitacion) {

        Optional<Huesped> huespedOpt = huespedRepository.findByTipoDocumentoAndDocumento(
                parseStringMayus(tipoDoc),
                parseStringMayus(numDoc)
        );

        if (huespedOpt.isEmpty()) throw new RuntimeException("Huésped no encontrado: " + numDoc);

        Huesped principal = huespedOpt.get();
        // Usamos ArrayList modificable
        List<Huesped> huespedes = new ArrayList<>(Collections.singletonList(principal));

        LocalDate fechaIn = parseLocalDate(checkin);
        LocalDate fechaOut = parseLocalDate(checkout);

        // Constructor actualizado: recibe la Habitacion
        Estadia estadia = new Estadia(fechaIn, fechaOut, habitacion, Logica.Dominio.Enum.TipoEstadoEstadia.ACTIVA);

        if (conReserva) {
            Reserva reserva = new Reserva();
            reserva.setFechaInicio(fechaIn);
            reserva.setFechaFin(fechaOut);
            reserva.setFechaReserva(fechaIn.minusDays(15));
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

        LocalDate hoy = LocalDate.now();

        if (fechaOut.isBefore(hoy)) {
            estadia.setTipoEstado(TipoEstadoEstadia.CERRADA); // Pasada
        } else if (fechaIn.isAfter(hoy)) {
            estadia.setTipoEstado(TipoEstadoEstadia.RESERVADA); // Futura
        } else {
            estadia.setTipoEstado(TipoEstadoEstadia.ACTIVA); // Hoy o en curso
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