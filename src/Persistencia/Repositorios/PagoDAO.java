package Persistencia.Repositorios;

import Logica.Dominio.Entidades.Pago;


public interface PagoDAO {
    void darAltaPago(Pago pago);
    void darBajaPago(Pago pago);
    void modificarPago(Pago pago);

}
