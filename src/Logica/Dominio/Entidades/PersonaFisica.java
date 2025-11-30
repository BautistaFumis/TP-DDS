package Logica.Dominio.Entidades;

public class PersonaFisica extends ResponsableDePago{
    private Huesped huesped;

    public PersonaFisica(Huesped huesped) {
        this.huesped = huesped;
    }

    public Huesped getHuesped() {
        return huesped;
    }

    public void setHuesped(Huesped huesped) {
        this.huesped = huesped;
    }
}
