package App;

import Logica.Dominio.Entidades.*;
import Logica.Dominio.Enum.EstadoHabitacion;
import Logica.Dominio.Enum.EstadoReserva;
import Logica.Dominio.Enum.TipoEstadoEstadia;
import Repositorios.*;
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

    @Autowired private UsuarioDAO usuarioRepository;
    @Autowired private HuespedDAO huespedRepository;
    @Autowired private EstadiaDAO estadiaRepository;
    @Autowired private ReservaDAO reservaRepository;
    @Autowired private HabitacionDAO habitacionRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (usuarioRepository.count() == 0) {
            System.out.println(">>> Cargando usuario por defecto...");
            usuarioRepository.save(new Usuario("Conserje", "conserje123"));
        }

        if (habitacionRepository.count() == 0) {
            System.out.println(">>> Cargando habitaciones...");
            List<Habitacion> habitaciones = new ArrayList<>();
            habitaciones.add(new IndividualEstandar("101", EstadoHabitacion.OCUPADA, 40000.0f, 1));
            habitaciones.add(new DobleEstandar("102", EstadoHabitacion.OCUPADA, 60000.0f, 1, 0));
            habitaciones.add(new DobleSuperior("201", EstadoHabitacion.LIBRE, 90000.0f, 1, 1, 1));
            habitaciones.add(new SuiteDoble("202", EstadoHabitacion.LIBRE, 120000.0f, 2, 0));
            habitaciones.add(new SuperiorFamilyPlan("301", EstadoHabitacion.LIBRE, 150000.0f, 2, 2));
            habitacionRepository.saveAll(habitaciones);
        }

        if (huespedRepository.count() == 0) {
            System.out.println(">>> Cargando huéspedes...");

            List<Huesped> huespedes = Arrays.asList(
                    // Apellido, Nombre, TipoDoc, Doc, Cuit, IVA, Nacimiento, Calle, Num, Localidad, Provincia, Pais, CP, Tel, Email, Ocupacion
                    crearHuesped("FUMIS", "BAUTISTA", "DNI", "45828019", "20-45828019-4", "CONSUMIDOR FINAL", "28/05/2004", "SAAVEDRA", 2444, "SANTA FE", "SANTA FE", "ARGENTINA", "3000", "3425112233", "bautista@mail.com", "ESTUDIANTE"),
                    crearHuesped("FERNANDEZ", "JUAN", "LE", "8123456", "20-8123456-9", "MONOTRIBUTISTA", "11/12/1950", "BV. PELLEGRINI", 3201, "SANTA FE", "SANTA FE", "ARGENTINA", "3000", "3424556677", "juan@mail.com", "JUBILADO"),
                    crearHuesped("GARCIA", "SOFIA", "DNI", "41987654", "27-41987654-5", "CONSUMIDOR FINAL", "19/09/1999", "ALVEAR", 1530, "ROSARIO", "SANTA FE", "ARGENTINA", "2000", "3415889900", "sofia@mail.com", "DISEÑADORA"),
                    crearHuesped("LOPEZ", "MARTIN", "DNI", "35123456", null, "CONSUMIDOR FINAL", "18/02/1990", "URQUIZA", 1850, "SANTA FE", "SANTA FE", "ARGENTINA", "3000", "3425000000", "martin@mail.com", "PROGRAMADOR"),
                    crearHuesped("GOMEZ", "CARLOS", "PASAPORTE", "ABC98765", "20-28765432-1", "RESPONSABLE INSCRIPTO", "22/07/1980", "AV. CORRIENTES", 950, "CABA", "BUENOS AIRES", "ARGENTINA", "1043", "1155667788", "carlos@mail.com", "ARQUITECTO"),
                    crearHuesped("MESSI", "LEO", "DNI", "10101010", null, "CONSUMIDOR FINAL", "24/06/1987", "LAVALLE", 10, "ROSARIO", "SANTA FE", "ARGENTINA", "2000", "3411010101", "leo@mail.com", "FUTBOLISTA")
            );
            huespedRepository.saveAllAndFlush(huespedes);
            System.out.println(">>> Huéspedes cargados.");
        }

        if (estadiaRepository.count() == 0) {
            System.out.println(">>> Generando estadías dinámicas (relativas a HOY)...");
            List<Habitacion> habs = habitacionRepository.findAll();
            // Helpers rápidos para buscar habitación por número
            Habitacion h101 = habs.stream().filter(h -> h.getNumero().equals("101")).findFirst().orElseThrow();
            Habitacion h102 = habs.stream().filter(h -> h.getNumero().equals("102")).findFirst().orElseThrow();
            Habitacion h201 = habs.stream().filter(h -> h.getNumero().equals("201")).findFirst().orElseThrow();
            Habitacion h202 = habs.stream().filter(h -> h.getNumero().equals("202")).findFirst().orElseThrow();
            Habitacion h301 = habs.stream().filter(h -> h.getNumero().equals("301")).findFirst().orElseThrow();

            LocalDate hoy = LocalDate.now();
            List<Estadia> estadías = new ArrayList<>();

            try {
                Estadia eActiva1 = crearEstadia(hoy.minusDays(2), hoy.plusDays(1), "PASAPORTE", "ABC98765", true, h102);
                eActiva1.agregarServicio(new Servicio("Champagne", 15000f));
                eActiva1.agregarServicio(new Servicio("Room Service Cena", 25000f));
                estadías.add(eActiva1);

                Estadia eActiva2 = crearEstadia(hoy, hoy.plusDays(3), "DNI", "45828019", false, h101);
                eActiva2.agregarServicio(new Servicio("Coca Cola", 3000f));
                eActiva2.agregarServicio(new Servicio("Papas Fritas", 4500f));
                estadías.add(eActiva2);

                estadías.add(crearEstadia(hoy.plusDays(15), hoy.plusDays(20), "DNI", "41987654", true, h201));
                estadías.add(crearEstadia(hoy.plusMonths(1), hoy.plusMonths(1).plusDays(5), "DNI", "10101010", true, h202));

                Estadia ePasada = crearEstadia(hoy.minusMonths(1), hoy.minusMonths(1).plusDays(5), "LE", "8123456", true, h301);
                ePasada.agregarServicio(new Servicio("Lavandería", 5000f));
                estadías.add(ePasada);

                estadiaRepository.saveAll(estadías);
                System.out.println(">>> Estadías y reservas cargadas exitosamente.");

            } catch (Exception e) {
                System.err.println("ERROR al cargar estadías: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    private Huesped crearHuesped(String apellido, String nombre, String tipoDoc, String doc, String cuit, String iva, String fechaNac,
                                 String calle, Integer numero, String localidad, String provincia, String pais, String cp,
                                 String tel, String email, String ocupacion) {

        return Huesped.builder()
                .nombre(nombre)
                .apellido(apellido)
                .tipoDocumento(tipoDoc)
                .documento(doc)
                .cuit(cuit)
                .categoriaIVA(iva)
                .fechaNacimiento(parseLocalDateStr(fechaNac))
                .nacionalidad(pais)
                .ocupacion(ocupacion)
                .telefono(tel)
                .email(email)

                .calle(calle)
                .numero(numero)
                .localidad(localidad)
                .provincia(provincia)
                .pais(pais)
                .codigoPostal(cp)
                .build();
    }

    private Estadia crearEstadia(LocalDate checkin, LocalDate checkout, String tipoDoc, String numDoc, boolean conReserva, Habitacion habitacion) {
        Optional<Huesped> huespedOpt = huespedRepository.findByTipoDocumentoAndDocumento(tipoDoc, numDoc);
        if (huespedOpt.isEmpty()) throw new RuntimeException("Huésped no encontrado: " + numDoc);
        Huesped principal = huespedOpt.get();

        List<Huesped> listaHuespedes = new ArrayList<>();
        listaHuespedes.add(principal);

        TipoEstadoEstadia estado;
        LocalDate hoy = LocalDate.now();
        if (checkout.isBefore(hoy)) {
            estado = TipoEstadoEstadia.CERRADA;
        } else if (checkin.isAfter(hoy)) {
            estado = TipoEstadoEstadia.RESERVADA;
        } else {
            estado = TipoEstadoEstadia.ACTIVA;
        }

        Estadia estadia = new Estadia(checkin, checkout, habitacion, estado);
        estadia.setHuespedes(listaHuespedes);

        if (conReserva || estado == TipoEstadoEstadia.RESERVADA) {
            Reserva reserva = new Reserva();
            reserva.setFechaInicio(checkin);
            reserva.setFechaFin(checkout);
            reserva.setFechaReserva(checkin.minusDays(10));
            reserva.setEstado(estado == TipoEstadoEstadia.CERRADA ? EstadoReserva.FINALIZADA : EstadoReserva.RESERVADA);

            String codigo = "RES-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
            reserva.setCodigoReserva(codigo);
            reserva.setNombre(principal.getNombre());
            reserva.setApellido(principal.getApellido());
            reserva.setTelefono(principal.getTelefono());

            reserva = reservaRepository.save(reserva);
            estadia.setReserva(reserva);
        }

        return estadia;
    }

    private LocalDate parseLocalDateStr(String fecha) {
        try { return LocalDate.parse(fecha, formatter); } catch (Exception e) { return null; }
    }
}