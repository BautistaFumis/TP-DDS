package Logica.Excepciones;

/**
 * Excepción que se lanza cuando una operación no puede completarse porque
 * uno o más campos obligatorios no fueron proporcionados.
 * Esta es una excepción chequeada (checked exception), lo que obliga a que sea
 * declarada en la firma de los métodos que pueden lanzarla o manejada
 * explícitamente con un bloque try-catch.
 *
 */
public class CamposObligatoriosException extends Exception {

    /**
     * Construye una nueva `CamposObligatoriosException` con el mensaje de detalle especificado.
     *
     * @param message El mensaje de detalle. El mensaje se guarda para su posterior
     */
    public CamposObligatoriosException(String message) {
        super(message);
    }
}