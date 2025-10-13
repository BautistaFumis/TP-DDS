package Excepciones;

public class CredencialesInvalidasException extends Exception {
    public CredencialesInvalidasException(String message) {
        super(message);
    }
}