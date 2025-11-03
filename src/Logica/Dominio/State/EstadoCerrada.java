package Logica.Dominio.State;

import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Enum.TipoEstadoEstadia;

/**
 * Representa el estado de una estadía que ya ha finalizado (Cerrada).
 */
public class EstadoCerrada implements EstadoEstadia {

    /**
     * No se puede cerrar una estadía que ya está cerrada. Muestra un mensaje informativo.
     * @param estadia La estadía cerrada.
     */
    @Override
    public void cerrarEstadia(Estadia estadia) {
        System.out.println("La estadía ya se encuentra cerrada.");
    }

    /**
     * Permite reabrir una estadía cerrada (ej: si hubo un error).
     * Cambia el estado a Activa.
     * @param estadia La estadía a reabrir.
     */
    @Override
    public void reabrirEstadia(Estadia estadia) {
        System.out.println("Reabriendo la estadía...");

        estadia.setEstadoInterno(new EstadoActiva());

        System.out.println("Estadía reabierta (ahora está Activa).");
    }

    /**
     * Devuelve el tipo de estado actual.
     * @return {@code TipoEstadoEstadia.CERRADA}
     */
    @Override
    public TipoEstadoEstadia getTipoEstado() {
        return TipoEstadoEstadia.CERRADA;
    }
}