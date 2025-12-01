package DTO;
import java.util.List;

public class CrearReservaDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    private List<ReservaItemDTO> items;
    // Getters y Setters...
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public List<ReservaItemDTO> getItems() { return items; }
    public void setItems(List<ReservaItemDTO> items) { this.items = items; }
}