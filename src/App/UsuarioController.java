package App;

import DTO.UsuarioDTO;
import Logica.Excepciones.CredencialesInvalidasException;
import Logica.Servicio.GestorUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController // Le dice a Spring que esto es un Controlador REST
@RequestMapping("/api/auth") // Todos los pedidos a esta clase empiezan con /api/auth
public class UsuarioController {

    @Autowired
    private GestorUsuario gestorUsuario;

    @PostMapping("/login")
    public ResponseEntity<?> autenticarUsuario(@RequestBody UsuarioDTO loginRequest) {
        try {
            // Reutilizamos tu lógica de negocio exacta del GestorUsuario
            gestorUsuario.autenticar(loginRequest.getId(), loginRequest.getPassword());

            // Si la autenticación es exitosa, devolvemos un 200 OK
            // con un mensaje de bienvenida.
            Map<String, String> response = Map.of("message", "¡Autenticación exitosa! Bienvenido, " + loginRequest.getId() + ".");
            return ResponseEntity.ok(response);

        } catch (CredencialesInvalidasException e) {
            // Si falla, devolvemos un 401 Unauthorized (No Autorizado)
            // con el mensaje de error de tu excepción.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            // Para cualquier otro error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + e.getMessage());
        }
    }
}