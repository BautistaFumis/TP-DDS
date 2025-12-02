package DTO;

import java.time.LocalDate;
import java.util.List;

public class CrearOcupacionDTO {
    private Long idHabitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    // Modificado: Ahora recibimos una lista de IDs (Responsable + Acompañantes)
    private List<Long> idsHuespedes;
    private boolean esOverrideReserva;

    // Getters y Setters
    public Long getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Long idHabitacion) { this.idHabitacion = idHabitacion; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public List<Long> getIdsHuespedes() { return idsHuespedes; }
    public void setIdsHuespedes(List<Long> idsHuespedes) { this.idsHuespedes = idsHuespedes; }

    public boolean isEsOverrideReserva() { return esOverrideReserva; }
    public void setEsOverrideReserva(boolean esOverrideReserva) { this.esOverrideReserva = esOverrideReserva; }
}