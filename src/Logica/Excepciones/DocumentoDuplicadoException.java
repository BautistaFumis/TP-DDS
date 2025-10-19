package Logica.Excepciones;

/**
 * Excepción que se lanza al intentar registrar un huésped cuyo tipo y número de documento
 * ya existen en el sistema.
 * Esta es una excepción chequeada (checked exception), diseñada para ser manejada explícitamente,
 * permitiendo a la interfaz de usuario decidir si se debe corregir la entrada o
 * permitir el registro duplicado.
 *
 */
public class DocumentoDuplicadoException extends Exception {

    /**
     * Construye una nueva `DocumentoDuplicadoException` con el mensaje de detalle especificado.
     *
     * @param message El mensaje de detalle, que informa al usuario sobre la duplicidad
     */
    public DocumentoDuplicadoException(String message) {
        super(message);
    }
}