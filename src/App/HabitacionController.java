package App;

import DTO.FilaGrillaDTO;
import Logica.Servicio.GestorHabitacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
@CrossOrigin(origins = "http://localhost:3000")
public class HabitacionController {

    @Autowired
    private GestorHabitacion gestorHabitacion;

    @GetMapping("/estado")
    public ResponseEntity<List<FilaGrillaDTO>> consultarEstado(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        return ResponseEntity.ok(gestorHabitacion.consultarEstadoHabitaciones(desde, hasta));
    }
}