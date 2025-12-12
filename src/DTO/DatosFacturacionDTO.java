package DTO;
import Logica.Dominio.Entidades.Huesped;
import java.util.List;

public class DatosFacturacionDTO {
    private Long idEstadia;
    private List<Huesped> huespedes;
    private List<ItemFacturableDTO> items;
    private String tipoFacturaSugerida; // A o B

    // Getters y Setters
    public Long getIdEstadia() { return idEstadia; }
    public void setIdEstadia(Long idEstadia) { this.idEstadia = idEstadia; }
    public List<Huesped> getHuespedes() { return huespedes; }
    public void setHuespedes(List<Huesped> huespedes) { this.huespedes = huespedes; }
    public List<ItemFacturableDTO> getItems() { return items; }
    public void setItems(List<ItemFacturableDTO> items) { this.items = items; }
    public String getTipoFacturaSugerida() { return tipoFacturaSugerida; }
    public void setTipoFacturaSugerida(String tipoFacturaSugerida) { this.tipoFacturaSugerida = tipoFacturaSugerida; }
}