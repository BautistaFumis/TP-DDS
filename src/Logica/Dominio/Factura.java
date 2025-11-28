package Logica.Dominio;

import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Enum.EstadoFactura;
import Logica.Dominio.Enum.TipoFactura;

import java.util.Date;
import java.util.List;

public class Factura {
    private String numeroFactura;
    private Date fechaEmision;
    private TipoFactura tipoFactura;
    private String CUIT;
    private Float montoTotal;
    private EstadoFactura estado;
    private Float descuento;
    private List<NotaDeCredito> notasDeCredito;
    private List<Pago> pagos;
    private ResponsableDePago responsableDePago;
    private Estadia estadia;

    public Factura(String numeroFactura, Date fechaEmision, TipoFactura tipoFactura, String CUIT, Float montoTotal, EstadoFactura estado, Float descuento, List<NotaDeCredito> notasDeCredito, List<Pago> pagos, ResponsableDePago responsableDePago,Estadia estadia) {
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.tipoFactura = tipoFactura;
        this.CUIT = CUIT;
        this.montoTotal = montoTotal;
        this.estado = estado;
        this.descuento = descuento;
        this.notasDeCredito = notasDeCredito;
        this.pagos = pagos;
        this.responsableDePago = responsableDePago;
        this.estadia = estadia;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public TipoFactura getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(TipoFactura tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public String getCUIT() {
        return CUIT;
    }

    public void setCUIT(String CUIT) {
        this.CUIT = CUIT;
    }

    public Float getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Float montoTotal) {
        this.montoTotal = montoTotal;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public Float getDescuento() {
        return descuento;
    }

    public void setDescuento(Float descuento) {
        this.descuento = descuento;
    }

    public List<NotaDeCredito> getNotasDeCredito() {
        return notasDeCredito;
    }

    public void setNotasDeCredito(List<NotaDeCredito> notasDeCredito) {
        this.notasDeCredito = notasDeCredito;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public ResponsableDePago getResponsableDePago() {
        return responsableDePago;
    }

    public void setResponsableDePago(ResponsableDePago responsableDePago) {
        this.responsableDePago = responsableDePago;
    }

    public Estadia getEstadia() {
        return estadia;
    }

    public void setEstadia(Estadia estadia) {
        this.estadia = estadia;
    }
}
