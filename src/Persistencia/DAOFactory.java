package Persistencia;

public interface DAOFactory {

    UsuarioDAO crearUsuarioDAO();
    HuespedDAO crearHuespedDAO();
    EstadiaDAO crearEstadiaDAO();
}
