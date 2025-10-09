import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // --- 1. Inicialización de componentes ---
        // Se instancian los objetos necesarios para que la aplicación funcione.
        HuespedDAO dao = new HuespedDAOImpl();
        GestorHuesped gestor = new GestorHuesped(dao);
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        System.out.println("--- BIENVENIDO AL SISTEMA DE GESTIÓN DE HUÉSPEDES ---");

        // --- 2. Bucle principal de la aplicación ---
        while (continuar) {
            System.out.println("\n--- ALTA DE NUEVO HUÉSPED ---");
            System.out.println("Por favor, ingrese los datos solicitados. Los campos con (*) son obligatorios.");

            // --- 3. Recolección de datos del usuario ---
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

                // La dirección se crea vacía por ahora, se podrían pedir los datos aquí también.
                huespedParaAlta.setDireccion(new Direccion());

                // --- 4. Intento de registrar al huésped ---
                gestor.registrarNuevoHuesped(huespedParaAlta);

                // Si llegamos aquí, fue un éxito
                System.out.println("\nÉXITO: El huésped '" + huespedParaAlta.getNombre() + " " + huespedParaAlta.getApellido() + "' ha sido satisfactoriamente cargado al sistema.");

            } catch (NumberFormatException e) {
                System.err.println("\nERROR: El documento y el teléfono deben ser números válidos.");
            } catch (CamposObligatoriosException e) {
                // Flujo Alternativo 2.A: Faltan datos obligatorios
                System.err.println("\nERROR: " + e.getMessage());
            } catch (DocumentoDuplicadoException e) {
                // Flujo Alternativo 2.B: El documento ya existe
                System.err.println("\n⚠ADVERTENCIA: " + e.getMessage());
                System.out.print("¿Desea aceptarlo igualmente? [1] ACEPTAR IGUALMENTE / [2] CORREGIR: ");
                String opcion = scanner.nextLine();
                if ("1".equals(opcion)) {
                    // Flujo Alternativo 2.B.2.1: Se acepta el duplicado
                    gestor.registrarHuespedAceptandoDuplicado(huespedParaAlta);
                    System.out.println("\n✅ ÉXITO: Se ha registrado el huésped duplicado.");
                } else {
                    // Flujo Alternativo 2.B.2.2: Se corrige
                    System.out.println("Operación cancelada. Por favor, ingrese los datos nuevamente.");
                }
            }

            // 5. En este paso, le vamos a preguntar al usuario si desea continuar
            System.out.print("\n¿Desea cargar otro huésped? (SI/NO): ");
            if (!scanner.nextLine().equalsIgnoreCase("SI")) {
                continuar = false;
            }
        }

        System.out.println("\n--- FIN DEL PROGRAMA ---");
        scanner.close();
    }
}