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

    @Autowired private UsuarioDAO usuarioRepository;
    @Autowired private HuespedDAO huespedRepository;
    @Autowired private EstadiaDAO estadiaRepository;
    @Autowired private ReservaDAO reservaRepository;
    @Autowired private HabitacionDAO habitacionRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // 1. USUARIO ADMIN
        if (usuarioRepository.count() == 0) {
            System.out.println(">>> Cargando usuario por defecto...");
            usuarioRepository.save(new Usuario("Conserje", "conserje123"));
        }

        // 2. HABITACIONES
        if (habitacionRepository.count() == 0) {
            System.out.println(">>> Cargando habitaciones...");
            List<Habitacion> habitaciones = new ArrayList<>();
            habitaciones.add(new IndividualEstandar("101", EstadoHabitacion.OCUPADA, 40000.0f, 1)); // Ocupada hoy
            habitaciones.add(new DobleEstandar("102", EstadoHabitacion.OCUPADA, 60000.0f, 1, 0));    // Ocupada hoy
            habitaciones.add(new DobleSuperior("201", EstadoHabitacion.LIBRE, 90000.0f, 1, 1, 1));   // Reservada futuro
            habitaciones.add(new SuiteDoble("202", EstadoHabitacion.LIBRE, 120000.0f, 2, 0));        // Reservada futuro
            habitaciones.add(new SuperiorFamilyPlan("301", EstadoHabitacion.LIBRE, 150000.0f, 2, 2)); // Libre
            habitacionRepository.saveAll(habitaciones);
        }

        // 3. HUÉSPEDES
        if (huespedRepository.count() == 0) {
            System.out.println(">>> Cargando huéspedes...");
            List<Huesped> huespedes = Arrays.asList(
                    // Huésped para Factura B (Consumidor Final) - Ocupando Hab 101
                    crearHuesped("FUMIS", "BAUTISTA", "DNI", "45828019", "20-45828019-4", "CONSUMIDOR FINAL", "28/05/2004", "SAAVEDRA", "2444", "SANTA FE"),

                    // Huésped para Historial Pasado (No se puede borrar)
                    crearHuesped("FERNANDEZ", "JUAN", "LE", "8123456", "20-8123456-9", "MONOTRIBUTISTA", "11/12/1950", "BV. PELLEGRINI", "3201", "SANTA FE"),

                    // Huésped para Cancelar Reserva (Futura) - Hab 201
                    crearHuesped("GARCIA", "SOFIA", "DNI", "41987654", "27-41987654-5", "CONSUMIDOR FINAL", "19/09/1999", "ALVEAR", "1530", "ROSARIO"),

                    // Huésped sin historial (Se puede borrar)
                    crearHuesped("LOPEZ", "MARTIN", "DNI", "35123456", "", "CONSUMIDOR FINAL", "18/02/1990", "URQUIZA", "1850", "SANTA FE"),

                    // Huésped para Factura A (Responsable Inscripto) - Ocupando Hab 102
                    crearHuesped("GOMEZ", "CARLOS", "PASAPORTE", "ABC98765", "20-28765432-1", "RESPONSABLE INSCRIPTO", "22/07/1980", "AV. CORRIENTES", "950", "CABA"),

                    // Otro huésped para reserva futura - Hab 202
                    crearHuesped("MESSI", "LEO", "DNI", "10101010", "", "CONSUMIDOR FINAL", "24/06/1987", "ROSARIO", "10", "ROSARIO")
            );
            huespedRepository.saveAllAndFlush(huespedes);
            System.out.println(">>> Huéspedes cargados.");
        }

        // 4. ESTADÍAS Y RESERVAS (Lógica Dinámica)
        if (estadiaRepository.count() == 0) {
            System.out.println(">>> Generando estadías dinámicas (relativas a HOY)...");

            List<Habitacion> habs = habitacionRepository.findAll();
            // Mapeo rápido por número para no confundirnos
            Habitacion h101 = habs.stream().filter(h -> h.getNumero().equals("101")).findFirst().get();
            Habitacion h102 = habs.stream().filter(h -> h.getNumero().equals("102")).findFirst().get();
            Habitacion h201 = habs.stream().filter(h -> h.getNumero().equals("201")).findFirst().get();
            Habitacion h202 = habs.stream().filter(h -> h.getNumero().equals("202")).findFirst().get();
            Habitacion h301 = habs.stream().filter(h -> h.getNumero().equals("301")).findFirst().get();

            LocalDate hoy = LocalDate.now();
            List<Estadia> estadías = new ArrayList<>();

            try {
                // --- CASO 1: ESTADÍA ACTIVA (Para Facturar) - Habitacion 102 ---
                // Carlos Gomez (RI). Entró hace 2 días, sale mañana.
                // Ideal para probar "Facturar" -> Debería salir Factura A.
                Estadia eActiva1 = crearEstadia(hoy.minusDays(2), hoy.plusDays(1), "PASAPORTE", "ABC98765", true, h102);
                eActiva1.agregarServicio(new Servicio("Champagne", 15000f));
                eActiva1.agregarServicio(new Servicio("Room Service Cena", 25000f));
                estadías.add(eActiva1);

                // --- CASO 2: ESTADÍA ACTIVA (Para Facturar) - Habitacion 101 ---
                // Bautista Fumis (CF). Entró hoy, sale en 3 días.
                // Ideal para probar "Facturar" -> Debería salir Factura B.
                Estadia eActiva2 = crearEstadia(hoy, hoy.plusDays(3), "DNI", "45828019", false, h101);
                eActiva2.agregarServicio(new Servicio("Coca Cola", 3000f));
                eActiva2.agregarServicio(new Servicio("Papas Fritas", 4500f));
                estadías.add(eActiva2);

                // --- CASO 3: RESERVA FUTURA (Para Cancelar) - Habitacion 201 ---
                // Sofia Garcia. Reserva para dentro de 15 días.
                // Ideal para probar "Cancelar Reserva" -> Buscar por "GARCIA".
                estadías.add(crearEstadia(hoy.plusDays(15), hoy.plusDays(20), "DNI", "41987654", true, h201));

                // --- CASO 4: RESERVA FUTURA (Para Cancelar) - Habitacion 202 ---
                // Leo Messi. Reserva para el mes que viene.
                estadías.add(crearEstadia(hoy.plusMonths(1), hoy.plusMonths(1).plusDays(5), "DNI", "10101010", true, h202));

                // --- CASO 5: ESTADÍA PASADA (Historial) - Habitacion 301 ---
                // Juan Fernandez. Estuvo el mes pasado.
                // Ideal para probar que NO se puede borrar al huésped.
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

    // --- MÉTODOS AUXILIARES ---

    private Huesped crearHuesped(String apellido, String nombre, String tipoDoc, String doc, String cuit, String iva, String fechaNac, String calle, String numero, String localidad) {
        Direccion dir = new Direccion();
        dir.setCalle(calle);
        dir.setNumero(Integer.parseInt(numero));
        dir.setLocalidad(localidad);
        dir.setProvincia("SANTA FE");
        dir.setPais("ARGENTINA");
        dir.setCodigoPostal("3000");

        Huesped h = new Huesped();
        h.setApellido(apellido);
        h.setNombre(nombre);
        h.setTipoDocumento(tipoDoc);
        h.setDocumento(doc);
        h.setCuit(cuit != null && !cuit.isEmpty() ? cuit : null);
        h.setCategoriaIVA(iva);
        h.setFechaNacimiento(parseLocalDateStr(fechaNac));
        h.setTelefono("342000000");
        h.setEmail(nombre.toLowerCase() + "." + apellido.toLowerCase() + "@mail.com");
        h.setOcupacion("VARIOS");
        h.setNacionalidad("ARGENTINA");
        h.setDireccion(dir);
        return h;
    }

    private Estadia crearEstadia(LocalDate checkin, LocalDate checkout, String tipoDoc, String numDoc, boolean conReserva, Habitacion habitacion) {

        // 1. Buscar Huésped
        Optional<Huesped> huespedOpt = huespedRepository.findByTipoDocumentoAndDocumento(tipoDoc, numDoc);
        if (huespedOpt.isEmpty()) throw new RuntimeException("Huésped no encontrado: " + numDoc);
        Huesped principal = huespedOpt.get();

        // 2. Crear Lista de Huéspedes
        List<Huesped> listaHuespedes = new ArrayList<>();
        listaHuespedes.add(principal);

        // 3. Determinar Estado
        TipoEstadoEstadia estado;
        LocalDate hoy = LocalDate.now();
        if (checkout.isBefore(hoy)) {
            estado = TipoEstadoEstadia.CERRADA; // Pasada
        } else if (checkin.isAfter(hoy)) {
            estado = TipoEstadoEstadia.RESERVADA; // Futura
        } else {
            estado = TipoEstadoEstadia.ACTIVA; // En curso
        }

        // 4. Crear Estadía
        Estadia estadia = new Estadia(checkin, checkout, habitacion, estado);
        estadia.setHuespedes(listaHuespedes); // <--- VINCULACIÓN IMPORTANTE

        // 5. Crear Reserva asociada si corresponde
        if (conReserva || estado == TipoEstadoEstadia.RESERVADA) {
            Reserva reserva = new Reserva();
            reserva.setFechaInicio(checkin);
            reserva.setFechaFin(checkout);
            reserva.setFechaReserva(checkin.minusDays(10)); // Reservó 10 días antes
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