package Logica.Dominio.Entidades;

import Logica.Dominio.Enum.EstadoHabitacion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SUITE_DBL")
public class SuiteDoble extends Habitacion {

    private Integer cantCamasDobles;
    private Integer cantCamasIndividuales;

    public SuiteDoble() {}
    
    @Override
    public String getNombreTipo() {
        return "Suite Doble"; // <--- AQUÍ DEFINES EL NOMBRE
    }

    public SuiteDoble(String numero, EstadoHabitacion estado, Float costoNoche, Integer cantCamasDobles, Integer cantCamasIndividuales) {
        super(numero, estado, costoNoche);
        this.cantCamasDobles = cantCamasDobles;
        this.cantCamasIndividuales = cantCamasIndividuales;
    }

    @Override
    public int getCantidadCamas() {
        return (cantCamasDobles != null ? cantCamasDobles : 0) +
                (cantCamasIndividuales != null ? cantCamasIndividuales : 0);
    }

    // Getters y Setters
    public Integer getCantCamasDobles() { return cantCamasDobles; }
    public void setCantCamasDobles(Integer cantCamasDobles) { this.cantCamasDobles = cantCamasDobles; }
    public Integer getCantCamasIndividuales() { return cantCamasIndividuales; }
    public void setCantCamasIndividuales(Integer cantCamasIndividuales) { this.cantCamasIndividuales = cantCamasIndividuales; }
}