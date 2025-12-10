package App;

import DTO.HuespedAltaDTO;
import DTO.HuespedBusquedaDTO;
import Logica.Dominio.Entidades.Huesped;
import Logica.Excepciones.CamposObligatoriosException;
import Logica.Excepciones.DocumentoDuplicadoException;
import Logica.Excepciones.EntidadNoEncontradaException;
import Logica.Excepciones.OperacionNoPermitidaException;
import Logica.Servicio.GestorHuesped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/huespedes")
@CrossOrigin(origins = "http://localhost:3000")
public class HuespedController {

    @Autowired
    private GestorHuesped gestorHuesped;

    // --- BÚSQUEDA ---
    @GetMapping("/buscar")
    public ResponseEntity<List<HuespedBusquedaDTO>> buscarHuespedes(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String tipoDocumento,
            @RequestParam(required = false) String documento
    ) {
        List<Huesped> encontrados = gestorHuesped.buscarHuespedes(apellido, nombre, tipoDocumento, documento);
        List<HuespedBusquedaDTO> respuesta = encontrados.stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuesta);
    }

    // --- OBTENER POR ID (Para llenar el formulario y verificar borrado) ---
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerHuesped(@PathVariable Long id) {
        try {
            Huesped huesped = gestorHuesped.obtenerHuespedPorId(id);

            // Calculamos si tiene historial para que el Frontend sepa qué cartel mostrar al borrar
            boolean tieneEstadias = huesped.getEstadias() != null && !huesped.getEstadias().isEmpty();

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("huesped", huesped);
            respuesta.put("tieneEstadias", tieneEstadias);

            return ResponseEntity.ok(respuesta);
        } catch (EntidadNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // --- REGISTRAR ---
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarNuevoHuesped(
            @RequestBody HuespedAltaDTO huespedDTO,
            @RequestParam(required = false) Boolean forzar
    ) {
        try {
            Huesped nuevoHuesped = gestorHuesped.convertirHuesped(huespedDTO);
            if (Boolean.TRUE.equals(forzar)) {
                gestorHuesped.registrarHuespedAceptandoDuplicado(nuevoHuesped);
            } else {
                gestorHuesped.registrarNuevoHuesped(nuevoHuesped);
            }
            HuespedBusquedaDTO dtoRespuesta = convertirEntidadADTO(nuevoHuesped);
            return ResponseEntity.status(HttpStatus.CREATED).body(dtoRespuesta);
        } catch (DocumentoDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (CamposObligatoriosException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }

    // --- MODIFICAR (Siempre permitido, salvo errores de validación) ---
    @PutMapping("/{id}")
    public ResponseEntity<?> modificarHuesped(
            @PathVariable Long id,
            @RequestBody HuespedAltaDTO huespedDTO,
            @RequestParam(required = false, defaultValue = "false") Boolean forzar
    ) {
        try {
            Huesped huespedModificado = gestorHuesped.modificarHuesped(id, huespedDTO, forzar);
            return ResponseEntity.ok(convertirEntidadADTO(huespedModificado));
        } catch (DocumentoDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (CamposObligatoriosException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (EntidadNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }

    // --- BAJA (Restringida por lógica de negocio si tiene historial) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<?> darDeBajaHuesped(@PathVariable Long id) {
        try {
            gestorHuesped.darDeBajaHuesped(id);
            return ResponseEntity.ok("Los datos del huésped han sido eliminados del sistema.");
        } catch (OperacionNoPermitidaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EntidadNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }

    private HuespedBusquedaDTO convertirEntidadADTO(Huesped huesped) {
        HuespedBusquedaDTO dto = new HuespedBusquedaDTO();
        dto.setId(huesped.getId());
        dto.setNombre(huesped.getNombre());
        dto.setApellido(huesped.getApellido());
        dto.setEmail(huesped.getEmail());
        dto.setTipoDocumento(huesped.getTipoDocumento());
        dto.setDocumento(huesped.getDocumento());
        return dto;
    }
}