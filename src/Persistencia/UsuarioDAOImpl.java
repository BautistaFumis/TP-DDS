package Persistencia;

import Logica.Dominio.Usuario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class UsuarioDAOImpl implements UsuarioDAO {

    private final String RUTA_ARCHIVO = "usuarios.csv";

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
