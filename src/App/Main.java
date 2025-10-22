package App;

import Logica.Dominio.Direccion;
import Logica.Dominio.Huesped;
import Logica.Excepciones.CamposObligatoriosException;
import Logica.Excepciones.CredencialesInvalidasException;
import Logica.Excepciones.DocumentoDuplicadoException;
import Logica.Gestores.GestorHuesped;
import Logica.Gestores.GestorUsuario;
import Persistencia.EstadiaDAO;
import Persistencia.EstadiaDAOImpl;
import Persistencia.HuespedDAO;
import Persistencia.HuespedDAOImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal de la aplicación de gestión hotelera.
 * Contiene el punto de entrada (main) y se encarga de manejar el flujo de la aplicación,
 * la interacción con el usuario a través de la consola y la orquestación de los casos de uso.
 */
public class Main {

    /**
     * Punto de entrada principal de la aplicación.
     * Gestiona la autenticación inicial del usuario y muestra el menú principal de casos de uso.
     *
     * @param args Argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GestorUsuario gestorUsuario = new GestorUsuario();
        HuespedDAO huespedDAO = new HuespedDAOImpl();
        EstadiaDAO estadiaDAO = new EstadiaDAOImpl();
        GestorHuesped gestorHuesped = new GestorHuesped(huespedDAO, estadiaDAO); // Inyección de dependencias corregida
        boolean autenticado = false;

        System.out.println("========================================");
        System.out.println(" BIENVENIDO AL SISTEMA DE GESTIÓN HOTELERA");
        System.out.println("========================================");

        // --- 2. Bucle de Autenticación ---
        while (!autenticado) {
            try {
                System.out.println("\nPor favor, inicie sesión:");
                System.out.print("Usuario: ");
                String id = scanner.nextLine();
                System.out.print("Contraseña: ");
                String password = scanner.nextLine();

                gestorUsuario.autenticar(id, password);
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
            System.out.println("2. CU02 - Buscar Huésped (Permite Modificar y Eliminar)");
            System.out.println("9. CU09 - Dar de alta Huésped");
            System.out.println("0. Salir");
            System.out.print("Opción: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                option = -1;
            }

            switch (option) {
                case 1:
                    System.out.println("Ya se encuentra autenticado.");
                    break;
                case 2:
                    ejecutarBusquedaHuesped(scanner, gestorHuesped);
                    break;
                case 9:
                    ejecutarAltaHuesped(scanner, gestorHuesped);
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
     * Encapsula la lógica para el Caso de Uso 09: Dar de alta un nuevo Huésped.
     * Solicita todos los datos al usuario por consola y maneja las validaciones y errores.
     *
     * @param scanner El objeto Scanner para leer la entrada del usuario.
     * @param gestor El GestorHuesped que maneja la lógica de negocio.
     */
    private static void ejecutarAltaHuesped(Scanner scanner, GestorHuesped gestor) {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n--- ALTA DE NUEVO HUÉSPED (CU09) ---");
            System.out.println("Por favor, ingrese los datos solicitados. Los campos con (*) son obligatorios.");
            Huesped huespedParaAlta = new Huesped();
            Direccion direccion = new Direccion();

            try {
                System.out.print("(*) Apellido: ");
                huespedParaAlta.setApellido(scanner.nextLine());
                System.out.print("(*) Nombre: ");
                huespedParaAlta.setNombre(scanner.nextLine());
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
                if(fechaTexto != null && !fechaTexto.trim().isEmpty()) { // Validar fecha no vacía
                    huespedParaAlta.setFechaNacimiento(LocalDate.parse(fechaTexto, formatoFecha));
                } else {
                    huespedParaAlta.setFechaNacimiento(null); // O lanzar error si es obligatorio
                }


                System.out.println("--- Dirección ---");
                System.out.print("(*) Calle: ");
                direccion.setCalle(scanner.nextLine());
                System.out.print("(*) Numero: ");
                String numeroStr = scanner.nextLine();
                if(numeroStr != null && !numeroStr.trim().isEmpty()) {
                    direccion.setNumero(Integer.parseInt(numeroStr.trim()));
                } else {
                    direccion.setNumero(null);
                }

                System.out.print("Departamento (Letra o Numero): ");
                direccion.setDepartamento(scanner.nextLine());
                System.out.print("Piso (Numero): ");
                String pisoStr = scanner.nextLine();
                if (pisoStr != null && !pisoStr.trim().isEmpty()) {
                    direccion.setPiso(Integer.parseInt(pisoStr.trim()));
                } else {
                    direccion.setPiso(null);
                }
                System.out.print("(*) Codigo Postal: ");
                direccion.setCodigoPostal(scanner.nextLine());
                System.out.print("(*) Localidad: ");
                direccion.setLocalidad(scanner.nextLine());
                System.out.print("(*) Provincia: ");
                direccion.setProvincia(scanner.nextLine());
                System.out.print("(*) Pais: ");
                direccion.setPais(scanner.nextLine());
                huespedParaAlta.setDireccion(direccion);

                System.out.print("Teléfono: ");
                huespedParaAlta.setTelefono(scanner.nextLine());
                System.out.print("Email: ");
                huespedParaAlta.setEmail(scanner.nextLine());
                System.out.print("Ocupacion: ");
                huespedParaAlta.setOcupacion(scanner.nextLine());
                System.out.print("Nacionalidad: ");
                huespedParaAlta.setNacionalidad(scanner.nextLine());

                gestor.registrarNuevoHuesped(huespedParaAlta);

                System.out.println("\n ÉXITO: El huésped '" + huespedParaAlta.getNombre() + " " + huespedParaAlta.getApellido() + "' ha sido satisfactoriamente cargado al sistema.");

            } catch (DateTimeParseException e) {
                System.err.println("\n ERROR: El formato de la fecha es incorrecto. Debe ser dd/mm/aaaa.");
            } catch (NumberFormatException e) {
                System.err.println("\n ERROR: Uno de los campos numéricos (Número, Piso, CP) tiene un formato inválido.");
            } catch (CamposObligatoriosException e) {
                System.err.println("\n ERROR: " + e.getMessage() + " Por favor, intente de nuevo.");
            } catch (DocumentoDuplicadoException e) {
                System.err.println("\n ADVERTENCIA: " + e.getMessage());
                System.out.print("¿Desea aceptarlo igualmente? [1] ACEPTAR IGUALMENTE / [2] CORREGIR: ");
                String opcion = scanner.nextLine();
                if ("1".equals(opcion)) {
                    gestor.registrarHuespedAceptandoDuplicado(huespedParaAlta);
                    System.out.println(" ÉXITO: Se ha registrado el huésped duplicado.");
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

    /**
     * Encapsula la lógica para el Caso de Uso 02: Buscar un Huésped.
     * Permite buscar por múltiples criterios y, si se encuentra un resultado,
     * deriva a los casos de uso de Modificación (CU10) o Alta (CU09).
     *
     * @param scanner El objeto Scanner para leer la entrada del usuario.
     * @param gestor El GestorHuesped que maneja la lógica de negocio.
     */
    private static void ejecutarBusquedaHuesped(Scanner scanner, GestorHuesped gestor) {
        System.out.println("\n--- BÚSQUEDA DE HUÉSPED (CU02) ---");
        System.out.println("Ingrese los criterios de búsqueda (deje en blanco para omitir).");

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Tipo de Documento: ");
        String tipoDocumento = scanner.nextLine();
        System.out.print("Numero de Documento: ");
        String documento = scanner.nextLine();

        List<Huesped> resultados = gestor.buscarHuespedes(apellido, nombre, tipoDocumento , documento);

        if (resultados.isEmpty()) {
            System.out.println("\nNo se encontró ninguna concordancia.");
            System.out.print("¿Desea registrar un nuevo huésped? [SI/NO]: ");
            if (scanner.nextLine().equalsIgnoreCase("SI")) {
                System.out.println("Redirigiendo al alta de huésped...");
                ejecutarAltaHuesped(scanner, gestor);
            }
            return;
        }

        System.out.println("\n--- RESULTADOS DE LA BÚSQUEDA ---");
        for (int i = 0; i < resultados.size(); i++) {
            Huesped h = resultados.get(i);
            System.out.printf("%d. %s, %s - %s: %s\n", (i + 1), h.getApellido(), h.getNombre(), h.getTipoDocumento(), h.getDocumento());
        }

        while (true) {
            System.out.print("\nSeleccione un huésped por su número para modificarlo, o presione [Enter] para crear uno nuevo: ");
            String seleccion = scanner.nextLine();

            if (seleccion.isEmpty()) {
                System.out.println("Redirigiendo al alta de huésped...");
                ejecutarAltaHuesped(scanner, gestor);
                return;
            }

            try {
                int indice = Integer.parseInt(seleccion) - 1;
                if (indice >= 0 && indice < resultados.size()) {
                    Huesped huespedSeleccionado = resultados.get(indice);
                    ejecutarModificarHuesped(scanner, gestor, huespedSeleccionado);
                    return;
                } else {
                    System.err.println("Número de selección inválido. Por favor, intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Entrada inválida. Por favor, ingrese un número o presione [Enter].");
            }
        }
    }

    /**
     * Encapsula la lógica para el Caso de Uso 10: Modificar un Huésped existente.
     * Muestra los datos actuales y permite al usuario actualizarlos, cancelarlos o eliminar el huésped.
     *
     * @param scanner El objeto Scanner para leer la entrada del usuario.
     * @param gestor El GestorHuesped que maneja la lógica de negocio.
     * @param huespedOriginal El objeto Huesped seleccionado en la búsqueda que se va a modificar.
     */
    private static void ejecutarModificarHuesped(Scanner scanner, GestorHuesped gestor, Huesped huespedOriginal) {
        System.out.println("\n--- MODIFICACIÓN DE HUÉSPED (CU10) ---");
        System.out.println("Modifique los campos que desee. Presione [Enter] para conservar el valor actual.");

        Huesped huespedModificado = new Huesped(huespedOriginal);
        Direccion direccionModificada = huespedModificado.getDireccion();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
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

            System.out.println("--- Dirección ---");
            System.out.printf("Calle (*): [%s] > ", huespedOriginal.getDireccion().getCalle());
            String nuevaCalle = scanner.nextLine();
            if (!nuevaCalle.isEmpty()) direccionModificada.setCalle(nuevaCalle);

            System.out.printf("Numero (*): [%d] > ", huespedOriginal.getDireccion().getNumero());
            String nuevoNumeroStr = scanner.nextLine();
            if (!nuevoNumeroStr.isEmpty()) direccionModificada.setNumero(Integer.parseInt(nuevoNumeroStr));

            String deptoOriginal = huespedOriginal.getDireccion().getDepartamento() != null ? huespedOriginal.getDireccion().getDepartamento() : "";
            System.out.printf("Departamento: [%s] > ", deptoOriginal);
            String nuevoDepto = scanner.nextLine();
            if (!nuevoDepto.isEmpty()) direccionModificada.setDepartamento(nuevoDepto);

            if (huespedOriginal.getDireccion().getPiso() == null)  System.out.print("Piso : [] > ");
            else {
            System.out.printf("Piso : [%d] > ", huespedOriginal.getDireccion().getPiso());
            }
            String nuevoPisoStr = scanner.nextLine();
            if (!nuevoPisoStr.isEmpty()) direccionModificada.setPiso(Integer.parseInt(nuevoPisoStr));

            System.out.printf("Codigo Postal (*): [%s] > ", huespedOriginal.getDireccion().getCodigoPostal());
            String nuevoCpStr = scanner.nextLine();
            if (!nuevoCpStr.isEmpty()) direccionModificada.setCodigoPostal(nuevoCpStr);

            System.out.printf("Localidad (*): [%s] > ", huespedOriginal.getDireccion().getLocalidad());
            String nuevaLocalidad = scanner.nextLine();
            if (!nuevaLocalidad.isEmpty()) direccionModificada.setLocalidad(nuevaLocalidad);

            System.out.printf("Provincia (*): [%s] > ", huespedOriginal.getDireccion().getProvincia());
            String nuevaProvincia = scanner.nextLine();
            if (!nuevaProvincia.isEmpty()) direccionModificada.setProvincia(nuevaProvincia);

            System.out.printf("Pais (*): [%s] > ", huespedOriginal.getDireccion().getPais());
            String nuevoPais = scanner.nextLine();
            if (!nuevoPais.isEmpty()) direccionModificada.setPais(nuevoPais);

            System.out.printf("Ocupacion: [%s] > ", huespedOriginal.getOcupacion());
            String nuevaOcupacion = scanner.nextLine();
            if (!nuevaOcupacion.isEmpty()) huespedModificado.setOcupacion(nuevaOcupacion);

            System.out.printf("Nacionalidad: [%s] > ", huespedOriginal.getNacionalidad());
            String nuevaNacionalidad = scanner.nextLine();
            if (!nuevaNacionalidad.isEmpty()) huespedModificado.setNacionalidad(nuevaNacionalidad);
            boolean valorincorrecto = true;
            while(valorincorrecto) {
            System.out.print("\nAcciones: [1] GUARDAR CAMBIOS / [2] CANCELAR / [3] BORRAR HUÉSPED / [4] DESCARTAR CAMBIOS > ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    gestor.modificarHuesped(huespedModificado);
                    System.out.println("\n ÉXITO: Los datos del huésped han sido actualizados.");
                    valorincorrecto = false;
                    break;
                case "2":
                    System.out.println("\nOperación cancelada. No se guardaron los cambios.");
                    valorincorrecto = false;
                    break;
                case "3":
                    System.out.println("\nEjecutando CU11: Dar baja de Huésped ");
                    ejecutarBajaHuesped(scanner, gestor, huespedOriginal);
                    valorincorrecto = false;
                    break;
                case "4":
                    System.out.println("Cambios descartados.");
                    valorincorrecto = false;
                    break;
                default:
                    System.out.println("Opción no válida. Ingrese de nuevo.");
                    break;
            }
            }
        } catch (Exception e) {
            System.err.println("\n ERROR: " + e.getMessage());
        }
    }

    /**
     * Encapsula la lógica para el Caso de Uso 11: Dar de baja un Huésped.
     * Verifica si el huésped puede ser eliminado y pide confirmación al usuario.
     *
     * @param scanner El objeto Scanner para leer la confirmación del usuario.
     * @param gestor El GestorHuesped que maneja la lógica de negocio.
     * @param huesped El huésped que se desea eliminar.
     */
    public static void ejecutarBajaHuesped(Scanner scanner, GestorHuesped gestor, Huesped huesped) {
        System.out.println("\n--- BAJA DE HUÉSPED (CU11) ---");

        if (!gestor.darDeBajaHuesped(huesped)) {
            System.out.println("El huésped no puede ser eliminado pues se ha alojado en el Hotel en alguna oportunidad.");
            System.out.print("PRESIONE [Enter] PARA CONTINUAR...");
            scanner.nextLine();
            return;
        }

        System.out.printf("Los datos del huésped '%s, %s' serán eliminados del sistema.\n",
                huesped.getApellido(), huesped.getNombre());
        System.out.print("¿Está seguro? [1] ELIMINAR / [2] CANCELAR > ");
        String opcion = scanner.nextLine();

        if (opcion.equals("1")) {
            gestor.darDeBajaHuesped(huesped);
            System.out.println("\nEl huésped ha sido eliminado del sistema.");
        } else {
            System.out.println("\nOperación cancelada.");
        }
        System.out.print("PRESIONE [Enter] PARA CONTINUAR...");
        scanner.nextLine();
    }
}