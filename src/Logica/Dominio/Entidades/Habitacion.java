package Logica.Dominio.Entidades;

import Logica.Dominio.Enum.EstadoHabitacion;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "habitaciones")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Una sola tabla para todos los hijos
@DiscriminatorColumn(name = "tipo_habitacion", discriminatorType = DiscriminatorType.STRING) // Columna que diferencia
public abstract class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;

    @Enumerated(EnumType.STRING)
    private EstadoHabitacion estado;

    private Float costoNoche;

    @OneToMany(mappedBy = "habitacion")
    private List<Estadia> estadias;

    public Habitacion() {}

    public Habitacion(String numero, EstadoHabitacion estado, Float costoNoche) {
        this.numero = numero;
        this.estado = estado;
        this.costoNoche = costoNoche;
    }
    // METODO ABSTRACTO: Obliga a las hijas a decir cómo se llaman
    public abstract String getNombreTipo();
    // Metodo abstracto: obliga a las hijas a decir cuántas camas tienen

    public abstract int getCantidadCamas();

    // Getters y Setters comunes
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public EstadoHabitacion getEstado() { return estado; }
    public void setEstado(EstadoHabitacion estado) { this.estado = estado; }
    public Float getCostoNoche() { return costoNoche; }
    public void setCostoNoche(Float costoNoche) { this.costoNoche = costoNoche; }
    public List<Estadia> getEstadias() { return estadias; }
    public void setEstadias(List<Estadia> estadias) { this.estadias = estadias; }
}