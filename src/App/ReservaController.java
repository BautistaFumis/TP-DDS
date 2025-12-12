package App;

import DTO.CrearReservaDTO;
import DTO.ReservaBusquedaDTO;
import Logica.Servicio.GestorReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // NUEVO: Buscar Reservas
    @GetMapping("/buscar")
    public ResponseEntity<List<ReservaBusquedaDTO>> buscarReservas(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = true) String apellido // Apellido obligatorio por CU
    ) {
        return ResponseEntity.ok(gestorReserva.buscarReservas(nombre, apellido));
    }

    // NUEVO: Cancelar Reservas
    @PostMapping("/cancelar")
    public ResponseEntity<?> cancelarReservas(@RequestBody List<Long> ids) {
        try {
            gestorReserva.cancelarReservas(ids);
            return ResponseEntity.ok("Reservas canceladas correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al cancelar: " + e.getMessage());
        }
    }
}