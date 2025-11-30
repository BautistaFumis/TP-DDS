package Logica.Dominio.Entidades;

import Logica.Dominio.Enum.TipoCheque;

public class Cheque extends MedioDePago {
    private TipoCheque tipo;

    public Cheque(){
    }
    public Cheque(TipoCheque tipo){
        this.tipo = tipo;
    }

    public TipoCheque getTipo() {
        return tipo;
    }
    public void setTipo(TipoCheque tipo) {
        this.tipo = tipo;
    }
}
