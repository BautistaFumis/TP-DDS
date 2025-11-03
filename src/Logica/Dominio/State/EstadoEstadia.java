package Logica.Dominio.State;

import Logica.Dominio.Entidades.Estadia;
import Logica.Dominio.Enum.TipoEstadoEstadia;

/**
 * Interfaz que define el comportamiento de una Estadia según su estado.
 * Las clases concretas implementarán las acciones específicas para cada estado.
 */
public interface EstadoEstadia {

    /**
     * Intenta cerrar la estadía. La lógica específica depende del estado actual.
     * @param estadia La instancia de Estadia cuyo estado se está manejando.
     */
    void cerrarEstadia(Estadia estadia);

    /**
     * Intenta reabrir una estadía (si aplica).
     * @param estadia La instancia de Estadia cuyo estado se está manejando.
     */
    void reabrirEstadia(Estadia estadia);

    /**
     * Devuelve el tipo de estado actual como un Enum.
     * Esto es crucial para sincronizar el estado de la LÓGICA
     * con el estado de los DATOS (el Enum) en la Entidad Estadia.
     *
     * @return El valor Enum correspondiente al estado (ACTIVA o CERRADA).
     */
    TipoEstadoEstadia getTipoEstado();
}