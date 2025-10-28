package Logica.Dominio;

public class DobleEstandar extends Habitacion {
    private Integer cant_camas_dobles;
    private Integer cant_camas_individuales;

    public DobleEstandar(Integer cant_camas_dobles, Integer cant_camas_individuales) {
        this.cant_camas_dobles = cant_camas_dobles;
        this.cant_camas_individuales = cant_camas_individuales;
    }

    public Integer getCant_camas_dobles() {
        return cant_camas_dobles;
    }

    public void setCant_camas_dobles(Integer cant_camas_dobles) {
        this.cant_camas_dobles = cant_camas_dobles;
    }

    public Integer getCant_camas_individuales() {
        return cant_camas_individuales;
    }

    public void setCant_camas_individuales(Integer cant_camas_individuales) {
        this.cant_camas_individuales = cant_camas_individuales;
    }


}
