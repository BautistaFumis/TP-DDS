package DTO;

public class CeldaEstadoDTO {
    private String idHabitacion; // Para keys de React
    private String numero;       // Lo que se muestra en el encabezado (01, 02...)
    private String estado;       // "LIBRE", "OCUPADA", "RESERVADA", "MANTENIMIENTO"
    private String texto;        // "Disponible", "Ocupada" (Texto visible)

    public CeldaEstadoDTO(String idHabitacion, String numero, String estado, String texto) {
        this.idHabitacion = idHabitacion;
        this.numero = numero;
        this.estado = estado;
        this.texto = texto;
    }

    // Getters
    public String getIdHabitacion() { return idHabitacion; }
    public String getNumero() { return numero; }
    public String getEstado() { return estado; }
    public String getTexto() { return texto; }
}