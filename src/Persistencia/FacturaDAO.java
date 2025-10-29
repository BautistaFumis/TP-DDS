package Persistencia;

import Logica.Dominio.Factura;

public interface FacturaDAO {
    void modificarFactura(Factura factura);
    void darAltaFactura(Factura factura);
    void darBajaFactura(Factura factura);
}
