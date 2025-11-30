package Logica.Dominio.Entidades;

public class Pago {
    private Float monto;
    private MedioDePago medioDePago;
    private ResponsableDePago responsableDePago;

    public Pago(){
    }
    public Pago(Float monto, MedioDePago medioDePago, ResponsableDePago responsableDePago){
        this.monto = monto;
        this.medioDePago = medioDePago;
        this.responsableDePago = responsableDePago;
    }

    public Float getMonto() {
        return monto;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public MedioDePago getMedioDePago() {
        return medioDePago;
    }
    public void setMedioDePago(MedioDePago medioDePago) {
        this.medioDePago = medioDePago;
    }
    public ResponsableDePago getResponsableDePago() {
        return responsableDePago;
    }
    public void setResponsableDePago(ResponsableDePago responsableDePago) {
        this.responsableDePago = responsableDePago;
    }

}
