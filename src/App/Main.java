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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

            try {
                System.out.print("(*) Apellido: ");
                huespedParaAlta.setApellido(scanner.nextLine());
                System.out.print("(*) Nombre: ");
                huespedParaAlta.setNombre(scanner.nextLine());
                // ver de usar ENUM
                System.out.print("(*) Tipo de Documento (DNI, LE, LC, PASAPORTE, Otro): ");
                huespedParaAlta.setTipoDocumento(scanner.nextLine());
                System.out.print("(*) Número de Documento: ");
                huespedParaAlta.setDocumento(scanner.nextLine());
                System.out.print("CUIT: ");
                huespedParaAlta.setCuit(scanner.nextLine());
                System.out.print("Posicion frente al IVA: ");
                huespedParaAlta.setCategoriaIVA(scanner.nextLine());
                DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                System.out.print("(*) Fecha de Nacimiento (formato dd/mm/aaaa): ");
                String fechaTexto = scanner.nextLine();
                LocalDate fechaNacimiento = LocalDate.parse(fechaTexto, formatoFecha);
                huespedParaAlta.setFechaNacimiento(fechaNacimiento);
                System.out.print("Direccion: \n");
                huespedParaAlta.setDireccion(new Direccion());
                System.out.print("(*) Calle: ");
                huespedParaAlta.getDireccion().setCalle(scanner.nextLine());
                System.out.print("(*) Numero: ");
                huespedParaAlta.getDireccion().setNumero(Integer.parseInt(scanner.nextLine()));
                System.out.print("Departamento (Letra o Numero): ");
                huespedParaAlta.getDireccion().setDepartamento(scanner.nextLine());
                System.out.print("Piso (Numero): ");
                huespedParaAlta.getDireccion().setPiso(scanner.nextLine());
                System.out.print("(*) Codigo Postal: ");
                huespedParaAlta.getDireccion().setCodigoPostal(Integer.parseInt(scanner.nextLine()));
                System.out.print("(*) Localidad: ");
                huespedParaAlta.getDireccion().setLocalidad(scanner.nextLine());
                System.out.print("(*) Provincia: ");
                huespedParaAlta.getDireccion().setProvincia(scanner.nextLine());
                System.out.print("(*) Pais: ");
                huespedParaAlta.getDireccion().setPais(scanner.nextLine());
                System.out.print("Teléfono: ");
                huespedParaAlta.setTelefono(scanner.nextLine());
                System.out.print("Email: ");
                huespedParaAlta.setEmail(scanner.nextLine());
                System.out.print("Ocupacion: ");
                huespedParaAlta.setOcupacion(scanner.nextLine());
                System.out.print("Nacionalidad: ");
                huespedParaAlta.setNacionalidad(scanner.nextLine());

                gestor.registrarNuevoHuesped(huespedParaAlta);

                System.out.println("\nÉXITO: El huésped '" + huespedParaAlta.getNombre() + " " + huespedParaAlta.getApellido() + "' ha sido satisfactoriamente cargado al sistema.");

            } catch (DateTimeParseException e) {
                System.err.println("\nERROR: El formato de la fecha es incorrecto. Debe ser dd/mm/aaaa.");
            } catch (NumberFormatException e) {
                System.err.println("\nERROR: Uno de los campos numéricos (Número, Piso, CP, Teléfono) tiene un formato inválido.");
            } catch (CamposObligatoriosException e) {
                System.err.println("\nERROR: " + e.getMessage() + " Por favor, intente de nuevo.");
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

        System.out.print("\nSi desea modificarlo, seleccione un huésped por su número o caso contrario presione [Enter] para crear uno nuevo: ");
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
                System.out.println("Ejecutando CU10: Modificar Huésped");
                ejecutarModificarHuesped(scanner, gestor, huespedSeleccionado);
            } else {
                System.err.println("Número de selección inválido.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Entrada inválida. Por favor, ingrese un número.");
        }
    }

    private static void ejecutarModificarHuesped(Scanner scanner, GestorHuesped gestor, Huesped huespedOriginal) {
        System.out.println("\n--- MODIFICACIÓN DE HUÉSPED (CU10) ---");
        System.out.println("Modifique los campos que desee. Presione [Enter] para conservar el valor actual.");

        Huesped huespedModificado = new Huesped(huespedOriginal);
        Direccion direccionModificada = huespedModificado.getDireccion();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            // --- Datos Personales ---
            System.out.printf("Apellido (*): [%s] > ", huespedOriginal.getApellido());
            String nuevoApellido = scanner.nextLine();
            if (!nuevoApellido.isEmpty()) huespedModificado.setApellido(nuevoApellido);

            System.out.printf("Nombre (*): [%s] > ", huespedOriginal.getNombre());
            String nuevoNombre = scanner.nextLine();
            if (!nuevoNombre.isEmpty()) huespedModificado.setNombre(nuevoNombre);

            System.out.printf("Tipo de Documento (*): [%s] > ", huespedOriginal.getTipoDocumento());
            String nuevoTipoDoc = scanner.nextLine();
            if (!nuevoTipoDoc.isEmpty()) huespedModificado.setTipoDocumento(nuevoTipoDoc);

            System.out.printf("Número de Documento (*): [%s] > ", huespedOriginal.getDocumento());
            String nuevoNumDoc = scanner.nextLine();
            if (!nuevoNumDoc.isEmpty()) huespedModificado.setDocumento(nuevoNumDoc);

            System.out.printf("Fecha de Nacimiento (*, dd/mm/aaaa): [%s] > ", huespedOriginal.getFechaNacimiento().format(formatoFecha));
            String nuevaFechaStr = scanner.nextLine();
            if (!nuevaFechaStr.isEmpty()) {
                huespedModificado.setFechaNacimiento(LocalDate.parse(nuevaFechaStr, formatoFecha));
            }

            // --- Datos Fiscales y de Contacto ---
            System.out.printf("CUIT: [%s] > ", huespedOriginal.getCuit());
            String nuevoCuit = scanner.nextLine();
            if (!nuevoCuit.isEmpty()) huespedModificado.setCuit(nuevoCuit);

            System.out.printf("Posicion frente al IVA: [%s] > ", huespedOriginal.getCategoriaIVA());
            String nuevaPosIva = scanner.nextLine();
            if (!nuevaPosIva.isEmpty()) huespedModificado.setCategoriaIVA(nuevaPosIva);

            System.out.printf("Teléfono: [%s] > ", huespedOriginal.getTelefono());
            String nuevoTelefonoStr = scanner.nextLine();
            if (!nuevoTelefonoStr.isEmpty()) huespedModificado.setTelefono(nuevoTelefonoStr);

            System.out.printf("Email: [%s] > ", huespedOriginal.getEmail());
            String nuevoEmail = scanner.nextLine();
            if (!nuevoEmail.isEmpty()) huespedModificado.setEmail(nuevoEmail);

            // --- Dirección ---
            System.out.println("--- Dirección ---");
            System.out.printf("Calle (*): [%s] > ", huespedOriginal.getDireccion().getCalle());
            String nuevaCalle = scanner.nextLine();
            if (!nuevaCalle.isEmpty()) direccionModificada.setCalle(nuevaCalle);

            System.out.printf("Numero (*): [%d] > ", huespedOriginal.getDireccion().getNumero());
            String nuevoNumeroStr = scanner.nextLine();
            if (!nuevoNumeroStr.isEmpty()) direccionModificada.setNumero(Integer.parseInt(nuevoNumeroStr));

            // Maneja el caso de que el depto/piso sean null en el original
            String deptoOriginal = huespedOriginal.getDireccion().getDepartamento() != null ? huespedOriginal.getDireccion().getDepartamento() : "";
            System.out.printf("Departamento: [%s] > ", deptoOriginal);
            String nuevoDepto = scanner.nextLine();
            if (!nuevoDepto.isEmpty()) direccionModificada.setDepartamento(nuevoDepto);

            String pisoOriginal = huespedOriginal.getDireccion().getPiso() != null ? huespedOriginal.getDireccion().getPiso() : "";
            System.out.printf("Piso: [%s] > ", pisoOriginal);
            String nuevoPisoStr = scanner.nextLine();
            if (!nuevoPisoStr.isEmpty()) {
                direccionModificada.setPiso(nuevoPisoStr);
            } else if (pisoOriginal.isEmpty() && nuevoPisoStr.isEmpty()) {
                // Si era null y sigue vacío, se mantiene null
                direccionModificada.setPiso(null);
            }


            System.out.printf("Codigo Postal (*): [%d] > ", huespedOriginal.getDireccion().getCodigoPostal());
            String nuevoCpStr = scanner.nextLine();
            if (!nuevoCpStr.isEmpty()) direccionModificada.setCodigoPostal(Integer.parseInt(nuevoCpStr));

            System.out.printf("Localidad (*): [%s] > ", huespedOriginal.getDireccion().getLocalidad());
            String nuevaLocalidad = scanner.nextLine();
            if (!nuevaLocalidad.isEmpty()) direccionModificada.setLocalidad(nuevaLocalidad);

            System.out.printf("Provincia (*): [%s] > ", huespedOriginal.getDireccion().getProvincia());
            String nuevaProvincia = scanner.nextLine();
            if (!nuevaProvincia.isEmpty()) direccionModificada.setProvincia(nuevaProvincia);

            System.out.printf("Pais (*): [%s] > ", huespedOriginal.getDireccion().getPais());
            String nuevoPais = scanner.nextLine();
            if (!nuevoPais.isEmpty()) direccionModificada.setPais(nuevoPais);

            // --- Otros Datos ---
            System.out.printf("Ocupacion: [%s] > ", huespedOriginal.getOcupacion());
            String nuevaOcupacion = scanner.nextLine();
            if (!nuevaOcupacion.isEmpty()) huespedModificado.setOcupacion(nuevaOcupacion);

            System.out.printf("Nacionalidad: [%s] > ", huespedOriginal.getNacionalidad());
            String nuevaNacionalidad = scanner.nextLine();
            if (!nuevaNacionalidad.isEmpty()) huespedModificado.setNacionalidad(nuevaNacionalidad);

            // Al final, presenta las opciones al usuario
            System.out.print("\nAcciones: [1] GUARDAR CAMBIOS / [2] CANCELAR / [3] BORRAR HUÉSPED > ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    gestor.modificarHuesped(huespedModificado);
                    System.out.println("\n ÉXITO: Los datos del huésped han sido actualizados.");
                    break;
                case "2":
                    System.out.println("\nOperación cancelada. No se guardaron los cambios.");
                    break;
                case "3":
                    System.out.println("\nEjecutando CU11: Dar baja de Huésped ");
                    // Aquí llamarías a: ejecutarBajaHuesped(scanner, gestor, huespedOriginal);
                    break;
                default:
                    System.out.println("Opción no válida. Cambios descartados.");
                    break;
            }
        } catch (Exception e) {
            System.err.println("\n ERROR: " + e.getMessage());
        }
    }
}

