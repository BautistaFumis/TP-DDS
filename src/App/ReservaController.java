package App;

import DTO.CrearReservaDTO;
import Logica.Servicio.GestorReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservaController {

    @Autowired private GestorReserva gestorReserva;

    @PostMapping("/crear")
    public ResponseEntity<?> crearReserva(@RequestBody CrearReservaDTO dto) {
        try {
            gestorReserva.procesarReservas(dto);
            return ResponseEntity.ok("Reservas registradas con éxito.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}