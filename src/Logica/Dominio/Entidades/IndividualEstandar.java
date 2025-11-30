package Logica.Dominio.Entidades;

import Logica.Dominio.Enum.EstadoHabitacion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("IND_STD")
public class IndividualEstandar extends Habitacion {

    private Integer cantCamasIndividuales;

    public IndividualEstandar() {}

    public IndividualEstandar(String numero, EstadoHabitacion estado, Float costoNoche, Integer cantCamasIndividuales) {
        super(numero, estado, costoNoche); // Llama al constructor de Habitacion
        this.cantCamasIndividuales = cantCamasIndividuales;
    }

    @Override
    public int getCantidadCamas() {
        return (cantCamasIndividuales != null) ? cantCamasIndividuales : 0;
    }

    // Getters y Setters
    public Integer getCantCamasIndividuales() { return cantCamasIndividuales; }
    public void setCantCamasIndividuales(Integer cantCamasIndividuales) { this.cantCamasIndividuales = cantCamasIndividuales; }
}