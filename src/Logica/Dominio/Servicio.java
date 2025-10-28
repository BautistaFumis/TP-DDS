package Logica.Dominio;

public class Servicio {

    private Float costo_servicio;

    public Servicio(Float costo_servicio) {
        this.costo_servicio = costo_servicio;
    }

    public Float getCosto_servicio() {
        return costo_servicio;
    }

    public void setCosto_servicio(Float costo_servicio) {
        this.costo_servicio = costo_servicio;
    }
}
