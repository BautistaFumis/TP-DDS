package DTO;
import java.util.List;

public class GenerarFacturaDTO {
    private Long idEstadia;
    private Long idResponsablePago; // ID del huesped seleccionado
    private String cuitTercero; // Opcional
    private List<ItemFacturableDTO> itemsSeleccionados;
    private Double montoTotal;
    private String tipoFactura;

    // Getters y Setters standard...
    public Long getIdEstadia() { return idEstadia; }
    public void setIdEstadia(Long idEstadia) { this.idEstadia = idEstadia; }
    public Long getIdResponsablePago() { return idResponsablePago; }
    public void setIdResponsablePago(Long idResponsablePago) { this.idResponsablePago = idResponsablePago; }
    public String getCuitTercero() { return cuitTercero; }
    public void setCuitTercero(String cuitTercero) { this.cuitTercero = cuitTercero; }
    public List<ItemFacturableDTO> getItemsSeleccionados() { return itemsSeleccionados; }
    public void setItemsSeleccionados(List<ItemFacturableDTO> itemsSeleccionados) { this.itemsSeleccionados = itemsSeleccionados; }
    public Double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(Double montoTotal) { this.montoTotal = montoTotal; }
    public String getTipoFactura() { return tipoFactura; }
    public void setTipoFactura(String tipoFactura) { this.tipoFactura = tipoFactura; }
}