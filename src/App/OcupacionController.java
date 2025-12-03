package App;

import DTO.CrearOcupacionDTO;
import Logica.Servicio.GestorEstadia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ocupacion")
@CrossOrigin(origins = "http://localhost:3000")
public class OcupacionController {

    @Autowired
    private GestorEstadia gestorOcupacion;

    @PostMapping("/crear")
    public ResponseEntity<?> crearOcupacion(@RequestBody CrearOcupacionDTO dto) {
        try {
            gestorOcupacion.registrarOcupacion(dto);
            return ResponseEntity.ok("Ocupación registrada con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

