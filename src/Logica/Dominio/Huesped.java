package Logica.Dominio;

import java.time.LocalDate;

/**
 * Representa la entidad principal del dominio: un huésped del hotel.
 * Esta clase contiene toda la información personal, de contacto y fiscal
 * de una persona que se aloja o se ha alojado en el hotel.
 */
public class Huesped {
    private String nombre;
    private String apellido;
    private String email;
    private String tipoDocumento;
    private String telefono;
    private String documento;
    private Direccion direccion;
    private String cuit;
    private String categoriaIVA;
    private LocalDate fechaNacimiento;
    private String ocupacion;
    private String nacionalidad;

    /**
     * Constructor por defecto.
     * Crea una instancia de Huesped con todos sus campos nulos o con valores por defecto.
     */
    public Huesped(){};

    /**
     * Constructor con todos los parámetros para crear un huésped completo.
     *
     * @param nombre El nombre de pila del huésped.
     * @param apellido El apellido del huésped.
     * @param email La dirección de correo electrónico.
     * @param tipoDocumento El tipo de documento (ej: DNI, PASAPORTE).
     * @param telefono El número de teléfono de contacto.
     * @param documento El número de identificación del documento.
     * @param direccion El objeto {@link Direccion} con los datos de su domicilio.
     * @param cuit El CUIT del huésped (opcional).
     * @param categoriaIVA La categoría fiscal frente al IVA.
     * @param fechaNacimiento La fecha de nacimiento del huésped.
     * @param ocupacion La profesión u ocupación del huésped.
     * @param nacionalidad La nacionalidad del huésped.
     */
    public Huesped(String nombre, String apellido, String email, String tipoDocumento, String telefono, String documento, Direccion direccion, String cuit, String categoriaIVA, LocalDate fechaNacimiento, String ocupacion, String nacionalidad) {
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

    /**
     * Constructor de copia.
     * Crea una nueva instancia de Huesped a partir de otra instancia existente,
     * duplicando todos sus atributos. Realiza una copia profunda de la dirección.
     *
     * @param otro La instancia de Huesped a copiar.
     */
    public Huesped(Huesped otro) {
        if (otro != null) {
            this.nombre = otro.nombre;
            this.apellido = otro.apellido;
            this.email = otro.email;
            this.tipoDocumento = otro.tipoDocumento;
            this.telefono = otro.telefono;
            this.documento = otro.documento;
            this.direccion = new Direccion(otro.direccion); // Llama al constructor de copia de Direccion
            this.cuit = otro.cuit;
            this.categoriaIVA = otro.categoriaIVA;
            this.fechaNacimiento = otro.fechaNacimiento;
            this.ocupacion = otro.ocupacion;
            this.nacionalidad = otro.nacionalidad;
        }
    }

    /**
     * Obtiene el nombre del huésped.
     * @return El nombre del huésped.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del huésped.
     * @param nombre El nuevo nombre del huésped.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del huésped.
     * @return El apellido del huésped.
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido del huésped.
     * @param apellido El nuevo apellido del huésped.
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene el email del huésped.
     * @return La dirección de correo electrónico.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email del huésped.
     * @param email La nueva dirección de correo electrónico.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el tipo de documento del huésped.
     * @return El tipo de documento (ej: "DNI").
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Establece el tipo de documento del huésped.
     * @param tipoDocumento El nuevo tipo de documento.
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * Obtiene el número de teléfono del huésped.
     * @return El número de teléfono.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del huésped.
     * @param telefono El nuevo número de teléfono.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el número de documento del huésped.
     * @return El número de documento.
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * Establece el número de documento del huésped.
     * @param documento El nuevo número de documento.
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     * Obtiene el objeto Dirección del huésped.
     * @return La dirección del huésped.
     */
    public Direccion getDireccion() {
        return direccion;
    }

    /**
     * Establece el objeto Dirección del huésped.
     * @param direccion La nueva dirección del huésped.
     */
    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene el CUIT del huésped.
     * @return El número de CUIT.
     */
    public String getCuit() {
        return cuit;
    }

    /**
     * Establece el CUIT del huésped.
     * @param cuit El nuevo número de CUIT.
     */
    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    /**
     * Obtiene la categoría de IVA del huésped.
     * @return La categoría fiscal frente al IVA (ej: "Consumidor Final").
     */
    public String getCategoriaIVA() {
        return categoriaIVA;
    }

    /**
     * Establece la categoría de IVA del huésped.
     * @param categoriaIVA La nueva categoría fiscal.
     */
    public void setCategoriaIVA(String categoriaIVA) {
        this.categoriaIVA = categoriaIVA;
    }

    /**
     * Obtiene la fecha de nacimiento del huésped.
     * @return La fecha de nacimiento.
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Establece la fecha de nacimiento del huésped.
     * @param fechaNacimiento La nueva fecha de nacimiento.
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Obtiene la ocupación del huésped.
     * @return La ocupación o profesión.
     */
    public String getOcupacion() {
        return ocupacion;
    }

    /**
     * Establece la ocupación del huésped.
     * @param ocupacion La nueva ocupación o profesión.
     */
    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    /**
     * Obtiene la nacionalidad del huésped.
     * @return La nacionalidad.
     */
    public String getNacionalidad() {
        return nacionalidad;
    }

    /**
     * Establece la nacionalidad del huésped.
     * @param nacionalidad La nueva nacionalidad.
     */
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
}