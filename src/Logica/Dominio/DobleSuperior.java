package Logica.Dominio;

public class DobleSuperior extends Habitacion {
    private Integer cant_camas_dobles;
    private Integer cant_camas_individuales;
    private Integer cant_camas_king_size;

    public DobleSuperior(Integer cant_camas_dobles, Integer cant_camas_individuales, Integer cant_camas_king_size) {
        this.cant_camas_dobles = cant_camas_dobles;
        this.cant_camas_individuales = cant_camas_individuales;
        this.cant_camas_king_size = cant_camas_king_size;
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

    public Integer getCant_camas_king_size() {
        return cant_camas_king_size;
    }

    public void setCant_camas_king_size(Integer cant_camas_king_size) {
        this.cant_camas_king_size = cant_camas_king_size;
    }
}
