package Excepciones;

public class PasswordInvalidaException extends Exception {
    public PasswordInvalidaException(String message) {
        super(message);
    }
}