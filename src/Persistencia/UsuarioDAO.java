package Persistencia;

import Clases.Usuario;

import java.util.Optional;

public interface UsuarioDAO {
    /**
     * Busca un usuario por su ID en el archivo de datos
     * @param id El nombre de usuario a buscar.
     * @return Un Optional que contiene el Usuario si se encuentra, o un Optional vacío si no.
     */

    Optional<Usuario> buscarPorId(String id);


}
