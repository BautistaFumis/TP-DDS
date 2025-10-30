package Persistencia;

import Logica.Dominio.Pago;


public interface PagoDAO {
    void darAltaPago(Pago pago);
    void darBajaPago(Pago pago);
    void modificarPago(Pago pago);

}
