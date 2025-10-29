package Persistencia.Implementaciones;

import Logica.Dominio.Usuario;
import Persistencia.UsuarioDAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

/**
 * Implementación concreta de la interfaz UsuarioDAO.
 * Esta clase se encarga de acceder a los datos de los usuarios almacenados
 * en un archivo de texto plano en formato CSV.
 */
public class UsuarioDAOImpl implements UsuarioDAO {

    /**
     * La ruta relativa al archivo CSV que contiene los datos de los usuarios.
     * Se espera que este archivo esté en la carpeta raíz del proyecto.
     */
    private final String RUTA_ARCHIVO = "usuarios.csv";

    /**
     * Busca un usuario por su ID en el archivo CSV.
     * El metodo lee el archivo línea por línea, dividiendo cada una por comas
     * para comparar el primer campo con el ID proporcionado.
     *
     * @param id El ID (nombre de usuario) a buscar.
     * @return Un {@link Optional} que contiene el objeto {@link Usuario} si se encuentra,
     * o un Optional vacío si no se encuentra ninguna coincidencia o si ocurre
     * un error de lectura.
     */
    @Override
    public Optional<Usuario> buscarPorId(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 2 && datos[0].equals(id)) {
                    return Optional.of(new Usuario(datos[0], datos[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de usuarios: " + e.getMessage());
        }
        return Optional.empty();
    }
}