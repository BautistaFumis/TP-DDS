package App;

import Logica.Dominio.Usuario;
import Persistencia.Repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Esta clase se ejecuta automáticamente al iniciar Spring Boot.
 * Su único propósito es verificar si la base de datos de usuarios está vacía
 * y, de ser así, cargar datos iniciales (como tu usuario "Conserje").
 */
@Component
public class CargadorDeDatos implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verifica si ya hay usuarios en la BD
        if (usuarioRepository.count() == 0) {
            System.out.println(">>> Base de datos de usuarios vacía. Cargando usuario por defecto...");

            // Crea tu usuario "Conserje"
            Usuario conserje = new Usuario("Conserje", "conserje123");

            // Guarda el usuario en la base de datos H2
            usuarioRepository.save(conserje);

            System.out.println(">>> Usuario 'Conserje' cargado exitosamente.");
        } else {
            System.out.println(">>> La base de datos de usuarios ya tiene datos.");
        }
    }
}