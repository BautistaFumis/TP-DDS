package Logica.Gestores;

import Logica.Dominio.Usuario;
import Logica.Excepciones.CredencialesInvalidasException;
import Persistencia.DAOFactory;
import Persistencia.UsuarioDAO;

import java.util.Optional;

/**
 * Gestiona la lógica de negocio para las operaciones de autenticación de usuarios.
 * Esta clase se encarga de verificar las credenciales proporcionadas por la interfaz de usuario
 * contra la capa de persistencia.
 */

public class GestorUsuario {

    private final UsuarioDAO usuarioDAO;

    /**
     * Constructor por defecto.
     * Inicializa el gestor y crea una instancia concreta del DAO de usuarios (`UsuarioDAOImpl`).
     */

    public GestorUsuario(DAOFactory factory) {
        this.usuarioDAO = factory.crearUsuarioDAO();
    }

    /**
     * Autentica a un usuario verificando su id y contraseña.
     * Utiliza el DAO para buscar al usuario por su ID y luego compara la contraseña proporcionada
     * con la almacenada.
     *
     * @param id El id (nombre de usuario) ingresado por el usuario.
     * @param password La contraseña ingresada por el usuario.
     * @throws CredencialesInvalidasException si el usuario no se encuentra en la persistencia o si la contraseña es incorrecta.
     */
    public void autenticar(String id, String password) throws CredencialesInvalidasException {
        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorId(id);

        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getPassword().equals(password)) {
            throw new CredencialesInvalidasException("El usuario o la contraseña no son válidos");
        }
    }
}