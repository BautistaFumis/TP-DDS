package Persistencia.Repositorios;

import Logica.Dominio.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Este es el nuevo "DAO" de Spring Data.
 * Al extender JpaRepository, nos da gratis los métodos:
 * .save()
 * .findById()  <-- El que vamos a usar
 * .findAll()
 * .delete()
 * ¡Y muchos más!
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    // Se deja vacío a propósito. La magia la hace JpaRepository.

}