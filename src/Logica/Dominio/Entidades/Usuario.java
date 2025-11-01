package Logica.Dominio.Entidades;

// <-- NUEVO: Importaciones de JPA (Java Persistence API)
// <-- VERDE: Estos son los nuevos nombres
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
/**
 * Representa a un usuario del sistema con credenciales para la autenticación.
 * Esta clase almacena el identificador único (nombre de usuario) y la contraseña.
 */

// <-- NUEVO: @Entity le dice a Spring que esta clase es una tabla
@Entity
// <-- NUEVO: @Table le da el nombre "usuarios" a la tabla en la BD
@Table(name = "usuarios")
public class Usuario {

    // <-- NUEVO: @Id le dice a Spring que este campo es la Clave Primaria
    @Id
    private String id;

    private String password;

    // <-- NUEVO: JPA necesita OBLIGATORIAMENTE un constructor vacío
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