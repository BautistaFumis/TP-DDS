package Logica.Dominio.Entidades;

import java.time.LocalDate;

import Logica.Dominio.Enum.EstadoReserva;
import jakarta.persistence.*;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String telefono;
    private String codigoReserva;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaReserva;


    @Enumerated(EnumType.STRING)
    private EstadoReserva estado;


    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
    private Estadia estadia;

    public Reserva() {}


    public Reserva(String nombre, String apellido, String telefono, String codigoReserva, LocalDate fechaInicio, LocalDate fechaFin, Huesped huespedPrincipal) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.codigoReserva = codigoReserva;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaReserva = LocalDate.now();
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCodigoReserva() { return codigoReserva; }
    public void setCodigoReserva(String codigoReserva) { this.codigoReserva = codigoReserva; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public LocalDate getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDate fechaReserva) { this.fechaReserva = fechaReserva; }
    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }

    public Estadia getEstadia() { return estadia; }

    // Setter especial para mantener coherencia bidireccional
    public void setEstadia(Estadia estadia) {
        this.estadia = estadia;
        if (estadia != null && estadia.getReserva() != this) {
            estadia.setReserva(this);
        }
    }
}