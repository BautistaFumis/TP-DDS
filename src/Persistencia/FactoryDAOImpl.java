package Persistencia;

public class FactoryDAOImpl implements DAOFactory {
    @Override
    public UsuarioDAO crearUsuarioDAO() {
        return new UsuarioDAOImpl();
    }

    @Override
    public HuespedDAO crearHuespedDAO() {
        return new HuespedDAOImpl();
    }

    @Override
    public EstadiaDAO crearEstadiaDAO() {
        return new EstadiaDAOImpl();
    }
}
