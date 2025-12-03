package Logica.Dominio.Entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "direcciones")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Necesario ahora que es entidad

    private String pais;
    private String provincia;
    private String localidad;
    private String codigoPostal;
    private String calle;
    private Integer numero;
    private String departamento;
    private Integer piso;

    public Direccion(){}

    public Direccion(String pais, String provincia, String localidad, String codigoPostal, String calle, Integer numero, String departamento, Integer piso) {
        this.pais = pais;
        this.provincia = provincia;
        this.localidad = localidad;
        this.codigoPostal = codigoPostal;
        this.calle = calle;
        this.numero = numero;
        this.departamento = departamento;
        this.piso = piso;
    }

    // Constructor de copia
    public Direccion(Direccion otra) {
        if (otra != null) {
            this.calle = otra.calle;
            this.numero = otra.numero;
            this.departamento = otra.departamento;
            this.piso = otra.piso;
            this.codigoPostal = otra.codigoPostal;
            this.localidad = otra.localidad;
            this.provincia = otra.provincia;
            this.pais = otra.pais;
        }
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public Integer getPiso() { return piso; }
    public void setPiso(Integer piso) { this.piso = piso; }
}