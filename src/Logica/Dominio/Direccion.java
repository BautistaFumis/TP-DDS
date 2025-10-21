package Logica.Dominio;

/**
 * Representa una dirección física con todos sus componentes.
 */
public class Direccion {
    private String pais;
    private String provincia;
    private String localidad;
    private String codigoPostal; // Hay ciudades que el CP tiene letra
    private String calle;
    private Integer numero;
    private String departamento;
    private Integer piso;

    /**
     * Constructor por defecto.
     * Crea una instancia de Direccion con todos sus campos inicializados a sus valores por defecto.
     */
    public Direccion(){};

    /**
     * Constructor con todos los parámetros.
     * Crea una instancia de Direccion con todos sus atributos especificados.
     *
     * @param pais El país de la dirección.
     * @param provincia La provincia o estado de la dirección.
     * @param localidad La ciudad o localidad de la dirección.
     * @param codigoPostal El código postal numérico.
     * @param calle El nombre de la calle.
     * @param numero El número de la dirección en la calle.
     * @param departamento El identificador del departamento (puede ser número o letra).
     * @param piso El número del piso.
     */
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

    /**
     * Constructor de copia.
     * Crea una nueva instancia de Direccion a partir de otra instancia existente.
     *
     * @param otra La instancia de Direccion a copiar.
     */
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

    /**
     * Obtiene el país.
     * @return El país de la dirección.
     */
    public String getPais() {
        return pais;
    }

    /**
     * Establece el país.
     * @param pais El nuevo país de la dirección.
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /**
     * Obtiene la provincia.
     * @return La provincia de la dirección.
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Establece la provincia.
     * @param provincia La nueva provincia de la dirección.
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * Obtiene la localidad.
     * @return La localidad de la dirección.
     */
    public String getLocalidad() {
        return localidad;
    }

    /**
     * Establece la localidad.
     * @param localidad La nueva localidad de la dirección.
     */
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    /**
     * Obtiene el código postal.
     * @return El código postal de la dirección.
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Establece el código postal.
     * @param codigoPostal El nuevo código postal de la dirección.
     */
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * Obtiene el nombre de la calle.
     * @return El nombre de la calle.
     */
    public String getCalle() {
        return calle;
    }

    /**
     * Establece el nombre de la calle.
     * @param calle El nuevo nombre de la calle.
     */
    public void setCalle(String calle) {
        this.calle = calle;
    }

    /**
     * Obtiene el número de la dirección.
     * @return El número de la dirección.
     */
    public Integer getNumero() {
        return numero;
    }

    /**
     * Establece el número de la dirección.
     * @param numero El nuevo número de la dirección.
     */
    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    /**
     * Obtiene el departamento.
     * @return El identificador del departamento.
     */
    public String getDepartamento() {
        return departamento;
    }

    /**
     * Establece el departamento.
     * @param departamento El nuevo identificador del departamento.
     */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    /**
     * Obtiene el piso.
     * @return El número del piso.
     */
    public Integer getPiso() {
        return piso;
    }

    /**
     * Establece el piso.
     * @param piso El nuevo número del piso.
     */
    public void setPiso(Integer piso) {
        this.piso = piso;
    }
}