package Persistencia.Repositorios;

import Logica.Dominio.Entidades.Factura;

public interface FacturaDAO {
    void modificarFactura(Factura factura);
    void darAltaFactura(Factura factura);
    void darBajaFactura(Factura factura);
}
