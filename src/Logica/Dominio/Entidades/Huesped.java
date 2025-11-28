package Logica.Dominio.Entidades;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

/**
 * Representa la entidad principal del dominio: un huésped del hotel.
 * Con @Entity, esta clase se convierte en una tabla de base de datos.
 */
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
     * "mappedBy" indica que la configuración de la tabla intermedia
     * está en la clase Estadia (en el campo "huespedes").
     */
    @ManyToMany(mappedBy = "huespedes")
    private List<Estadia> estadias;

    /**
     * Relación con Direccion.
     * Ahora es una tabla separada. CascadeType.ALL permite que si guardas/borras
     * al huésped, pase lo mismo con su dirección asociada.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "direccion_id", referencedColumnName = "id")
    private Direccion direccion;

    private String cuit;
    private String categoriaIVA;
    private LocalDate fechaNacimiento;
    private String ocupacion;
    private String nacionalidad;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Huesped(){}

    /**
     * Constructor completo.
     */
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

    /**
     * Constructor de copia.
     * Útil para crear un nuevo objeto basado en otro sin copiar el ID.
     */
    public Huesped(Huesped otro) {
        if (otro != null) {
            // NO copiamos el ID, porque este es un nuevo registro
            this.nombre = otro.nombre;
            this.apellido = otro.apellido;
            this.email = otro.email;
            this.tipoDocumento = otro.tipoDocumento;
            this.telefono = otro.telefono;
            this.documento = otro.documento;

            // Copiamos la dirección usando su propio constructor de copia
            // para evitar referencias cruzadas al mismo objeto en memoria.
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

    // --- GETTERS Y SETTERS ---

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

    /**
     * Obtiene la entidad Dirección asociada.
     */
    public Direccion getDireccion() { return direccion; }

    /**
     * Establece la entidad Dirección.
     */
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

    /**
     * Obtiene el historial de estadías de este huésped.
     */
    public List<Estadia> getEstadias() { return estadias; }

    /**
     * Establece el historial de estadías.
     */
    public void setEstadias(List<Estadia> estadias) { this.estadias = estadias; }
}