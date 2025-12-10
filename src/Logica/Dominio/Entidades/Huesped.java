package Logica.Dominio.Entidades;

import com.fasterxml.jackson.annotation.JsonIgnore; // <--- IMPORTANTE: Nueva importación
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "huespedes")
public class Huesped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String email;
    private String tipoDocumento;
    private String documento;
    private String telefono;

    /**
     * Relación con Estadia.
     * AGREGAMOS @JsonIgnore para evitar el bucle infinito al traer el huésped.
     * Esto permite cargar el formulario de modificación sin errores.
     */
    @JsonIgnore // <--- ESTA LÍNEA SOLUCIONA EL PROBLEMA
    @ManyToMany(mappedBy = "huespedes")
    private List<Estadia> estadias;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "direccion_id", referencedColumnName = "id")
    private Direccion direccion;

    private String cuit;
    private String categoriaIVA;
    private LocalDate fechaNacimiento;
    private String ocupacion;
    private String nacionalidad;

    public Huesped(){}

    public Huesped(Long id, String nombre, String apellido, String email, String tipoDocumento, String documento, String telefono, Direccion direccion, String cuit, String categoriaIVA, LocalDate fechaNacimiento, String ocupacion, String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.telefono = telefono;
        this.direccion = direccion;
        this.cuit = cuit;
        this.categoriaIVA = categoriaIVA;
        this.fechaNacimiento = fechaNacimiento;
        this.ocupacion = ocupacion;
        this.nacionalidad = nacionalidad;
    }

    public Huesped(Huesped otro) {
        if (otro != null) {
            this.nombre = otro.nombre;
            this.apellido = otro.apellido;
            this.email = otro.email;
            this.tipoDocumento = otro.tipoDocumento;
            this.telefono = otro.telefono;
            this.documento = otro.documento;

            if (otro.direccion != null) {
                this.direccion = new Direccion(otro.direccion);
            }

            this.cuit = otro.cuit;
            this.categoriaIVA = otro.categoriaIVA;
            this.fechaNacimiento = otro.fechaNacimiento;
            this.ocupacion = otro.ocupacion;
            this.nacionalidad = otro.nacionalidad;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }

    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }

    public String getCategoriaIVA() { return categoriaIVA; }
    public void setCategoriaIVA(String categoriaIVA) { this.categoriaIVA = categoriaIVA; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public List<Estadia> getEstadias() { return estadias; }
    public void setEstadias(List<Estadia> estadias) { this.estadias = estadias; }
}