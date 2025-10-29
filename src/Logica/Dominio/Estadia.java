package Logica.Dominio;

import Logica.Dominio.Enum.TipoEstadoEstadia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el período de permanencia de uno o más huéspedes en el hotel.
 *
 */
public class Estadia {
    private LocalDate fechaCheckin;
    private LocalDate fechaCheckout;
    private Huesped huespedPrincipal;
    private List<Huesped> acompanantes; // no esta implementada la logica para cargar acompanantes por el momento, por lo que solo se trabaja con un solo huesped
    private EstadoEstadia estado;

    /**
     * Constructor para crear una nueva instancia de Estadia.
     * @param fechaCheckin La fecha de inicio de la estadía.
     * @param huespedPrincipal El huésped responsable de la estadía.
     */

    public Estadia(LocalDate fechaCheckin, Huesped huespedPrincipal) {
        this.fechaCheckin = fechaCheckin;
        this.fechaCheckout = null;
        this.huespedPrincipal = huespedPrincipal;
        this.acompanantes = new ArrayList<>(); //no vamos a tener en cuenta en esta entrega para eliminar los acompanantes, ya que no nos lo pide explicitamente
        setEstadoInterno(new EstadoActiva());
    }

    /**
     * Intenta cerrar la estadía. El comportamiento depende del estado actual.
     */
    public void cerrar() {
        this.estado.cerrarEstadia(this);
    }

    /**
     * Intenta reabrir la estadía. El comportamiento depende del estado actual.
     */
    public void reabrir() {
        this.estado.reabrirEstadia(this);
    }


    /**
     * Solo las clases de estado deberían llamarlo.
     * @param nuevoEstado El nuevo objeto de estado.
     */
    void setEstadoInterno(EstadoEstadia nuevoEstado) {
        this.estado = nuevoEstado;
        System.out.println("--> Nuevo estado de la estadía: " + nuevoEstado.getClass().getSimpleName());
    }

    /**
     * Obtiene el tipo de estado actual de la estadía.
     * @return El valor Enum {@link TipoEstadoEstadia} correspondiente.
     */
    public TipoEstadoEstadia getTipoEstado() {
        return this.estado.getTipoEstado();
    }

    /**
     * Agrega un huésped acompañante a la lista de la estadía.
     * @param huesped El huésped a agregar como acompañante.
     */
    public void agregarAcompanante(Huesped huesped) {
        this.acompanantes.add(huesped);
    }
    /**
     * Obtiene la fecha de check-in de la estadía.
     *
     * @return La fecha en la que comenzó la estadía.
     */
    public LocalDate getFechaCheckin() { return fechaCheckin; }

    /**
     * Establece la fecha de check-in de la estadía.
     *
     * @param fechaCheckin La nueva fecha de inicio de la estadía.
     */
    public void setFechaCheckin(LocalDate fechaCheckin) { this.fechaCheckin = fechaCheckin; }

    /**
     * Obtiene la fecha de check-out de la estadía.
     *
     * @return La fecha en la que finalizó o finalizará la estadía.
     */
    public LocalDate getFechaCheckout() { return fechaCheckout; }

    /**
     * Establece la fecha de check-out de la estadía.
     *
     * @param fechaCheckout La nueva fecha de fin de la estadía.
     */
    public void setFechaCheckout(LocalDate fechaCheckout) { this.fechaCheckout = fechaCheckout; }

    /**
     * Obtiene el huésped principal responsable de la estadía.
     *
     * @return El objeto {@link Huesped} que figura como principal.
     */
    public Huesped getHuespedPrincipal() { return huespedPrincipal; }

    /**
     * Establece o cambia el huésped principal de la estadía.
     *
     * @param huespedPrincipal El nuevo objeto {@link Huesped} que será el principal.
     */
    public void setHuespedPrincipal(Huesped huespedPrincipal) { this.huespedPrincipal = huespedPrincipal; }

    /**
     * Obtiene la lista de huéspedes acompañantes de la estadía.
     *
     * @return Una {@link List} que contiene los objetos {@link Huesped} de los acompañantes.
     * Puede ser una lista vacía si no hay acompañantes.
     */
    public List<Huesped> getAcompanantes() { return acompanantes; }

    /**
     * Establece la lista completa de huéspedes acompañantes.
     * Reemplaza cualquier lista de acompañantes existente.
     *
     * @param acompanantes La nueva {@link List} de objetos {@link Huesped} acompañantes.
     */
    public void setAcompanantes(List<Huesped> acompanantes) { this.acompanantes = acompanantes; }
}