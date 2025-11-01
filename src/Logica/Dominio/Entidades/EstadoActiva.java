package Logica.Dominio.Entidades;

import Logica.Dominio.Enum.TipoEstadoEstadia;

import java.time.LocalDate;

/**
 * Representa el estado de una estadía que está actualmente en curso (Activa).
 */
public class EstadoActiva implements EstadoEstadia {

    /**
     * Cierra una estadía activa. Establece la fecha de checkout y cambia el estado a Cerrada.
     * @param estadia La estadía a cerrar.
     */
    @Override
    public void cerrarEstadia(Estadia estadia) {
        System.out.println("Cerrando la estadía...");
        if (estadia.getFechaCheckout() == null) {
            estadia.setFechaCheckout(LocalDate.now());
        }
        estadia.setEstadoInterno(new EstadoCerrada());
        System.out.println("Estadía cerrada con fecha: " + estadia.getFechaCheckout());
    }

    /**
     * No se puede reabrir una estadía que ya está activa.
     * @param estadia La estadía activa.
     */
    @Override
    public void reabrirEstadia(Estadia estadia) {
        System.out.println("La estadía ya está activa.");
    }

    /**
     * Devuelve el tipo de estado actual.
     * @return {@code TipoEstadoEstadia.ACTIVA}
     */
    @Override
    public TipoEstadoEstadia getTipoEstado() {
        return TipoEstadoEstadia.ACTIVA;
    }
}