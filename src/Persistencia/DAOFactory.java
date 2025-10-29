package Persistencia;

/**
 * Define el contrato para una fábrica abstracta de objetos DAO (Data Access Objects).
 * Esta interfaz desacopla la creación de instancias DAO de las clases cliente (como los Gestores),
 * permitiendo cambiar fácilmente la implementación de persistencia (ej: de CSV a base de datos)
 * sin modificar el código que utiliza los DAOs, por lo que creemos que el patron es muy acorde para su utilizacion posterior.
 */

public interface DAOFactory {

    /**
     * Crea y devuelve una instancia concreta de {@link UsuarioDAO}.
     * La implementación específica (ej: {@code UsuarioDAOImpl}) será determinada
     * por la subclase concreta de DAOFactory que se esté utilizando.
     *
     * @return Una implementación de la interfaz UsuarioDAO.
     */
    UsuarioDAO crearUsuarioDAO();

    /**
     * Crea y devuelve una instancia concreta de {@link HuespedDAO}.
     * La implementación específica (ej: {@code HuespedDAOImpl}) será determinada
     * por la subclase concreta de DAOFactory.
     *
     *
     * @return Una implementación de la interfaz HuespedDAO.
     */
    HuespedDAO crearHuespedDAO();

    /**
     * Crea y devuelve una instancia concreta de {@link EstadiaDAO}.
     * La implementación específica (ej: {@code EstadiaDAOImpl}) será determinada
     * por la subclase concreta de DAOFactory.
     *
     * @return Una implementación de la interfaz EstadiaDAO.
     */
    EstadiaDAO crearEstadiaDAO();
    NotaDeCreditoDAO crearNotaDeCreditoDAO();
    FacturaDAO crearFacturaDAO();
}