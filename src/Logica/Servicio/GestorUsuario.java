package Logica.Servicio;

import Logica.Dominio.Entidades.Usuario;
import Logica.Excepciones.CredencialesInvalidasException;

// <-- NUEVO: Importamos el Repositorio de Spring Data
import Persistencia.Repositorios.UsuarioDAO;
// <-- ADIÓS: Ya no usamos DAOFactory ni UsuarioDAO
// import Persistencia.DAOFactory;
// import Persistencia.UsuarioDAO;

import java.util.Optional;

// <-- NUEVO: Importaciones de Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Gestiona la lógica de negocio para las operaciones de autenticación de usuarios.
 * Esta clase se encarga de verificar las credenciales proporcionadas por la interfaz de usuario
 * contra la capa de persistencia.
 */

// <-- NUEVO: @Service le dice a Spring que esta es una clase de Lógica/Servicio
@Service
public class GestorUsuario {

    // <-- CAMBIO: Reemplazamos el DAO por el Repository
    private final UsuarioDAO usuarioRepository;
    // private final UsuarioDAO usuarioDAO; <-- ADIÓS

    /**
     * <-- CAMBIO: El constructor ahora usa Inyección de Dependencias (@Autowired).
     * Spring se encarga de pasarnos el repositorio automáticamente.
     */
    @Autowired
    public GestorUsuario(UsuarioDAO usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        // this.usuarioDAO = factory.crearUsuarioDAO(); <-- ADIÓS
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

        // usamos el metodo del repositorio de Spring
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getPassword().equals(password)) {
            throw new CredencialesInvalidasException("El usuario o la contraseña no son válidos");
        }
    }
}