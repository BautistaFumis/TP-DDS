package DTO;

public class UsuarioDTO {

    private String id;

    private String password;

    public UsuarioDTO() {
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