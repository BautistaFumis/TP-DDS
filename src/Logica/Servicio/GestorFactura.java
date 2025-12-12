package Logica.Servicio;

import DTO.DatosFacturacionDTO;
import DTO.GenerarFacturaDTO;
import DTO.ItemFacturableDTO;
import Logica.Dominio.Entidades.*;
import Logica.Dominio.Enum.EstadoFactura;
import Logica.Dominio.Enum.TipoFactura;
import Logica.Dominio.Enum.TipoEstadoEstadia;
import Persistencia.Repositorios.EstadiaDAO;
import Persistencia.Repositorios.HuespedDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
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

        // Calculamos items
        List<ItemFacturableDTO> items = new ArrayList<>();

        // 1. Costo Estadía
        long dias = ChronoUnit.DAYS.between(estadia.getFechaCheckin(), estadia.getFechaCheckout());
        if (dias == 0) dias = 1; // Al menos un día

        Double precioBase = (double) estadia.getHabitacion().getCostoNoche();
        items.add(new ItemFacturableDTO("Alojamiento (" + dias + " noches)", precioBase * dias, true));

        // 2. Servicios (Asumimos que Estadia tiene getServicios - mockeado si no existe en tu entidad previa)
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

        // Aquí iría la lógica de crear la entidad Factura y guardarla
        // Como el CU termina imprimiendo y "Actualizando datos", cerramos la estadía

        // Simulación de guardado de factura (puedes expandir esto guardando en un FacturaDAO)
        // Factura factura = new Factura(...);
        // facturaRepository.save(factura);

        // Liberar habitación (Check-out)
        estadia.setTipoEstado(TipoEstadoEstadia.CERRADA);
        estadiaRepository.save(estadia);
    }
}