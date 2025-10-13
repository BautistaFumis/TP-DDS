package Persistencia;

import Clases.Huesped;

import java.util.Optional;

public interface HuespedDAO {

    void altaHuesped(Huesped huesped);

    // Aca usa un tipo Optional que a chequear
    Optional<Huesped> buscarHuesped(String tipoDocumento, int documento);

    void modificarHuesped(Huesped huesped);

    void bajaHuesped(int documento);
}