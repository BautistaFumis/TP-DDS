package Logica.Dominio.Entidades;

import Logica.Dominio.Enum.TipoEstadoEstadia;
import Logica.Dominio.State.EstadoActiva;
import Logica.Dominio.State.EstadoCerrada;
import Logica.Dominio.State.EstadoEstadia;

import java.time.LocalDate;
import java.util.ArrayList; // Importante para inicializar listas
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "estadias")
public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RELACIÓN NUEVA: MUCHAS ESTADÍAS -> 1 HABITACIÓN
    @ManyToOne
    @JoinColumn(name = "habitacion_id", nullable = true) // nullable=true por si hay datos viejos, idealmente false
    private Habitacion habitacion;

    // RELACIÓN NUEVA: 1 ESTADÍA -> MUCHOS SERVICIOS
    // CascadeType.ALL permite que si guardas la estadía, se guarden sus servicios nuevos
    @OneToMany(mappedBy = "estadia", cascade = CascadeType.ALL)
    private List<Servicio> servicios = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "estadia_huespedes",
            joinColumns = @JoinColumn(name = "estadia_id"),
            inverseJoinColumns = @JoinColumn(name = "huesped_id")
    )
    private List<Huesped> huespedes;

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

    // Constructor actualizado
    public Estadia(LocalDate fechaCheckin, List<Huesped> huespedes, Habitacion habitacion) {
        this.fechaCheckin = fechaCheckin;
        this.huespedes = huespedes;
        this.habitacion = habitacion;
        setEstadoInterno(new EstadoActiva());
    }

    public void cerrar() {
        if (this.estadoLogic == null) reconstruirEstado();
        this.estadoLogic.cerrarEstadia(this);
    }

    public void reabrir() {
        if (this.estadoLogic == null) reconstruirEstado();
        this.estadoLogic.reabrirEstadia(this);
    }

    // Método helper para agregar servicios facilmente
    public void agregarServicio(Servicio servicio) {
        this.servicios.add(servicio);
        servicio.setEstadia(this);
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

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
        if (reserva != null && reserva.getEstadia() != this) {
            reserva.setEstadia(this);
        }
    }

    // Getters y Setters Nuevos
    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }

    public List<Servicio> getServicios() { return servicios; }
    public void setServicios(List<Servicio> servicios) { this.servicios = servicios; }
}