package Logica.Dominio.Entidades;

import Logica.Dominio.Enum.TipoEstadoEstadia;
import Logica.Dominio.State.EstadoActiva;
import Logica.Dominio.State.EstadoCerrada;
import Logica.Dominio.State.EstadoEstadia;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "estadias")
public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación ManyToMany con Huéspedes (La que arreglamos antes)
    @ManyToMany
    @JoinTable(
            name = "estadia_huespedes",
            joinColumns = @JoinColumn(name = "estadia_id"),
            inverseJoinColumns = @JoinColumn(name = "huesped_id")
    )
    private List<Huesped> huespedes;

    // RELACIÓN 1 a 1 con RESERVA
    // Esta es la dueña de la relación (Foreign Key: reserva_id)
    @OneToOne
    @JoinColumn(name = "reserva_id", referencedColumnName = "id", nullable = true)
    private Reserva reserva;

    private LocalDate fechaCheckin;
    private LocalDate fechaCheckout;

    @Enumerated(EnumType.STRING)
    private TipoEstadoEstadia tipoEstado;

    @Transient
    private EstadoEstadia estadoLogic;

    @PostLoad
    private void reconstruirEstado() {
        if (tipoEstado == TipoEstadoEstadia.ACTIVA) {
            this.estadoLogic = new EstadoActiva();
        } else if (tipoEstado == TipoEstadoEstadia.CERRADA) {
            this.estadoLogic = new EstadoCerrada();
        }
    }

    public void setEstadoInterno(EstadoEstadia nuevoEstado) {
        this.estadoLogic = nuevoEstado;
        this.tipoEstado = nuevoEstado.getTipoEstado();
    }

    public Estadia() {}

    public Estadia(LocalDate fechaCheckin, List<Huesped> huespedes) {
        this.fechaCheckin = fechaCheckin;
        this.huespedes = huespedes;
        setEstadoInterno(new EstadoActiva());
    }

    // Métodos de negocio
    public void cerrar() {
        if (this.estadoLogic == null) reconstruirEstado();
        this.estadoLogic.cerrarEstadia(this);
    }

    public void reabrir() {
        if (this.estadoLogic == null) reconstruirEstado();
        this.estadoLogic.reabrirEstadia(this);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public List<Huesped> getHuespedes() { return huespedes; }
    public void setHuespedes(List<Huesped> huespedes) { this.huespedes = huespedes; }
    public LocalDate getFechaCheckin() { return fechaCheckin; }
    public void setFechaCheckin(LocalDate fechaCheckin) { this.fechaCheckin = fechaCheckin; }
    public LocalDate getFechaCheckout() { return fechaCheckout; }
    public void setFechaCheckout(LocalDate fechaCheckout) { this.fechaCheckout = fechaCheckout; }
    public TipoEstadoEstadia getTipoEstado() { return tipoEstado; }

    public Reserva getReserva() { return reserva; }

    // Setter bidireccional
    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
        // Evitar bucle infinito al setear
        if (reserva != null && reserva.getEstadia() != this) {
            reserva.setEstadia(this);
        }
    }
}