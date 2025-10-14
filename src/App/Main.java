package App;

import Logica.Dominio.Direccion;
import Logica.Dominio.Huesped;
import Logica.Excepciones.CamposObligatoriosException;
import Logica.Excepciones.CredencialesInvalidasException;
import Logica.Excepciones.DocumentoDuplicadoException;
import Logica.Gestores.GestorHuesped;
import Logica.Gestores.GestorUsuario;
import Persistencia.HuespedDAO;
import Persistencia.HuespedDAOImpl;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // 1. Inicialización de Objetos
        Scanner scanner = new Scanner(System.in);
        GestorUsuario gestorUsuario = new GestorUsuario();
        HuespedDAO huespedDAO = new HuespedDAOImpl();
        GestorHuesped gestorHuesped = new GestorHuesped(huespedDAO);
        boolean autenticado = false;

        System.out.println("========================================");
        System.out.println(" BIENVENIDO AL SISTEMA DE GESTIÓN HOTELERA ");
        System.out.println("========================================");

        //2. Bucle de Autenticación - El programa no continuará hasta que el usuario se autentique con éxito.
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

        // 3. Menú Principal de Casos de Uso
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
                    System.out.println("Ya se encuentra autenticado."); // Esto no estabamos seguros de si ponerlo o no, porque la autenticacion se hace al ingresar a la aplicacion.
                    break;
                case 2:
                    // Se llama al metodo que contiene la lógica del CU02
                    ejecutarBusquedaHuesped(scanner, gestorHuesped);
                    break;
                case 9:
                    // Se llama al metodo que contiene la lógica del CU09
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
                System.err.println("\n ADVERTENCIA: " + e.getMessage());
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

    private static void ejecutarBusquedaHuesped(Scanner scanner, GestorHuesped gestor) {
        System.out.println("\n--- BÚSQUEDA DE HUÉSPED (CU02) ---");
        System.out.println("Ingrese los criterios de búsqueda (deje en blanco para omitir).");

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        // 1. Realizamos la búsqueda a través del gestor
        List<Huesped> resultados = gestor.buscarHuespedes(apellido, nombre);

        // 2. Flujo Alternativo 4.A: No se encontraron resultados
        if (resultados.isEmpty()) {
            System.out.println("\nNo se encontró ninguna concordancia.");
            System.out.println("Redirigiendo al alta de huésped...");
            ejecutarAltaHuesped(scanner, gestor); // Reutilizamos el CU09
            return;
        }

        // 3. Flujo Principal: Se encontraron resultados
        System.out.println("\n--- RESULTADOS DE LA BÚSQUEDA ---");
        for (int i = 0; i < resultados.size(); i++) {
            Huesped h = resultados.get(i);
            System.out.printf("%d. %s, %s - %s: %d\n", (i + 1), h.getApellido(), h.getNombre(), h.getTipoDocumento(), h.getDocumento());
        }

        System.out.print("\nSeleccione un huésped por su número o presione [Enter] para crear uno nuevo: ");
        String seleccion = scanner.nextLine();

        // 4. Flujo Alternativo 5.A: El usuario no selecciona a nadie y presiona Enter
        if (seleccion.isEmpty()) {
            System.out.println("\nNo se seleccionó un huésped existente.");
            System.out.println("Redirigiendo al alta de huésped...");
            ejecutarAltaHuesped(scanner, gestor);
            return;
        }

        try {
            int indice = Integer.parseInt(seleccion) - 1;
            if (indice >= 0 && indice < resultados.size()) {
                Huesped huespedSeleccionado = resultados.get(indice);
                System.out.println("\nHuésped seleccionado: " + huespedSeleccionado.getNombre() + " " + huespedSeleccionado.getApellido());
                // 5. Flujo Principal 6: Derivar al CU10 "Modificar Huésped"
                System.out.println("Ejecutando CU10: Modificar Huésped (lógica no implementada)...");
                // Aquí iría la llamada al metodo: ejecutarModificarHuesped(scanner, gestor, huespedSeleccionado);
            } else {
                System.err.println("Número de selección inválido.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Entrada inválida. Por favor, ingrese un número.");
        }
    }
}