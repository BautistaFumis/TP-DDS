package Logica.Dominio.Entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private Float costoServicio;


    @ManyToOne
    @JoinColumn(name = "estadia_id")
    private Estadia estadia;

    public Servicio() {}

    public Servicio(String descripcion, Float costoServicio) {
        this.descripcion = descripcion;
        this.costoServicio = costoServicio;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Float getCostoServicio() { return costoServicio; }
    public void setCostoServicio(Float costoServicio) { this.costoServicio = costoServicio; }

    public Estadia getEstadia() { return estadia; }
    public void setEstadia(Estadia estadia) { this.estadia = estadia; }
}