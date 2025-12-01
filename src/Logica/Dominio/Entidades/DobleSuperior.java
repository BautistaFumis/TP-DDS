package Logica.Dominio.Entidades;

import Logica.Dominio.Enum.EstadoHabitacion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DOB_SUP")
public class DobleSuperior extends Habitacion {

    private Integer cantCamasDobles;
    private Integer cantCamasIndividuales;
    private Integer cantCamasKingSize;

    public DobleSuperior() {}

    public DobleSuperior(String numero, EstadoHabitacion estado, Float costoNoche, Integer cantCamasDobles, Integer cantCamasIndividuales, Integer cantCamasKingSize) {
        super(numero, estado, costoNoche);
        this.cantCamasDobles = cantCamasDobles;
        this.cantCamasIndividuales = cantCamasIndividuales;
        this.cantCamasKingSize = cantCamasKingSize;
    }
    @Override
    public String getNombreTipo() {
        return "Doble Superior"; // <--- AQUÍ DEFINES EL NOMBRE QUE SE VE EN EL FRONT
    }
    @Override
    public int getCantidadCamas() {
        return (cantCamasDobles != null ? cantCamasDobles : 0) +
                (cantCamasIndividuales != null ? cantCamasIndividuales : 0) +
                (cantCamasKingSize != null ? cantCamasKingSize : 0);
    }

    // Getters y Setters
    public Integer getCantCamasDobles() { return cantCamasDobles; }
    public void setCantCamasDobles(Integer cantCamasDobles) { this.cantCamasDobles = cantCamasDobles; }
    public Integer getCantCamasIndividuales() { return cantCamasIndividuales; }
    public void setCantCamasIndividuales(Integer cantCamasIndividuales) { this.cantCamasIndividuales = cantCamasIndividuales; }
    public Integer getCantCamasKingSize() { return cantCamasKingSize; }
    public void setCantCamasKingSize(Integer cantCamasKingSize) { this.cantCamasKingSize = cantCamasKingSize; }
}