package Clases;

public class Direccion {
    private String pais;
    private String provincia;
    private String localidad;
    private int codigoPostal;
    private String calle;
    private int numero;
    private String departamento;
    private int piso;

    public Direccion(){};
    public Direccion(String pais, String provincia, String localidad, int codigoPostal, String calle, int numero, String departamento, int piso) {
        this.pais = pais;
        this.provincia = provincia;
        this.localidad = localidad;
        this.codigoPostal = codigoPostal;
        this.calle = calle;
        this.numero = numero;
        this.departamento = departamento;
        this.piso = piso;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }
}