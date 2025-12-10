'use client';
import { useState, useEffect, useRef } from 'react';
import ModalAlerta from './ModalAlerta';

interface ModificarHuespedProps {
    huespedId: number;
    onCancel: () => void;
    onSuccess: () => void;
}

const API_BASE_URL = 'http://localhost:8081/api/huespedes';

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

export default function ModificarHuesped({ huespedId, onCancel, onSuccess }: ModificarHuespedProps) {

    const [formData, setFormData] = useState(estadoInicialFormulario);
    const [tieneEstadias, setTieneEstadias] = useState(false); // Bandera para el flujo 2.A
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    // UI States
    const [modalConflictoDoc, setModalConflictoDoc] = useState<{show: boolean, msg: string}>({show: false, msg: ''});
    const [modalConfirmarCancel, setModalConfirmarCancel] = useState(false);
    const [modalExito, setModalExito] = useState<{show: boolean, msg: string}>({show: false, msg: ''});

    // Modal Eliminar unificado: Tipo 'CONFIRMACION' (Flujo 2) o 'ERROR_HISTORIAL' (Flujo 2.A)
    const [modalEliminar, setModalEliminar] = useState<{show: boolean, msg: string, tipo: 'CONFIRMACION' | 'ERROR_HISTORIAL'}>({show: false, msg: '', tipo: 'CONFIRMACION'});

    const [errorCampos, setErrorCampos] = useState<string[]>([]);

    // Refs
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

    // Carga de datos inicial
    useEffect(() => {
        const cargarDatos = async () => {
            setIsLoading(true);
            try {
                const res = await fetch(`${API_BASE_URL}/${huespedId}`);
                if (!res.ok) throw new Error('Error al cargar datos del huésped');

                // Ahora el backend devuelve un objeto { huesped: {...}, tieneEstadias: boolean }
                const dataRespuesta = await res.json();
                const data = dataRespuesta.huesped;
                setTieneEstadias(dataRespuesta.tieneEstadias); // Guardamos la bandera

                setFormData({
                    nombre: data.nombre || '',
                    apellido: data.apellido || '',
                    email: data.email || '',
                    tipoDocumento: data.tipoDocumento || 'DNI',
                    documento: data.documento || '',
                    telefono: data.telefono || '',
                    fechaNacimiento: data.fechaNacimiento || '',
                    ocupacion: data.ocupacion || '',
                    nacionalidad: data.nacionalidad || '',
                    cuit: data.cuit || '',
                    categoriaIVA: data.categoriaIVA || 'Consumidor Final',
                    direccion: {
                        pais: data.direccion?.pais || '',
                        provincia: data.direccion?.provincia || '',
                        localidad: data.direccion?.localidad || '',
                        calle: data.direccion?.calle || '',
                        numero: data.direccion?.numero || '',
                        piso: data.direccion?.piso || '',
                        departamento: data.direccion?.departamento || '',
                        codigoPostal: data.direccion?.codigoPostal || '',
                    }
                });
            } catch (err) {
                setError("No se pudieron recuperar los datos del huésped.");
            } finally {
                setIsLoading(false);
            }
        };
        cargarDatos();
    }, [huespedId]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        const camposSinMayusculas = ['email', 'tipoDocumento', 'categoriaIVA'];
        const valorProcesado = camposSinMayusculas.includes(name) ? value : value.toUpperCase();
        setFormData(prev => ({ ...prev, [name]: valorProcesado }));
        if (errorCampos.includes(name)) setErrorCampos(prev => prev.filter(c => c !== name));
    };

    const handleDireccionChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            direccion: { ...prev.direccion, [name]: value.toUpperCase() }
        }));
        if (errorCampos.includes(name)) setErrorCampos(prev => prev.filter(c => c !== name));
    };

    const validarFormulario = (): string[] => {
        const { nombre, apellido, documento, telefono, fechaNacimiento, ocupacion, nacionalidad, direccion } = formData;
        const errores: string[] = [];
        const regexSoloLetras = /^[A-ZÁÉÍÓÚÜÑ\s]+$/i;
        const regexSoloNumeros = /^[0-9]+$/;

        if (!nombre || !regexSoloLetras.test(nombre)) errores.push('nombre');
        if (!apellido || !regexSoloLetras.test(apellido)) errores.push('apellido');
        if (!telefono || !regexSoloNumeros.test(telefono)) errores.push('telefono');
        if (!documento) errores.push('documento');
        if (!fechaNacimiento) errores.push('fechaNacimiento');
        if (!ocupacion || !regexSoloLetras.test(ocupacion)) errores.push('ocupacion');
        if (!nacionalidad || !regexSoloLetras.test(nacionalidad)) errores.push('nacionalidad');

        if (!direccion.pais || !regexSoloLetras.test(direccion.pais)) errores.push('pais');
        if (!direccion.provincia || !regexSoloLetras.test(direccion.provincia)) errores.push('provincia');
        if (!direccion.localidad || !regexSoloLetras.test(direccion.localidad)) errores.push('localidad');
        if (!direccion.calle || !regexSoloLetras.test(direccion.calle)) errores.push('calle');
        if (!direccion.numero || !regexSoloNumeros.test(direccion.numero)) errores.push('numero');
        if (!direccion.codigoPostal || !regexSoloNumeros.test(direccion.codigoPostal)) errores.push('codigoPostal');
        if (direccion.piso && !regexSoloNumeros.test(direccion.piso)) errores.push('piso');

        return errores;
    };

    // --- MODIFICAR (PUT) ---
    const handleUpdate = async (forzar: boolean = false) => {
        setError(null);
        setModalConflictoDoc({show:false, msg:''});

        const errores = validarFormulario();
        if (errores.length > 0) {
            setError("Verifique los campos obligatorios y formatos.");
            setErrorCampos(errores);
            if(errores.includes('nombre')) nombreRef.current?.focus();
            return;
        }

        setIsLoading(true);
        const url = `${API_BASE_URL}/${huespedId}${forzar ? '?forzar=true' : ''}`;

        try {
            const response = await fetch(url, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                setModalExito({show: true, msg: "La operación ha culminado con éxito"});
            } else {
                const msg = await response.text();
                if (response.status === 409) {
                    setModalConflictoDoc({show: true, msg: msg || "Conflicto de duplicados."});
                } else {
                    setError("Error al modificar: " + msg);
                }
            }
        } catch (err) {
            setError("Error de conexión al guardar.");
        } finally {
            setIsLoading(false);
        }
    };

    // --- LÓGICA DE BAJA (CU11) CORREGIDA ---
    const iniciarEliminacion = () => {
        // PASO 1: El usuario pulsa BORRAR.
        // PASO 2: El sistema constata...

        if (tieneEstadias) {
            // FLUJO 2.A: Tiene historial -> Muestra error inmediatamente.
            const msg = "El huésped no puede ser eliminado pues se ha alojado en el Hotel en alguna oportunidad.";
            setModalEliminar({ show: true, msg, tipo: 'ERROR_HISTORIAL' });
        } else {
            // FLUJO 2 (Principal): No tiene historial -> Muestra botones de confirmación.
            const msg = `Los datos del huésped ${formData.apellido}, ${formData.nombre} (${formData.tipoDocumento} ${formData.documento}) serán eliminados del sistema`;
            setModalEliminar({ show: true, msg, tipo: 'CONFIRMACION' });
        }
    };

    const confirmarEliminacion = async () => {
        // PASO 3: El actor presiona "ELIMINAR" (Solo accesible si no tenía historial)
        setIsLoading(true);
        try {
            const response = await fetch(`${API_BASE_URL}/${huespedId}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                // Mensaje final del flujo principal
                const msg = `Los datos del huésped ${formData.apellido}, ${formData.nombre} han sido eliminados del sistema.`;
                setModalEliminar({ show: false, msg: '', tipo: 'CONFIRMACION' });
                setModalExito({ show: true, msg: msg + " PRESIONE OK PARA CONTINUAR..." });
            } else {
                const msg = await response.text();
                // Seguridad adicional por si la UI falló
                if (response.status === 409) {
                    setModalEliminar({ show: true, msg: msg, tipo: 'ERROR_HISTORIAL' });
                } else {
                    setError("No se pudo eliminar: " + msg);
                    setModalEliminar({show:false, msg:'', tipo: 'CONFIRMACION'});
                }
            }
        } catch (err) {
            setError("Error de conexión al intentar eliminar.");
            setModalEliminar({show:false, msg:'', tipo: 'CONFIRMACION'});
        } finally {
            setIsLoading(false);
        }
    };

    const getEstiloCampo = (nombreCampo: string, estilosBase: React.CSSProperties = {}) => ({
        ...estilosBase,
        border: errorCampos.includes(nombreCampo) ? '2px solid red' : undefined
    });

    return (
        <div className="search-container">
            <div className="search-panel-left">
                <h1 style={{ paddingTop: '150px' }}>MODIFICAR HUÉSPED</h1>

                <button
                    onClick={iniciarEliminacion}
                    className="cancel-button"
                    style={{ marginTop: '20px', backgroundColor: '#e74c3c', color: 'white', border: 'none' }}
                    disabled={isLoading}
                >
                    BORRAR
                </button>

                <button
                    onClick={() => setModalConfirmarCancel(true)}
                    className="cancel-button"
                    style={{ marginTop: 'auto' }}
                    disabled={isLoading}
                >
                    Cancelar
                </button>
            </div>

            <div className="search-panel-right">
                <form onSubmit={(e) => { e.preventDefault(); handleUpdate(false); }} className="search-form" noValidate>
                    {error && <div className="error-message">{error}</div>}

                    <h3 className="form-section-header">Datos Personales</h3>
                    {/* Campos del formulario idénticos a los anteriores... */}
                    <div className="form-row">
                        <div className="form-group">
                            <label>Nombre(*)</label>
                            <input name="nombre" value={formData.nombre} onChange={handleChange} ref={nombreRef} style={getEstiloCampo('nombre', {textTransform:'uppercase'})} />
                        </div>
                        <div className="form-group">
                            <label>Apellido(*)</label>
                            <input name="apellido" value={formData.apellido} onChange={handleChange} ref={apellidoRef} style={getEstiloCampo('apellido', {textTransform:'uppercase'})} />
                        </div>
                    </div>
                    <div className="form-row">
                        <div className="form-group">
                            <label>Tipo Doc.(*)</label>
                            <select name="tipoDocumento" value={formData.tipoDocumento} onChange={handleChange} ref={tipoDocumentoRef} style={getEstiloCampo('tipoDocumento')}>
                                <option value="DNI">DNI</option>
                                <option value="LE">LE</option>
                                <option value="LC">LC</option>
                                <option value="PASAPORTE">PASAPORTE</option>
                                <option value="Otro">Otro</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label>Documento(*)</label>
                            <input name="documento" value={formData.documento} onChange={handleChange} ref={documentoRef} style={getEstiloCampo('documento')} />
                        </div>
                    </div>
                    <div className="form-row">
                        <div className="form-group">
                            <label>Fecha de Nac.(*)</label>
                            <input type="date" name="fechaNacimiento" value={formData.fechaNacimiento} onChange={handleChange} ref={fechaNacimientoRef} style={getEstiloCampo('fechaNacimiento')} />
                        </div>
                        <div className="form-group">
                            <label>Teléfono(*)</label>
                            <input name="telefono" value={formData.telefono} onChange={handleChange} ref={telefonoRef} style={getEstiloCampo('telefono')} />
                        </div>
                    </div>
                    <div className="form-row">
                        <div className="form-group">
                            <label>Email</label>
                            <input type="email" name="email" value={formData.email} onChange={handleChange} />
                        </div>
                        <div className="form-group">
                            <label>Nacionalidad(*)</label>
                            <input name="nacionalidad" value={formData.nacionalidad} onChange={handleChange} ref={nacionalidadRef} style={getEstiloCampo('nacionalidad', {textTransform:'uppercase'})} />
                        </div>
                        <div className="form-group">
                            <label>Ocupación(*)</label>
                            <input name="ocupacion" value={formData.ocupacion} onChange={handleChange} ref={ocupacionRef} style={getEstiloCampo('ocupacion', {textTransform:'uppercase'})} />
                        </div>
                    </div>

                    <h3 className="form-section-header">Datos Fiscales</h3>
                    <div className="form-row">
                        <div className="form-group">
                            <label>CUIT</label>
                            <input name="cuit" value={formData.cuit} onChange={handleChange} style={{textTransform:'uppercase'}} />
                        </div>
                        <div className="form-group">
                            <label>Categoría IVA</label>
                            <select name="categoriaIVA" value={formData.categoriaIVA} onChange={handleChange}>
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
                            <label>País(*)</label>
                            <input name="pais" value={formData.direccion.pais} onChange={handleDireccionChange} ref={paisRef} style={getEstiloCampo('pais', {textTransform:'uppercase'})} />
                        </div>
                        <div className="form-group">
                            <label>Provincia(*)</label>
                            <input name="provincia" value={formData.direccion.provincia} onChange={handleDireccionChange} ref={provinciaRef} style={getEstiloCampo('provincia', {textTransform:'uppercase'})} />
                        </div>
                        <div className="form-group">
                            <label>Localidad(*)</label>
                            <input name="localidad" value={formData.direccion.localidad} onChange={handleDireccionChange} ref={localidadRef} style={getEstiloCampo('localidad', {textTransform:'uppercase'})} />
                        </div>
                    </div>
                    <div className="form-row">
                        <div className="form-group" style={{flex: 2}}>
                            <label>Calle(*)</label>
                            <input name="calle" value={formData.direccion.calle} onChange={handleDireccionChange} ref={calleRef} style={getEstiloCampo('calle', {textTransform:'uppercase'})} />
                        </div>
                        <div className="form-group" style={{flex: 1}}>
                            <label>Número(*)</label>
                            <input name="numero" value={formData.direccion.numero} onChange={handleDireccionChange} ref={numeroRef} style={getEstiloCampo('numero', {textTransform:'uppercase'})} />
                        </div>
                    </div>
                    <div className="form-row">
                        <div className="form-group">
                            <label>Piso</label>
                            <input name="piso" value={formData.direccion.piso} onChange={handleDireccionChange} style={{textTransform:'uppercase'}} />
                        </div>
                        <div className="form-group">
                            <label>Depto</label>
                            <input name="departamento" value={formData.direccion.departamento} onChange={handleDireccionChange} style={{textTransform:'uppercase'}} />
                        </div>
                        <div className="form-group">
                            <label>CP(*)</label>
                            <input name="codigoPostal" value={formData.direccion.codigoPostal} onChange={handleDireccionChange} ref={codigoPostalRef} style={getEstiloCampo('codigoPostal', {textTransform:'uppercase'})} />
                        </div>
                    </div>

                    <p style={{ color: 'red', fontSize: '0.9rem', marginTop: '15px' }}>* Campos obligatorios</p>
                    <hr className="divider" />

                    <button type="submit" className="search-button" disabled={isLoading}>
                        {isLoading ? 'Guardando...' : 'SIGUIENTE'}
                    </button>
                </form>
            </div>

            {/* MODALES */}
            {modalConfirmarCancel && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <span className="modal-icon">⚠️</span>
                        <h3>¿Desea cancelar la modificación del huésped?</h3>
                        <div className="modal-actions" style={{ display: 'flex', gap: '10px', justifyContent: 'center' }}>
                            <button className="cancel-button" onClick={() => setModalConfirmarCancel(false)}>NO</button>
                            <button className="search-button" onClick={onCancel}>SI</button>
                        </div>
                    </div>
                </div>
            )}

            {modalConflictoDoc.show && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <span className="modal-icon">⚠️</span>
                        <h3>{modalConflictoDoc.msg}</h3>
                        <div className="modal-actions" style={{ display: 'flex', flexDirection:'column', gap: '10px' }}>
                            <button className="search-button" onClick={() => handleUpdate(true)}>ACEPTAR IGUALMENTE</button>
                            <button className="cancel-button" onClick={() => {
                                setModalConflictoDoc({show:false, msg:''});
                                setErrorCampos(['tipoDocumento', 'documento']);
                                tipoDocumentoRef.current?.focus();
                            }}>CORREGIR</button>
                        </div>
                    </div>
                </div>
            )}

            {/* MODAL DE ELIMINACIÓN: GESTIONA FLUJO PRINCIPAL Y 2.A */}
            {modalEliminar.show && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <span className="modal-icon">⚠️</span>
                        <p style={{fontWeight:'bold', marginTop:'10px'}}>{modalEliminar.msg}</p>

                        {/* Si el tipo es CONFIRMACION (Flujo Principal), mostramos ELIMINAR/CANCELAR */}
                        {modalEliminar.tipo === 'CONFIRMACION' ? (
                            <div className="modal-actions" style={{ display: 'flex', gap: '10px', justifyContent: 'center' }}>
                                <button className="cancel-button" onClick={() => setModalEliminar({show:false, msg:'', tipo:'CONFIRMACION'})}>CANCELAR</button>
                                <button className="search-button" style={{backgroundColor: '#e74c3c', color:'white'}} onClick={confirmarEliminacion}>ELIMINAR</button>
                            </div>
                        ) : (
                            // Si el tipo es ERROR_HISTORIAL (Flujo 2.A), Solo mostramos CONTINUAR
                            <div className="modal-actions" style={{ display: 'flex', gap: '10px', justifyContent: 'center', marginTop: '10px' }}>
                                <button className="search-button" onClick={() => setModalEliminar({show:false, msg:'', tipo:'CONFIRMACION'})}>CONTINUAR</button>
                            </div>
                        )}
                    </div>
                </div>
            )}

            {modalExito.show && (
                <ModalAlerta mensaje={modalExito.msg} onAceptar={onSuccess} icono="✅" />
            )}
        </div>
    );
}