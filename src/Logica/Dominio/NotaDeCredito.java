package Logica.Dominio;

import java.util.Date;
import java.util.List;

public class NotaDeCredito {
    private String numero;

    public NotaDeCredito(String numero, Date fecha, Float montoTotal, List<Factura> facturas) {
        this.numero = numero;
        this.fecha = fecha;
        this.montoTotal = montoTotal;
        this.facturas = facturas;
    }

    private Date fecha;
    private Float montoTotal;
    private List<Factura> facturas;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Float getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Float montoTotal) {
        this.montoTotal = montoTotal;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }
}
