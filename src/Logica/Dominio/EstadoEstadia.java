package Logica.Dominio;

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
     * Intenta reabrir una estadía (si aplica). Por defecto, no hace nada o lanza error.
     * @param estadia La instancia de Estadia cuyo estado se está manejando.
     */
    void reabrirEstadia(Estadia estadia); // Ejemplo de otra acción dependiente del estado

    /**
     * Devuelve el tipo de estado actual como un Enum.
     * @return El valor Enum correspondiente al estado (ACTIVA o CERRADA).
     */
    TipoEstadoEstadia getTipoEstado();
}