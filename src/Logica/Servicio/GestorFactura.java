package Logica.Servicio;

import DTO.DatosFacturacionDTO;
import DTO.GenerarFacturaDTO;
import DTO.ItemFacturableDTO;
import Logica.Dominio.Entidades.*;
import Logica.Dominio.Enum.TipoEstadoEstadia;
import Repositorios.EstadiaDAO;
import Repositorios.HuespedDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class GestorFactura {

    @Autowired private EstadiaDAO estadiaRepository;
    @Autowired private HuespedDAO huespedRepository;

    public DatosFacturacionDTO obtenerDatosFacturacion(String numeroHabitacion) {
        Estadia estadia = estadiaRepository.findEstadiaActivaPorHabitacion(numeroHabitacion)
                .orElseThrow(() -> new RuntimeException("No hay una estadía activa en la habitación " + numeroHabitacion));

        DatosFacturacionDTO dto = new DatosFacturacionDTO();
        dto.setIdEstadia(estadia.getId());
        dto.setHuespedes(estadia.getHuespedes());


        List<ItemFacturableDTO> items = new ArrayList<>();


        long dias = ChronoUnit.DAYS.between(estadia.getFechaCheckin(), estadia.getFechaCheckout());
        if (dias == 0) dias = 1; // Al menos un día

        Double precioBase = (double) estadia.getHabitacion().getCostoNoche();
        items.add(new ItemFacturableDTO("Alojamiento (" + dias + " noches)", precioBase * dias, true));

        if (estadia.getServicios() != null) {
            for (Servicio s : estadia.getServicios()) {
                items.add(new ItemFacturableDTO(s.getDescripcion(), (double) s.getCostoServicio(), false));
            }
        }

        dto.setItems(items);
        return dto;
    }

    @Transactional
    public void generarFactura(GenerarFacturaDTO dto) {
        Estadia estadia = estadiaRepository.findById(dto.getIdEstadia())
                .orElseThrow(() -> new RuntimeException("Estadía no encontrada"));


        estadia.setTipoEstado(TipoEstadoEstadia.CERRADA);
        estadiaRepository.save(estadia);
    }
}