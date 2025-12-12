package DTO;

public class ItemFacturableDTO {
    private String descripcion;
    private Double monto;
    private boolean esEstadia; // true si es el costo de la habitación, false si es consumo

    public ItemFacturableDTO(String descripcion, Double monto, boolean esEstadia) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.esEstadia = esEstadia;
    }
    // Getters y Setters
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
    public boolean isEsEstadia() { return esEstadia; }
    public void setEsEstadia(boolean esEstadia) { this.esEstadia = esEstadia; }
}