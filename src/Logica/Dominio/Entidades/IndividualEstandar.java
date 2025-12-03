package Logica.Dominio.Entidades;

import Logica.Dominio.Enum.EstadoHabitacion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("IND_STD")
public class IndividualEstandar extends Habitacion {

    private Integer cantCamasIndividuales;
    @Override
    public String getNombreTipo() {
        return "Individual Estándar";
    }
    public IndividualEstandar() {}

    public IndividualEstandar(String numero, EstadoHabitacion estado, Float costoNoche, Integer cantCamasIndividuales) {
        super(numero, estado, costoNoche);
        this.cantCamasIndividuales = cantCamasIndividuales;
    }

    @Override
    public int getCantidadCamas() {
        return (cantCamasIndividuales != null) ? cantCamasIndividuales : 0;
    }


    public Integer getCantCamasIndividuales() { return cantCamasIndividuales; }
    public void setCantCamasIndividuales(Integer cantCamasIndividuales) { this.cantCamasIndividuales = cantCamasIndividuales; }
}