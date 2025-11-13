package DTO;

import Logica.Dominio.Entidades.Direccion; // Reutilizamos tu clase Direccion
import java.time.LocalDate;

/**
 * DTO para recibir los datos de un nuevo huésped desde el frontend.
 * Contiene todos los campos necesarios para crear una Entidad Huesped completa.
 */
public class HuespedAltaDTO {

    // Datos Personales
    private String nombre;
    private String apellido;
    private String email;
    private String tipoDocumento;
    private String documento;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String ocupacion;
    private String nacionalidad;

    // Datos Fiscales
    private String cuit;
    private String categoriaIVA;

    // Datos de Dirección
    // Incluimos el objeto Direccion directamente.
    // Spring/Jackson sabrá cómo "desarmar" un JSON anidado
    // { ..., "direccion": { "pais": "...", "provincia": "..." } }
    private Direccion direccion;

    // Constructor por defecto
    public HuespedAltaDTO() {
    }

    // --- Getters y Setters para todos los campos ---
    // (Spring los necesita para "poblar" el objeto desde el JSON)

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }

    public String getCategoriaIVA() { return categoriaIVA; }
    public void setCategoriaIVA(String categoriaIVA) { this.categoriaIVA = categoriaIVA; }

    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }
}