package Logica.Dominio.Entidades;
import Logica.Dominio.Enum.TipoMoneda;
public class Efectivo extends MedioDePago {
    private TipoMoneda tipoMoneda;

    public Efectivo(){
    }
    public Efectivo(TipoMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }
    public TipoMoneda getTipoMoneda() {
        return tipoMoneda;
    }
    public void setTipoMoneda(TipoMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }
}
