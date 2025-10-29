package Logica.Dominio;

import java.util.Date;

public class NotaDeCredito {
    private String numero;
    private Date fecha;
    private float montoTotal;

    // Constructor
    public NotaDeCredito(String numero, Date fecha, float montoTotal) {
        this.numero = numero;
        this.fecha = fecha;
        this.montoTotal = montoTotal;
    }

    // Getters
    public String getNumero() {
        return numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public float getMontoTotal() {
        return montoTotal;
    }

    // Setters
    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setMontoTotal(float montoTotal) {
        this.montoTotal = montoTotal;
    }
}
