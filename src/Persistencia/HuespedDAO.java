package Persistencia;

import Logica.Dominio.Huesped;

import java.util.List;
import java.util.Optional;

public interface HuespedDAO {

    void altaHuesped(Huesped huesped); //Agregar un Huesped al archivo

    // Aca usa un tipo Optional que a chequear
    Optional<Huesped> buscarHuesped(String tipoDocumento, String documento);

    void modificarHuesped(Huesped huesped);

    void bajaHuesped(int documento);


    /**
     * Busca huéspedes cuyos nombres o apellidos comiencen con los criterios dados.
     * @param apellido Criterio de búsqueda para el apellido (ignora si es nulo o vacío).
     * @param nombre Criterio de búsqueda para el nombre (ignora si es nulo o vacío).
     * @return Una lista de huéspedes que coinciden con los criterios.
     */
    List<Huesped> buscarPorCriterios(String apellido, String nombre, String tipoDocumento, String documento);
}