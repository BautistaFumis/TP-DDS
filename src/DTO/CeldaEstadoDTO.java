package DTO;

public class CeldaEstadoDTO {
    private String idHabitacion;
    private String numero;
    private String estado;
    private String texto;
    private String tipoHabitacion; // <--- NUEVO CAMPO

    public CeldaEstadoDTO(String idHabitacion, String numero, String estado, String texto, String tipoHabitacion) {
        this.idHabitacion = idHabitacion;
        this.numero = numero;
        this.estado = estado;
        this.texto = texto;
        this.tipoHabitacion = tipoHabitacion;
    }

    public String getIdHabitacion() { return idHabitacion; }
    public String getNumero() { return numero; }
    public String getEstado() { return estado; }
    public String getTexto() { return texto; }
    public String getTipoHabitacion() { return tipoHabitacion; }
}