package Logica.Servicio;

import Logica.Dominio.Entidades.Usuario;
import Logica.Excepciones.CredencialesInvalidasException;

// <-- NUEVO: Importamos el Repositorio de Spring Data
import Persistencia.Repositorios.UsuarioDAO;
// <-- ADIÓS: Ya no usamos DAOFactory ni UsuarioDAO
// import Persistencia.DAOFactory;
// import Persistencia.Repositorios.UsuarioDAO;

import java.util.Optional;

// <-- NUEVO: Importaciones de Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GestorUsuario {

    private final UsuarioDAO usuarioRepository;

    @Autowired
    public GestorUsuario(UsuarioDAO usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void autenticar(String id, String password) throws CredencialesInvalidasException {

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getPassword().equals(password)) {
            throw new CredencialesInvalidasException("El usuario o la contraseña no son válidos");
        }
    }
}