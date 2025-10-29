package Logica.Dominio;

import Logica.Dominio.Enum.EstadoFactura;
import Logica.Dominio.Enum.TipoFactura;

import java.util.Date;

public class Factura {
    private String numeroFactura;
    private Date fechaEmision;
    private TipoFactura tipoFactura;
    private String CUIT;
    private int montoTotal;
    private EstadoFactura estado;
    private int descuento;

    public Factura(String numeroFactura, Date fechaEmision, TipoFactura tipoFactura,
                   String CUIT, int montoTotal, EstadoFactura estado, int descuento) {
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.tipoFactura = tipoFactura;
        this.CUIT = CUIT;
        this.montoTotal = montoTotal;
        this.estado = estado;
        this.descuento = descuento;
    }
    //Funciones
    public String getNumeroFactura(){
        return numeroFactura;
    }

    public int getDescuento(){
        return descuento;
    }
    public Date getFechaEmision() {
        return fechaEmision;
    }
    public String getCUIT() {
        return CUIT;
    }
    public TipoFactura getTipoFactura() {
        return tipoFactura;
    }
    public EstadoFactura getEstado() {
        return estado;
    }
    public int getMontoTotal() {
        return montoTotal;
    }

    public void setDescuento(int descuento){
        this.descuento = descuento;
    }
    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }
    public void setTipoFactura(TipoFactura tipoFactura) {
        this.tipoFactura = tipoFactura;
    }
    public void setCUIT(String CUIT) {
        this.CUIT = CUIT;
    }
    public void setMontoTotal(int montoTotal) {
        this.montoTotal = montoTotal;
    }
    public void setNumeroFactura(String numeroFactura){
        this.numeroFactura = numeroFactura;
    }

}
