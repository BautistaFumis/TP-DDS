package App;

import DTO.UsuarioDTO;
import Logica.Excepciones.CredencialesInvalidasException;
import Logica.Servicio.GestorUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UsuarioController {

    @Autowired
    private GestorUsuario gestorUsuario;

    @PostMapping("/login")
    public ResponseEntity<?> autenticarUsuario(@RequestBody UsuarioDTO loginRequest) {
        try {
            gestorUsuario.autenticar(loginRequest.getId(), loginRequest.getPassword());

            Map<String, String> response = Map.of("message", "¡Autenticación exitosa! Bienvenido, " + loginRequest.getId() + ".");
            return ResponseEntity.ok(response);

        } catch (CredencialesInvalidasException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + e.getMessage());
        }
    }
}