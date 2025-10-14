package Logica.Gestores;

import Logica.Dominio.Usuario;
import Logica.Excepciones.CredencialesInvalidasException;
import Logica.Excepciones.PasswordInvalidaException;
import Persistencia.UsuarioDAO;
import Persistencia.UsuarioDAOImpl;

import java.util.Optional;

public class GestorUsuario {

    private UsuarioDAO usuarioDAO;

    public GestorUsuario() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    /**
     * Autentica a un usuario verificando su id y contraseña.
     * @param id El id (nombre de usuario) ingresado.
     * @param password La contraseña ingresada.
     * @throws CredencialesInvalidasException si el usuario no existe o la contraseña es incorrecta.
     */
    public void autenticar(String id, String password) throws CredencialesInvalidasException {
        // Buscamos al usuario usando el DAO
        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorId(id);

        // Verificamos si el usuario existe y si la contraseña coincide.
        // Si el Optional está vacío o la contraseña no es igual, lanzamos la excepción.
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getPassword().equals(password)) {
            throw new CredencialesInvalidasException("El usuario o la contraseña no son válidos");
        }

        // Si no se lanza ninguna excepción, la autenticación es exitosa.
    }

    /**
     * Valida que una contraseña cumpla con las reglas de negocio.
     * (Esta función será más útil para los CU de alta o modificación de usuario)
     * @param password La contraseña a validar.
     * @throws PasswordInvalidaException si la contraseña no cumple el formato.
     */
    public void validarFormatoPassword(String password) throws PasswordInvalidaException {
        long letras = password.chars().filter(Character::isLetter).count();
        long numeros = password.chars().filter(Character::isDigit).count();

        // Requisito: Al menos 5 letras y 3 números.
        if (letras < 5 || numeros < 3) {
            throw new PasswordInvalidaException("La contraseña debe tener al menos 5 letras y 3 números.");
        }
        // Aquí se podrían agregar las validaciones más complejas de números no consecutivos.
    }
}