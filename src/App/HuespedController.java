package App;

import DTO.HuespedAltaDTO;
import DTO.HuespedBusquedaDTO; // Asumo que ya tienes este DTO de la respuesta anterior
import Logica.Dominio.Entidades.Huesped;
import Logica.Excepciones.CamposObligatoriosException;
import Logica.Excepciones.DocumentoDuplicadoException;
import Logica.Servicio.GestorHuesped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/huespedes")
@CrossOrigin(origins = "http://localhost:3000")
public class HuespedController {

    @Autowired
    private GestorHuesped gestorHuesped;


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


    @PostMapping("/registrar")
    public ResponseEntity<?> registrarNuevoHuesped(
            @RequestBody HuespedAltaDTO huespedDTO,
            @RequestParam(required = false) Boolean forzar
    ) {
        try {
            Huesped nuevoHuesped = new Huesped();

            if (Boolean.TRUE.equals(forzar)) {
                nuevoHuesped = gestorHuesped.convertirHuesped(huespedDTO);
                gestorHuesped.registrarHuespedAceptandoDuplicado(nuevoHuesped);
            } else {
                nuevoHuesped = gestorHuesped.convertirHuesped(huespedDTO);
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

    // auxiliar para convertir a DTO de salida
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