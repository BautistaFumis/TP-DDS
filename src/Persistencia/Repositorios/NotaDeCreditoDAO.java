package Persistencia.Repositorios;

import Logica.Dominio.Entidades.NotaDeCredito;

public interface NotaDeCreditoDAO{

    public void crearNotaDeCredito(NotaDeCredito nota);
    public void modificarNotaDeCredito(NotaDeCredito nota);
    public void eliminarNotaDeCredito(String numeroNota);
}
