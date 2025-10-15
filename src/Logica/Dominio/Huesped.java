package Logica.Dominio;

import java.time.LocalDate;
import java.util.Date;

public class Huesped {
    private String nombre;
    private String apellido;
    private String email;
    private String tipoDocumento;
    private Long telefono;
    private String documento; // Capaz habria que hacerlo LONG
    private Direccion direccion;
    private String cuit;
    private String categoriaIVA;
    private LocalDate fechaNacimiento;
    private String ocupacion;
    private String nacionalidad;

    public Huesped(){};

    public Huesped(String nombre, String apellido, String email, String tipoDocumento, Long telefono, String documento, Direccion direccion, String cuit, String categoriaIVA, LocalDate fechaNacimiento, String ocupacion, String nacionalidad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.tipoDocumento = tipoDocumento;
        this.telefono = telefono;
        this.documento = documento;
        this.direccion = direccion;
        this.cuit = cuit;
        this.categoriaIVA = categoriaIVA;
        this.fechaNacimiento = fechaNacimiento;
        this.ocupacion = ocupacion;
        this.nacionalidad = nacionalidad;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoDocumento() {

        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {

        this.tipoDocumento = tipoDocumento;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {

        this.telefono = telefono;
    }

    public String getDocumento() {

        return documento;
    }

    public void setDocumento(String documento) {

        this.documento = documento;
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

    public String getCategoriaIVA() {
        return categoriaIVA;
    }

    public void setCategoriaIVA(String categoriaIVA) {
        this.categoriaIVA = categoriaIVA;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
}