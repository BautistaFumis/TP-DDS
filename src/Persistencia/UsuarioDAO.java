package Persistencia;

import Logica.Dominio.Usuario;

import java.util.Optional;

/**
 * Define el contrato para las operaciones de acceso a datos (DAO) de la entidad UsuarioDAO.
 * Esta interfaz abstrae la lógica de persistencia, permitiendo que las implementaciones
 * concretas manejen cómo se guardan, leen o modifican los datos de los usuarios,
 * ya sea desde un archivo, una base de datos u otra fuente.
 */
public interface UsuarioDAO {

    /**
     * Busca un usuario por su identificador único (ID) en la fuente de datos.
     *
     * @param id El nombre de usuario a buscar.
     * @return Un {@link Optional} que contiene el objeto {@link Usuario} si se encuentra,
     * o un Optional vacío si no existe un usuario con ese ID.
     */
    Optional<Usuario> buscarPorId(String id);

    // ABM (Alta, Baja, Modificación) de Usuario no implementado por el momento,
    // ya que los usuarios son cargados y modificados directamente desde el archivo usuarios.csv.
}