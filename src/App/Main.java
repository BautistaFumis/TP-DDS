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
        Scanner scanner = new Scanner(System.in);
        GestorUsuario gestorUsuario = new GestorUsuario();
        HuespedDAO huespedDAO = new HuespedDAOImpl();
        GestorHuesped gestorHuesped = new GestorHuesped(huespedDAO);
        boolean autenticado = false;

        System.out.println("========================================");
        System.out.println(" BIENVENIDO AL SISTEMA DE GESTIÓN HOTELERA");
        System.out.println("========================================");


        while (!autenticado) {
            try {
                System.out.println("\nPor favor, inicie sesión:");
                System.out.print("Usuario: ");
                String id = scanner.nextLine();

                System.out.print("Contraseña: "); // Debe ser oculta... Verlo para entrega
                String password = scanner.nextLine();

                gestorUsuario.autenticar(id, password); // Si esto falla, capturamos la excepcion
                autenticado = true;
                System.out.println("\n¡Inicio de sesión exitoso! Bienvenido, " + id + ".");

            } catch (CredencialesInvalidasException e) {
                System.err.println("Error: " + e.getMessage());
                System.out.println("Por favor, intente de nuevo.");
            }
        }

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
    // faltan datos del huesped, ver el dominio
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

            // ver de usar ENUM
            System.out.print("(*) Tipo de Documento (DNI, PASAPORTE): ");
            huespedParaAlta.setTipoDocumento(scanner.nextLine());

            try {
                System.out.print("(*) Número de Documento: ");
                huespedParaAlta.setDocumento(scanner.nextLine());

                System.out.print("Teléfono: ");
                huespedParaAlta.setTelefono(Long.parseLong(scanner.nextLine()));

                // La dirección se crea vacía por ahora.
                huespedParaAlta.setDireccion(new Direccion());

                gestor.registrarNuevoHuesped(huespedParaAlta);

                System.out.println("\nÉXITO: El huésped '" + huespedParaAlta.getNombre() + " " + huespedParaAlta.getApellido() + "' ha sido satisfactoriamente cargado al sistema.");

            } catch (NumberFormatException e) {
                System.err.println("\nERROR: El documento y el teléfono deben ser números válidos.");
            } catch (CamposObligatoriosException e) { //ver si funciona y volver a ingresar datos
                System.err.println("\nERROR: " + e.getMessage());
            } catch (DocumentoDuplicadoException e) {
                System.err.println("\n ADVERTENCIA: " + e.getMessage());
                System.out.print("¿Desea aceptarlo igualmente? [1] ACEPTAR IGUALMENTE / [2] CORREGIR: ");
                String opcion = scanner.nextLine();
                if ("1".equals(opcion)) {
                    gestor.registrarHuespedAceptandoDuplicado(huespedParaAlta);
                    System.out.println("ÉXITO: Se ha registrado el huésped duplicado.");
                } else {
                    //aca hay que volver a el ingresa con foco en tipo de documento
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
        System.out.print("Tipo de Documento (DNI, LE, LC, PASAPORTE, Otro): ");
        String tipoDocumento = scanner.nextLine();
        System.out.print("Numero de Documento: ");
        String documento = scanner.nextLine();

        List<Huesped> resultados = gestor.buscarHuespedes(apellido, nombre, tipoDocumento , documento);

        if (resultados.isEmpty()) {
            // aca dar la opcion de poner 1 si queres agregarlo y 2 si no queres agregarlo
            System.out.println("\nNo se encontró ninguna concordancia.");
            System.out.println("Redirigiendo al alta de huésped...");
            ejecutarAltaHuesped(scanner, gestor);
            return;
        }

        System.out.println("\n--- RESULTADOS DE LA BÚSQUEDA ---");
        for (int i = 0; i < resultados.size(); i++) {
            Huesped h = resultados.get(i);
            System.out.printf("%d. %s, %s - %s: %s\n", (i + 1), h.getApellido(), h.getNombre(), h.getTipoDocumento(), h.getDocumento());
        }

        System.out.print("\nSeleccione un huésped por su número o presione [Enter] para crear uno nuevo: ");
        String seleccion = scanner.nextLine();

        if (seleccion.isEmpty()) {
            System.out.println("\nNo se seleccionó un huésped existente.");
            System.out.println("Redirigiendo al alta de huésped...");
            ejecutarAltaHuesped(scanner, gestor);
            return;
        }
// hacer un loop hasta que ingrese un numero valido
        try {
            int indice = Integer.parseInt(seleccion) - 1;
            if (indice >= 0 && indice < resultados.size()) {
                Huesped huespedSeleccionado = resultados.get(indice);
                System.out.println("\nHuésped seleccionado: " + huespedSeleccionado.getNombre() + " " + huespedSeleccionado.getApellido());
                System.out.println("Ejecutando CU10: Modificar Huésped (lógica no implementada)...");
                // Aca va la llamada al metodo: ejecutarModificarHuesped(scanner, gestor, huespedSeleccionado);
            } else {
                System.err.println("Número de selección inválido.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Entrada inválida. Por favor, ingrese un número.");
        }
    }
}