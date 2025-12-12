package App;

import DTO.DatosFacturacionDTO;
import DTO.GenerarFacturaDTO;
import Logica.Servicio.GestorFactura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/facturacion")
@CrossOrigin(origins = "http://localhost:3000")
public class FacturaController {

    @Autowired private GestorFactura gestorFacturacion;

    @GetMapping("/preparar")
    public ResponseEntity<?> prepararFacturacion(@RequestParam String habitacion) {
        try {
            DatosFacturacionDTO datos = gestorFacturacion.obtenerDatosFacturacion(habitacion);
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/generar")
    public ResponseEntity<?> generarFactura(@RequestBody GenerarFacturaDTO dto) {
        try {
            gestorFacturacion.generarFactura(dto);
            return ResponseEntity.ok("Factura generada y estadía cerrada con éxito.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}