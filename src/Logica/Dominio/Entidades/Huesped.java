package Logica.Dominio.Entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
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

    // Getters y Setters standard...
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


    public static HuespedBuilder builder() {
        return new HuespedBuilder();
    }

    public static class HuespedBuilder {
        private final Huesped huesped;

        public HuespedBuilder() {
            this.huesped = new Huesped();
            this.huesped.setDireccion(new Direccion()); // Instanciamos para evitar NullPointer
        }


        public HuespedBuilder nombre(String nombre) {
            this.huesped.setNombre(nombre);
            return this;
        }
        public HuespedBuilder apellido(String apellido) {
            this.huesped.setApellido(apellido);
            return this;
        }
        public HuespedBuilder tipoDocumento(String tipo) {
            this.huesped.setTipoDocumento(tipo);
            return this;
        }
        public HuespedBuilder documento(String documento) {
            this.huesped.setDocumento(documento);
            return this;
        }


        public HuespedBuilder email(String email) {
            this.huesped.setEmail(email);
            return this;
        }
        public HuespedBuilder telefono(String telefono) {
            this.huesped.setTelefono(telefono);
            return this;
        }


        public HuespedBuilder cuit(String cuit) {
            this.huesped.setCuit(cuit);
            return this;
        }
        public HuespedBuilder categoriaIVA(String categoriaIVA) {
            this.huesped.setCategoriaIVA(categoriaIVA);
            return this;
        }


        public HuespedBuilder fechaNacimiento(LocalDate fecha) {
            this.huesped.setFechaNacimiento(fecha);
            return this;
        }
        public HuespedBuilder nacionalidad(String nacionalidad) {
            this.huesped.setNacionalidad(nacionalidad);
            return this;
        }
        public HuespedBuilder ocupacion(String ocupacion) {
            this.huesped.setOcupacion(ocupacion);
            return this;
        }


        public HuespedBuilder calle(String calle) {
            this.huesped.getDireccion().setCalle(calle);
            return this;
        }
        public HuespedBuilder numero(Integer numero) {
            this.huesped.getDireccion().setNumero(numero);
            return this;
        }
        public HuespedBuilder localidad(String localidad) {
            this.huesped.getDireccion().setLocalidad(localidad);
            return this;
        }
        public HuespedBuilder provincia(String provincia) {
            this.huesped.getDireccion().setProvincia(provincia);
            return this;
        }
        public HuespedBuilder pais(String pais) {
            this.huesped.getDireccion().setPais(pais);
            return this;
        }
        public HuespedBuilder codigoPostal(String cp) {
            this.huesped.getDireccion().setCodigoPostal(cp);
            return this;
        }
        public HuespedBuilder departamento(String depto) {
            this.huesped.getDireccion().setDepartamento(depto);
            return this;
        }
        public HuespedBuilder piso(Integer piso) {
            this.huesped.getDireccion().setPiso(piso);
            return this;
        }

        public Huesped build() {
            return this.huesped;
        }
    }
}