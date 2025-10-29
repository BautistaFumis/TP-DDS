package Persistencia;

import Logica.Dominio.ResponsableDePago;

public interface ResponsableDePagoDAO {

    public void darAltaResponsableDePago(ResponsableDePago responsableDePago);
    public void darBajaResponsableDePago(ResponsableDePago responsableDePago);
    public void modificarResponsableDePago(ResponsableDePago responsableDePago);
}
