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
        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorId(id);
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getPassword().equals(password)) {
            throw new CredencialesInvalidasException("El usuario o la contraseña no son válidos");
        }
    }


}