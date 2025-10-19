package Logica.Dominio;

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
    private List<Huesped> acompanantes;

    /**
     * Constructor para crear una nueva instancia de Estadia.
     * @param fechaCheckin La fecha de inicio de la estadía.
     * @param fechaCheckout La fecha de fin de la estadía.
     * @param huespedPrincipal El huésped responsable de la estadía.
     */
    public Estadia(LocalDate fechaCheckin, LocalDate fechaCheckout, Huesped huespedPrincipal) {
        this.fechaCheckin = fechaCheckin;
        this.fechaCheckout = fechaCheckout;
        this.huespedPrincipal = huespedPrincipal;
        this.acompanantes = new ArrayList<>();
    }

    /**
     * Agrega un huésped acompañante a la lista de la estadía.
     * @param huesped El huésped a agregar como acompañante.
     */
    public void agregarAcompanante(Huesped huesped) {
        this.acompanantes.add(huesped);
    }

    public LocalDate getFechaCheckin() { return fechaCheckin; }
    public void setFechaCheckin(LocalDate fechaCheckin) { this.fechaCheckin = fechaCheckin; }
    public LocalDate getFechaCheckout() { return fechaCheckout; }
    public void setFechaCheckout(LocalDate fechaCheckout) { this.fechaCheckout = fechaCheckout; }
    public Huesped getHuespedPrincipal() { return huespedPrincipal; }
    public void setHuespedPrincipal(Huesped huespedPrincipal) { this.huespedPrincipal = huespedPrincipal; }
    public List<Huesped> getAcompanantes() { return acompanantes; }
    public void setAcompanantes(List<Huesped> acompanantes) { this.acompanantes = acompanantes; }
}