package Logica.Dominio;

public class Habitacion {
    private EstadoHabitacion estado;
    private Float costo_noche;

    public Habitacion(EstadoHabitacion estado, Float costo_noche) {
        this.estado = estado;
        this.costo_noche = costo_noche;
    }

    public Habitacion() {
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoHabitacion estado) {
        this.estado = estado;
    }

    public Float getCosto_noche() {
        return costo_noche;
    }

    public void setCosto_noche(Float costo_noche) {
        this.costo_noche = costo_noche;
    }
}
