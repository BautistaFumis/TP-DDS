// Archivo: src/Main.java

// --- Importaciones combinadas de ambos archivos ---
import Clases.Direccion;
import Clases.Huesped;
import Excepciones.CamposObligatoriosException;
import Excepciones.CredencialesInvalidasException; // Asumiendo que esta excepción está en este paquete
import Excepciones.DocumentoDuplicadoException;
import Gestores.GestorHuesped;
import Gestores.GestorUsuario; // Asumiendo que el gestor de usuario está en este paquete
import Persistencia.HuespedDAO;
import Persistencia.HuespedDAOImpl;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // --- 1. Inicialización de componentes ---
        // Se instancian todos los objetos necesarios para que la aplicación funcione.
        Scanner scanner = new Scanner(System.in);
        GestorUsuario gestorUsuario = new GestorUsuario();
        HuespedDAO huespedDAO = new HuespedDAOImpl();
        GestorHuesped gestorHuesped = new GestorHuesped(huespedDAO);
        boolean autenticado = false;

        System.out.println("========================================");
        System.out.println(" BIENVENIDO AL SISTEMA DE GESTIÓN HOTELERA ");
        System.out.println("========================================");

        // --- 2. Bucle de Autenticación ---
        // El programa no continuará hasta que el usuario se autentique con éxito.
        while (!autenticado) {
            try {
                System.out.println("\nPor favor, inicie sesión:");
                System.out.print("Usuario: ");
                String id = scanner.nextLine();

                System.out.print("Contraseña: ");
                String password = scanner.nextLine();

                gestorUsuario.autenticar(id, password);
                autenticado = true; // Si no hay excepción, el login es exitoso
                System.out.println("\n¡Inicio de sesión exitoso! Bienvenido, " + id + ".");

            } catch (CredencialesInvalidasException e) {
                System.err.println("Error: " + e.getMessage());
                System.out.println("Por favor, intente de nuevo.");
            }
        }

        // --- 3. Menú Principal de Casos de Uso ---
        // Este menú solo se muestra después de una autenticación exitosa.
        int option;
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("Seleccione el Caso de Uso que desea ejecutar:");
            System.out.println("1. CU01 - Autenticar Usuario");
            System.out.println("2. CU02 - Buscar Huésped");
            System.out.println("9. CU09 - Dar de alta Huésped");
            System.out.println("10. CU10 - Modificar Huésped");
            System.out.println("11. CU11 - Dar de baja Huésped");
            System.out.println("0. Salir");
            System.out.print("Opción: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                option = -1; // Opción inválida si no es un número
            }

            switch (option) {
                case 1:
                    System.out.println("Ya se encuentra autenticado.");
                    break;
                case 2:
                    System.out.println("Ejecutando CU02: Buscar Huésped (lógica no implementada)...");
                    break;
                case 9:
                    // Se llama al método que contiene la lógica del CU09
                    ejecutarAltaHuesped(scanner, gestorHuesped);
                    break;
                case 10:
                    System.out.println("Ejecutando CU10: Modificar Huésped (lógica no implementada)...");
                    break;
                case 11:
                    System.out.println("Ejecutando CU11: Dar de baja Huésped (lógica no implementada)...");
                    break;
                case 0:
                    System.out.println("Saliendo del sistema. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
                    break;
            }
        } while (option != 0);

        scanner.close();
    }

    /**
     * Encapsula toda la lógica para el Caso de Uso 09: Dar de alta Huésped.
     * @param scanner El objeto Scanner para leer la entrada del usuario.
     * @param gestor El GestorHuesped que maneja la lógica de negocio.
     */
    private static void ejecutarAltaHuesped(Scanner scanner, GestorHuesped gestor) {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n--- ALTA DE NUEVO HUÉSPED (CU09) ---");
            System.out.println("Por favor, ingrese los datos solicitados. Los campos con (*) son obligatorios.");

            Huesped huespedParaAlta = new Huesped();

            System.out.print("(*) Apellido: ");
            huespedParaAlta.setApellido(scanner.nextLine());

            System.out.print("(*) Nombre: ");
            huespedParaAlta.setNombre(scanner.nextLine());

            System.out.print("Email: ");
            huespedParaAlta.setEmail(scanner.nextLine());

            System.out.print("(*) Tipo de Documento (DNI, PASAPORTE): ");
            huespedParaAlta.setTipoDocumento(scanner.nextLine());

            try {
                System.out.print("(*) Número de Documento: ");
                huespedParaAlta.setDocumento(Integer.parseInt(scanner.nextLine()));

                System.out.print("Teléfono: ");
                huespedParaAlta.setTelefono(Integer.parseInt(scanner.nextLine()));

                // La dirección se crea vacía por ahora.
                huespedParaAlta.setDireccion(new Direccion());

                gestor.registrarNuevoHuesped(huespedParaAlta);

                System.out.println("\nÉXITO: El huésped '" + huespedParaAlta.getNombre() + " " + huespedParaAlta.getApellido() + "' ha sido satisfactoriamente cargado al sistema.");

            } catch (NumberFormatException e) {
                System.err.println("\nERROR: El documento y el teléfono deben ser números válidos.");
            } catch (CamposObligatoriosException e) {
                System.err.println("\nERROR: " + e.getMessage());
            } catch (DocumentoDuplicadoException e) {
                System.err.println("\n⚠ ADVERTENCIA: " + e.getMessage());
                System.out.print("¿Desea aceptarlo igualmente? [1] ACEPTAR IGUALMENTE / [2] CORREGIR: ");
                String opcion = scanner.nextLine();
                if ("1".equals(opcion)) {
                    gestor.registrarHuespedAceptandoDuplicado(huespedParaAlta);
                    System.out.println("ÉXITO: Se ha registrado el huésped duplicado.");
                } else {
                    System.out.println("Operación cancelada. Por favor, ingrese los datos nuevamente.");
                }
            }

            System.out.print("\n¿Desea cargar otro huésped? (SI/NO): ");
            if (!scanner.nextLine().equalsIgnoreCase("SI")) {
                continuar = false;
            }
        }
    }
}