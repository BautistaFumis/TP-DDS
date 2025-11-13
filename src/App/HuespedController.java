package App;

import DTO.HuespedBusquedaDTO;
import DTO.HuespedAltaDTO;
import Logica.Dominio.Entidades.Huesped;
import Logica.Excepciones.CamposObligatoriosException; // <-- IMPORTAR
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
public class HuespedController {

    @Autowired
    private GestorHuesped gestorHuesped;

    /**
     * Endpoint para buscar huéspedes (GET /api/huespedes/buscar)
     * (Sin cambios)
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<HuespedBusquedaDTO>> buscarHuespedes(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String tipoDocumento,
            @RequestParam(required = false) String documento
    ) {
        List<Huesped> huespedesEncontrados = gestorHuesped.buscarHuespedes(apellido, nombre, tipoDocumento, documento);
        List<HuespedBusquedaDTO> respuestaDTOs = huespedesEncontrados.stream()
                .map(this::convertirEntidadADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuestaDTOs);
    }

    /**
     * Endpoint para registrar un nuevo huésped (POST /api/huespedes/registrar)
     * Recibe: HuespedRegistroDTO
     * Devuelve: HuespedDTO (el huésped creado) o un String de error
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarNuevoHuesped(
            @RequestBody HuespedAltaDTO huespedDTO,
            @RequestParam(required = false) Boolean forzar // <-- 1. AÑADIR PARÁMETRO
    ) {
        try {
            // 1. Convertir el DTO de Registro a una Entidad Huesped
            Huesped nuevoHuesped = new Huesped();
            nuevoHuesped.setNombre(huespedDTO.getNombre());
            nuevoHuesped.setApellido(huespedDTO.getApellido());
            nuevoHuesped.setEmail(huespedDTO.getEmail());
            nuevoHuesped.setTipoDocumento(huespedDTO.getTipoDocumento());
            nuevoHuesped.setDocumento(huespedDTO.getDocumento());
            nuevoHuesped.setTelefono(huespedDTO.getTelefono());
            nuevoHuesped.setFechaNacimiento(huespedDTO.getFechaNacimiento());
            nuevoHuesped.setOcupacion(huespedDTO.getOcupacion());
            nuevoHuesped.setNacionalidad(huespedDTO.getNacionalidad());
            nuevoHuesped.setCuit(huespedDTO.getCuit());
            nuevoHuesped.setCategoriaIVA(huespedDTO.getCategoriaIVA());
            nuevoHuesped.setDireccion(huespedDTO.getDireccion());

            // --- 2. LÓGICA DE DECISIÓN ---
            if (Boolean.TRUE.equals(forzar)) {
                // Flujo 2.B.2.1: Aceptar Igualmente
                gestorHuesped.registrarHuespedAceptandoDuplicado(nuevoHuesped);
            } else {
                // Flujo normal (que puede lanzar DocumentoDuplicadoException)
                gestorHuesped.registrarNuevoHuesped(nuevoHuesped);
            }
            // --- FIN DE LA LÓGICA ---

            // 3. Convertir la Entidad (ya con ID) a un DTO de respuesta
            HuespedBusquedaDTO dtoRespuesta = convertirEntidadADTO(nuevoHuesped);

            // 4. Devolver el DTO de respuesta
            return ResponseEntity.status(HttpStatus.CREATED).body(dtoRespuesta);

        } catch (DocumentoDuplicadoException e) {
            // Devuelve el mensaje de error de negocio (409)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        } catch (CamposObligatoriosException e) { // <-- 3. MANEJAR ERRORES 400
            // Devuelve el mensaje de error de validación (400)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            // Devuelve un mensaje de error genérico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + e.getMessage());
        }
    }

    /**
     * Método helper privado para convertir una Entidad Huesped a un HuespedDTO.
     * (Sin cambios)
     */
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