package Logica.Dominio.Entidades;

import Logica.Dominio.Enum.TipoEstadoEstadia;
import Logica.Dominio.State.EstadoActiva;
import Logica.Dominio.State.EstadoCerrada;
import Logica.Dominio.State.EstadoEstadia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "estadias")
public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;


    @OneToMany(mappedBy = "estadia", cascade = CascadeType.ALL)
    private List<Servicio> servicios = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "estadia_huespedes",
            joinColumns = @JoinColumn(name = "estadia_id"),
            inverseJoinColumns = @JoinColumn(name = "huesped_id")
    )
    private List<Huesped> huespedes = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "reserva_id", referencedColumnName = "id", nullable = true)
    private Reserva reserva;

    private LocalDate fechaCheckin;
    private LocalDate fechaCheckout;

    @Enumerated(EnumType.STRING)
    private TipoEstadoEstadia tipoEstado;

    @Transient
    private EstadoEstadia estadoLogic;


    public Estadia() {}


    public Estadia(LocalDate fechaCheckin, LocalDate fechaCheckout, Habitacion habitacion, TipoEstadoEstadia tipoEstado) {
        this.fechaCheckin = fechaCheckin;
        this.fechaCheckout = fechaCheckout;
        this.habitacion = habitacion;
        this.tipoEstado = tipoEstado;

        // Inicialización básica del State Pattern
        if (tipoEstado == TipoEstadoEstadia.ACTIVA) {
            this.estadoLogic = new EstadoActiva();
        }
        // Si es RESERVADA, no tiene estado lógico activo todavía o podemos asignar uno EstadoReservada si existe.
    }


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

    public void cerrar() {
        if (this.estadoLogic == null) reconstruirEstado();
        if (this.estadoLogic != null) this.estadoLogic.cerrarEstadia(this);
    }

    public void reabrir() {
        if (this.estadoLogic == null) reconstruirEstado();
        if (this.estadoLogic != null) this.estadoLogic.reabrirEstadia(this);
    }

    public void agregarServicio(Servicio servicio) {
        this.servicios.add(servicio);
        servicio.setEstadia(this);
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }

    public List<Servicio> getServicios() { return servicios; }
    public void setServicios(List<Servicio> servicios) { this.servicios = servicios; }

    public List<Huesped> getHuespedes() { return huespedes; }
    public void setHuespedes(List<Huesped> huespedes) { this.huespedes = huespedes; }

    public LocalDate getFechaCheckin() { return fechaCheckin; }
    public void setFechaCheckin(LocalDate fechaCheckin) { this.fechaCheckin = fechaCheckin; }

    public LocalDate getFechaCheckout() { return fechaCheckout; }
    public void setFechaCheckout(LocalDate fechaCheckout) { this.fechaCheckout = fechaCheckout; }

    public TipoEstadoEstadia getTipoEstado() { return tipoEstado; }
    public void setTipoEstado(TipoEstadoEstadia tipoEstado) { this.tipoEstado = tipoEstado; }

    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
        // Mantenimiento de la relación bidireccional
        if (reserva != null && reserva.getEstadia() != this) {
            reserva.setEstadia(this);
        }
    }
}