package DTO;

import java.time.LocalDate;
import java.util.List;

public class FilaGrillaDTO {
    private String fechaStr; // "dd/mm/aaaa"
    private List<CeldaEstadoDTO> celdas;

    public FilaGrillaDTO(String fechaStr, List<CeldaEstadoDTO> celdas) {
        this.fechaStr = fechaStr;
        this.celdas = celdas;
    }

    public String getFechaStr() { return fechaStr; }
    public List<CeldaEstadoDTO> getCeldas() { return celdas; }
}