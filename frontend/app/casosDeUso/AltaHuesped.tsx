'use client';
import { useState, FormEvent, useRef } from 'react';


interface AltaHuespedProps {
    onCancel: () => void;
}

const API_REGISTER_URL = 'http://localhost:8081/api/huespedes/registrar';


const estadoInicialFormulario = {
    nombre: '',
    apellido: '',
    email: '',
    tipoDocumento: 'DNI',
    documento: '',
    telefono: '',
    fechaNacimiento: '',
    ocupacion: '',
    nacionalidad: '',
    cuit: '',
    categoriaIVA: 'Consumidor Final',
    direccion: {
        pais: '',
        provincia: '',
        localidad: '',
        calle: '',
        numero: '',
        piso: '',
        departamento: '',
        codigoPostal: '',
    }
};


export default function AltaHuesped({ onCancel }: AltaHuespedProps) {


    const [formData, setFormData] = useState(estadoInicialFormulario);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [showCancelModal, setShowCancelModal] = useState(false);
    const [showDuplicateModal, setShowDuplicateModal] = useState(false);
    const [duplicateErrorMsg, setDuplicateErrorMsg] = useState('');
    const [showSuccessModal, setShowSuccessModal] = useState(false);
    const [successHuespedName, setSuccessHuespedName] = useState('');
    const [errorCampos, setErrorCampos] = useState<string[]>([]);


    const nombreRef = useRef<HTMLInputElement>(null);
    const apellidoRef = useRef<HTMLInputElement>(null);
    const tipoDocumentoRef = useRef<HTMLSelectElement>(null);
    const documentoRef = useRef<HTMLInputElement>(null);
    const fechaNacimientoRef = useRef<HTMLInputElement>(null);
    const telefonoRef = useRef<HTMLInputElement>(null);
    const nacionalidadRef = useRef<HTMLInputElement>(null);
    const ocupacionRef = useRef<HTMLInputElement>(null);
    const paisRef = useRef<HTMLInputElement>(null);
    const provinciaRef = useRef<HTMLInputElement>(null);
    const localidadRef = useRef<HTMLInputElement>(null);
    const calleRef = useRef<HTMLInputElement>(null);
    const numeroRef = useRef<HTMLInputElement>(null);
    const codigoPostalRef = useRef<HTMLInputElement>(null);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        const camposSinMayusculas = ['email', 'tipoDocumento', 'categoriaIVA'];
        const valorProcesado = camposSinMayusculas.includes(name) ? value : value.toUpperCase();
        
        setFormData(prev => ({ ...prev, [name]: valorProcesado }));


        if (errorCampos.includes(name)) {
            setErrorCampos(prevErrores => prevErrores.filter(campo => campo !== name));
        }
    };


    const handleDireccionChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            direccion: { ...prev.direccion, [name]: value.toUpperCase() }
        }));

        if (errorCampos.includes(name)) {
            setErrorCampos(prevErrores => prevErrores.filter(campo => campo !== name));
        }
    };

    const handleCorregirDuplicado = () => {
        setShowDuplicateModal(false); 
        setErrorCampos(['tipoDocumento', 'documento']); 
        tipoDocumentoRef.current?.focus();
    };

   const validarFormulario = (): string[] => {
       const {
           nombre,
           apellido,
           documento,
           telefono,
           fechaNacimiento,
           ocupacion,
           nacionalidad,
           cuit,
           direccion
       } = formData;

       const errores: string[] = [];

       // Solo letras
       const regexSoloLetras = /^[A-ZÁÉÍÓÚÜÑ\s]+$/i;

       //  Solo números
       const regexSoloNumeros = /^[0-9]+$/;


       if (!nombre) errores.push('nombre');
       else if (!regexSoloLetras.test(nombre)) errores.push('nombre');


       if (!apellido) errores.push('apellido');
       else if (!regexSoloLetras.test(apellido)) errores.push('apellido');


       if (!telefono) errores.push('telefono');
       else if (!regexSoloNumeros.test(telefono)) errores.push('telefono');


       if (!documento) errores.push('documento');


       if (!fechaNacimiento) errores.push('fechaNacimiento');


       if (!ocupacion) errores.push('ocupacion');
       else if (!regexSoloLetras.test(ocupacion)) errores.push('ocupacion');


       if (!nacionalidad) errores.push('nacionalidad');
       else if (!regexSoloLetras.test(nacionalidad)) errores.push('nacionalidad');


       if (!direccion.pais) errores.push('pais');
       else if (!regexSoloLetras.test(direccion.pais)) errores.push('pais');


       if (!direccion.provincia) errores.push('provincia');
       else if (!regexSoloLetras.test(direccion.provincia)) errores.push('provincia');


       if (!direccion.localidad) errores.push('localidad');
       else if (!regexSoloLetras.test(direccion.localidad)) errores.push('localidad');


       if (!direccion.calle) errores.push('calle');
       else if (!regexSoloLetras.test(direccion.calle)) errores.push('calle');

       if (!direccion.numero) errores.push('numero');
       else if (!regexSoloNumeros.test(direccion.numero)) errores.push('numero');


       if (direccion.piso && !regexSoloNumeros.test(direccion.piso)) errores.push('piso');

       if (!direccion.codigoPostal) errores.push('codigoPostal');
       else if (!regexSoloNumeros.test(direccion.codigoPostal)) errores.push('codigoPostal');

       return errores;
   };



    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setError(null);
        setErrorCampos([]);
        
        const erroresDeValidacion = validarFormulario();
        
        if (erroresDeValidacion.length > 0) {

            setError("Campos obligatorios no completados o formato invalido, complete todo los campos obligatorios (*)");
            setErrorCampos(erroresDeValidacion);
            setIsLoading(false);

            const primerError = erroresDeValidacion[0];
            switch (primerError) {
                case 'nombre': nombreRef.current?.focus(); break;
                case 'apellido': apellidoRef.current?.focus(); break;
                case 'tipoDocumento': tipoDocumentoRef.current?.focus(); break;
                case 'documento': documentoRef.current?.focus(); break;
                case 'fechaNacimiento': fechaNacimientoRef.current?.focus(); break;
                case 'telefono': telefonoRef.current?.focus(); break;
                case 'nacionalidad': nacionalidadRef.current?.focus(); break;
                case 'ocupacion': ocupacionRef.current?.focus(); break;
                case 'pais': paisRef.current?.focus(); break;
                case 'provincia': provinciaRef.current?.focus(); break;
                case 'localidad': localidadRef.current?.focus(); break;
                case 'calle': calleRef.current?.focus(); break;
                case 'numero': numeroRef.current?.focus(); break;
                case 'codigoPostal': codigoPostalRef.current?.focus(); break;
                default: break;
            }
            return;
        }

        try {
            const response = await fetch(API_REGISTER_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                const huespedCreado = await response.json();
                setSuccessHuespedName(`${huespedCreado.nombre} ${huespedCreado.apellido}`);
                setShowSuccessModal(true);
            } else {
                const errorMsg = await response.text();
                if (response.status === 409) {
                    setDuplicateErrorMsg(errorMsg || "¡CUIDADO! El tipo y número de documento ya existen en el sistema");
                    setShowDuplicateModal(true);
                } else {
                    setError(errorMsg || 'Error al registrar el huésped.');
                }
            }
        } catch (err) {
            setError('No se pudo conectar con el servidor.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleForceSubmit = async () => {
        setShowDuplicateModal(false);
        setIsLoading(true);
        setError(null);

        try {
            const response = await fetch(`${API_REGISTER_URL}?forzar=true`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                const huespedCreado = await response.json();
                setSuccessHuespedName(`${huespedCreado.nombre} ${huespedCreado.apellido}`);
                setShowSuccessModal(true);
            } else {
                const errorMsg = await response.text();
                setError(errorMsg || 'Error al registrar el huésped.');
            }
        } catch (err) {
            setError('No se pudo conectar con el servidor.');
        } finally {
            setIsLoading(false);
        }
    };


    const getEstiloCampo = (nombreCampo: string, estilosBase: React.CSSProperties = {}) => {
        return {
            ...estilosBase,
            border: errorCampos.includes(nombreCampo) ? '2px solid red' : undefined
        };
    };



   return (
        <div className="search-container">

            <div className="search-panel-left">
                <h1 style={{ paddingTop: '300px' }}>ALTA DE HUÉSPED</h1>
                <button 
                    onClick={() => setShowCancelModal(true)} 
                    className="cancel-button"
                    disabled={isLoading}
                >
                    Cancelar
                </button>
            </div>

            <div className="search-panel-right">
                

                <form onSubmit={handleSubmit} className="search-form" noValidate>


                    {error && <div className="error-message">{error}</div>}

                    <h3 className="form-section-header">Datos Personales</h3>

                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="nombre">Nombre(*)</label>
                            <input type="text" id="nombre" name="nombre" value={formData.nombre} 
                                onChange={handleChange} 
                                ref={nombreRef}
                                style={getEstiloCampo('nombre', { textTransform: 'uppercase' })} 
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="apellido">Apellido(*)</label>
                            <input type="text" id="apellido" name="apellido" value={formData.apellido} 
                                onChange={handleChange} 
                                ref={apellidoRef}
                                style={getEstiloCampo('apellido', { textTransform: 'uppercase' })}
                            />
                        </div>
                    </div>
                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="tipoDocumento">Tipo Doc.(*)</label>
                            <select id="tipoDocumento" name="tipoDocumento" value={formData.tipoDocumento} 
                                onChange={handleChange} 
                                ref={tipoDocumentoRef}
                                style={getEstiloCampo('tipoDocumento')}
                            >
                                <option value="DNI">DNI</option>
                                <option value="LE">LE</option>
                                <option value="LC">LC</option>
                                <option value="PASAPORTE">PASAPORTE</option>
                                <option value="Otro">Otro</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label htmlFor="documento">Documento(*)</label>
                            <input type="text" id="documento" name="documento" value={formData.documento} 
                                onChange={handleChange} 
                                ref={documentoRef}
                                style={getEstiloCampo('documento', { textTransform: 'uppercase' })}
                            />
                        </div>
                    </div>
                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="fechaNacimiento">Fecha de Nac.(*)</label>
                            <input type="date" id="fechaNacimiento" name="fechaNacimiento" value={formData.fechaNacimiento} 
                                onChange={handleChange} 
                                ref={fechaNacimientoRef}
                                style={getEstiloCampo('fechaNacimiento')}
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="telefono">Teléfono(*)</label>
                            <input type="tel" id="telefono" name="telefono" value={formData.telefono} 
                                onChange={handleChange} 
                                ref={telefonoRef}
                                style={getEstiloCampo('telefono', { textTransform: 'uppercase' })}
                            />
                        </div>
                    </div>
                    <div className="form-row">
                         <div className="form-group">
                            <label htmlFor="email">Email</label>
                            <input type="email" id="email" name="email" value={formData.email} onChange={handleChange} />
                        </div>
                        <div className="form-group">
                            <label htmlFor="nacionalidad">Nacionalidad(*)</label>
                            <input type="text" id="nacionalidad" name="nacionalidad" value={formData.nacionalidad} 
                                onChange={handleChange} 
                                ref={nacionalidadRef}
                                style={getEstiloCampo('nacionalidad', { textTransform: 'uppercase' })}
                            />
                        </div>
                         <div className="form-group">
                            <label htmlFor="ocupacion">Ocupación(*)</label>
                            <input type="text" id="ocupacion" name="ocupacion" value={formData.ocupacion} 
                                onChange={handleChange} 
                                ref={ocupacionRef}
                                style={getEstiloCampo('ocupacion', { textTransform: 'uppercase' })}
                            />
                        </div>
                    </div>
                    
                    <h3 className="form-section-header">Datos Fiscales</h3>
                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="cuit">CUIT</label>
                            <input type="text" id="cuit" name="cuit" value={formData.cuit} onChange={handleChange} style={{ textTransform: 'uppercase' }} />
                        </div>
                        <div className="form-group">
                            <label htmlFor="categoriaIVA">Categoría IVA</label>
                            <select id="categoriaIVA" name="categoriaIVA" value={formData.categoriaIVA} onChange={handleChange}>
                                <option value="Consumidor Final">Consumidor Final</option>
                                <option value="Responsable Inscripto">Responsable Inscripto</option>
                                <option value="Monotributista">Monotributista</option>
                                <option value="Exento">Exento</option>
                            </select>
                        </div>
                    </div>
                    
                    <h3 className="form-section-header">Dirección</h3>
                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="pais">País(*)</label>
                            <input type="text" id="pais" name="pais" value={formData.direccion.pais} 
                                onChange={handleDireccionChange} 
                                ref={paisRef}
                                style={getEstiloCampo('pais', { textTransform: 'uppercase' })}
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="provincia">Provincia(*)</label>
                            <input type="text" id="provincia" name="provincia" value={formData.direccion.provincia} 
                                onChange={handleDireccionChange} 
                                ref={provinciaRef}
                                style={getEstiloCampo('provincia', { textTransform: 'uppercase' })}
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="localidad">Localidad(*)</label>
                            <input type="text" id="localidad" name="localidad" value={formData.direccion.localidad} 
                                onChange={handleDireccionChange} 
                                ref={localidadRef}
                                style={getEstiloCampo('localidad', { textTransform: 'uppercase' })}
                            />
                        </div>
                    </div>
                    <div className="form-row">
                         <div className="form-group" style={{flex: 2}}>
                            <label htmlFor="calle">Calle(*)</label>
                            <input type="text" id="calle" name="calle" value={formData.direccion.calle} 
                                onChange={handleDireccionChange} 
                                ref={calleRef}
                                style={getEstiloCampo('calle', { textTransform: 'uppercase' })}
                            />
                        </div>
                         <div className="form-group" style={{flex: 1}}>
                            <label htmlFor="numero">Número(*)</label>
                            <input type="text" id="numero" name="numero" value={formData.direccion.numero} 
                                onChange={handleDireccionChange} 
                                ref={numeroRef}
                                style={getEstiloCampo('numero', { textTransform: 'uppercase' })}
                            />
                        </div>
                    </div>
                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="piso">Piso</label>
                            <input type="text" id="piso" name="piso" value={formData.direccion.piso} onChange={handleDireccionChange} style={{ textTransform: 'uppercase' }} />
                        </div>
                        <div className="form-group">
                            <label htmlFor="departamento">Departamento</label>
                            <input type="text" id="departamento" name="departamento" value={formData.direccion.departamento} onChange={handleDireccionChange} style={{ textTransform: 'uppercase' }} />
                        </div>
                        <div className="form-group">
                            <label htmlFor="codigoPostal">Código Postal(*)</label>
                            <input type="text" id="codigoPostal" name="codigoPostal" value={formData.direccion.codigoPostal} 
                                onChange={handleDireccionChange} 
                                ref={codigoPostalRef}
                                style={getEstiloCampo('codigoPostal', { textTransform: 'uppercase' })}
                            />
                        </div>
                    </div>


                    <p style={{ color: 'red', fontSize: '0.9rem', textAlign: 'left', marginTop: '15px' }}>
                        * Campos obligatorios
                    </p>
                    
                    <hr className="divider" />
                    
                    <button type="submit" className="search-button" disabled={isLoading}>
                        {isLoading ? 'Registrando...' : 'SIGUIENTE'}
                    </button>
                </form>
            </div>


            {showCancelModal && (
                <div className="modal-overlay">
                    <div className="modal-content" style={{ textAlign: 'center' }}>
                        <span className="modal-icon" style={{ fontSize: '2rem' }}>⚠️</span>
                        <h3>¿Desea cancelar el alta del huésped?</h3>
                        <div className="modal-actions" style={{ display: 'flex', gap: '10px', justifyContent: 'center', marginTop: '20px' }}>
                            <button className="cancel-button" style={{ width: '100px' }} onClick={() => setShowCancelModal(false)}>NO</button>
                            <button className="search-button" style={{ width: '100px' }} onClick={onCancel}>SI</button>
                        </div>
                    </div>
                </div>
            )}
            
            {showDuplicateModal && (
                <div className="modal-overlay">
                    <div className="modal-content" style={{ textAlign: 'center' }}>
                        <span className="modal-icon" style={{ fontSize: '2rem' }}>⚠️</span>
                        <h3 style={{ margin: '15px 0' }}>{duplicateErrorMsg}</h3>
                        <div className="modal-actions" style={{ display: 'flex', flexDirection: 'column', gap: '10px', marginTop: '20px' }}>
                            <button className="search-button" onClick={handleForceSubmit} disabled={isLoading}>
                                {isLoading ? 'Aceptando...' : 'ACEPTAR IGUALMENTE'}
                            </button>
                            <button 
                                className="search-button" 
                                style={{ backgroundColor: '#f0f0f0', color: '#333', border: '1px solid #ccc' }} 
                                onClick={handleCorregirDuplicado} 
                                disabled={isLoading}
                            >
                                CORREGIR
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {showSuccessModal && (
                <div className="modal-overlay">
                    <div className="modal-content" style={{ textAlign: 'center' }}>
                        <p style={{ fontSize: '1.1rem', margin: '15px 0' }}>
                            El huésped "{successHuespedName}" ha sido satisfactoriamente cargado al sistema. 
                            <br/>
                            ¿Desea cargar otro?
                        </p>
                        <div className="modal-actions" style={{ display: 'flex', gap: '10px', justifyContent: 'center', marginTop: '20px' }}>
                            <button className="cancel-button" style={{ width: '100px' }} onClick={onCancel}>NO</button>
                            <button className="search-button" style={{ width: '100px' }}
                                onClick={() => {
                                    setFormData(estadoInicialFormulario);
                                    setShowSuccessModal(false);
                                    setSuccessHuespedName('');
                                    setError(null);
                                    setErrorCampos([]);
                                }}
                            >
                                SI
                            </button>
                        </div>
                    </div>
                </div>
            )}
            
        </div>
    );
}