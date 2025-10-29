package Persistencia;

/**
 * Implementación concreta de la interfaz {@link DAOFactory}.
 * Esta fábrica se especializa en crear instancias de DAOs que manejan la persistencia
 * de datos utilizando archivos CSV (ej: {@code HuespedDAOImpl}, {@code UsuarioDAOImpl}).
 *
 */

public class FactoryDAOImpl implements DAOFactory {

    /**
     * Devuelve una nueva instancia de {@link UsuarioDAOImpl}, que trabaja con {@code usuarios.csv}.
     */
    @Override
    public UsuarioDAO crearUsuarioDAO() {
        return new UsuarioDAOImpl();
    }

    /**
     * Devuelve una nueva instancia de {@link HuespedDAOImpl}, que trabaja con {@code huespedes.csv}.
     */

    @Override
    public HuespedDAO crearHuespedDAO() {
        return new HuespedDAOImpl();
    }

    /**
     * Devuelve una nueva instancia de {@link EstadiaDAOImpl}, que trabaja con {@code estadias.csv}.
     */
    
    @Override
    public EstadiaDAO crearEstadiaDAO() {
        return new EstadiaDAOImpl();
    }
    @Override
    public NotaDeCreditoDAO crearNotaDeCreditoDAO() {
        return new NotaDeCreditoDAOImpl();
    }
    @Override
    public FacturaDAO crearFacturaDAO() {
        return new FacturaDAOImpl();
    }
}