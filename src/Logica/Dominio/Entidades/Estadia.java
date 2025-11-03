package Logica.Dominio.Entidades;

import Logica.Dominio.Enum.TipoEstadoEstadia;
import Logica.Dominio.State.EstadoActiva; // <-- Asume que moviste tus clases State
import Logica.Dominio.State.EstadoCerrada; // <-- Asume que moviste tus clases State
import Logica.Dominio.State.EstadoEstadia; // <-- Asume que moviste tus clases State

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
    @JoinColumn(name = "huesped_id")
    private Huesped huespedPrincipal;

    private LocalDate fechaCheckin;
    private LocalDate fechaCheckout;

    @Transient // Le dice a JPA: "Ignora este campo, no es parte de la base de datos"
    private List<Huesped> acompanantes;

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
        this.tipoEstado = nuevoEstado.getTipoEstado(); // Sincroniza el Enum
    }

    public Estadia() {}

    public Estadia(LocalDate fechaCheckin, Huesped huespedPrincipal) {
        this.fechaCheckin = fechaCheckin;
        this.huespedPrincipal = huespedPrincipal;
        this.acompanantes = new ArrayList<>();
        setEstadoInterno(new EstadoActiva()); // Inicia el estado
    }

    public void cerrar() {
        if (this.estadoLogic == null) reconstruirEstado(); // Seguridad
        this.estadoLogic.cerrarEstadia(this);
    }

    public void reabrir() {
        if (this.estadoLogic == null) reconstruirEstado(); // Seguridad
        this.estadoLogic.reabrirEstadia(this);
    }

    public TipoEstadoEstadia getTipoEstado() { return this.tipoEstado; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Huesped getHuespedPrincipal() { return huespedPrincipal; }
    public void setHuespedPrincipal(Huesped huespedPrincipal) { this.huespedPrincipal = huespedPrincipal; }
    public LocalDate getFechaCheckin() { return fechaCheckin; }
    public void setFechaCheckin(LocalDate fechaCheckin) { this.fechaCheckin = fechaCheckin; }
    public LocalDate getFechaCheckout() { return fechaCheckout; }
    public void setFechaCheckout(LocalDate fechaCheckout) { this.fechaCheckout = fechaCheckout; }
    public List<Huesped> getAcompanantes() { return acompanantes; }
    public void setAcompanantes(List<Huesped> acompanantes) { this.acompanantes = acompanantes; }
}