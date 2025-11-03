package Logica.Dominio.Entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
/**
 * Representa a un usuario del sistema con credenciales para la autenticación.
 * Esta clase almacena el identificador único (nombre de usuario) y la contraseña.
 */

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    private String id;

    private String password;

    public Usuario() {
    }

    /**
     * Constructor para crear una nueva instancia de Usuario.
     *
     * @param id El identificador único del usuario (nombre de usuario).
     * @param password La contraseña del usuario.
     */
    public Usuario(String id, String password) {
        this.id = id;
        this.password = password;
    }

    /**
     * Obtiene el ID del usuario.
     * @return El ID (nombre de usuario).
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el ID del usuario.
     * @param id El nuevo ID (nombre de usuario).
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return La contraseña.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * @param password La nueva contraseña.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}