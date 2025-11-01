package Logica.Dominio;

import Logica.Dominio.Entidades.Direccion;

public class PersonaJuridica {
    private String nombre;
    private String apellido;
    private String razonSocial;
    private int  telefono;
    private Direccion direccion;
    private String cuit;

    public PersonaJuridica(String nombre, String apellido, String razonSocial, int telefono, Direccion direccion, String cuit) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.razonSocial = razonSocial;
        this.telefono = telefono;
        this.direccion = direccion;
        this.cuit = cuit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }
}
