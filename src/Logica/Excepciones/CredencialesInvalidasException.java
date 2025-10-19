package Logica.Excepciones;

/**
 * Excepción que se lanza cuando un intento de autenticación falla debido a que
 * el nombre de usuario no existe o la contraseña proporcionada es incorrecta.
 * Al ser una excepción chequeada (checked exception), obliga a los métodos que la lanzan
 * a declararla en su firma, promoviendo un manejo explícito de los errores de inicio de sesión.
 *
 */
public class CredencialesInvalidasException extends Exception {

    /**
     * Construye una nueva `CredencialesInvalidasException` con el mensaje de detalle especificado.
     *
     * @param message El mensaje de detalle que explica la razón del fallo. Este mensaje
     */
    public CredencialesInvalidasException(String message) {
        super(message);
    }
}