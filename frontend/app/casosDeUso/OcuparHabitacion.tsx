'use client';
import { useState, useMemo, useEffect } from 'react';
import ModalAlerta from './ModalAlerta';

// --- TIPOS ---
interface Celda {
    idHabitacion: string;
    numero: string;
    estado: string; // 'LIBRE' | 'OCUPADA' | 'RESERVADA'
    texto: string;
    tipoHabitacion: string;
}

interface Fila {
    fechaStr: string;
    celdas: Celda[];
}

interface Seleccion {
    idHabitacion: string;
    numeroHabitacion: string;
    tipoHabitacion: string;
    fechaInicio: string;
    fechaFin: string;
    fechaInicioDisplay: string;
    fechaFinDisplay: string;
    indiceInicio: number;
    indiceFin: number;
    conflictoConReserva?: boolean;
}

interface Huesped {
    id: number;
    nombre: string;
    apellido: string;
    tipoDocumento: string;
    documento: string;
}

type Paso =
    | 'INGRESO_FECHAS'
    | 'SELECCION_GRILLA'
    | 'PINTAR_OCUPADA'
    | 'BUSQUEDA_HUESPED'
    | 'RESULTADOS_HUESPED'
    | 'GESTION_FINAL';

export default function OcuparHabitacion({ onCancel }: { onCancel: () => void }) {
    // --- ESTADOS ---
    const [paso, setPaso] = useState<Paso>('INGRESO_FECHAS');
    const [modal, setModal] = useState<{show: boolean, msg: string | React.ReactNode}>({show:false, msg:''});

    // Fechas y Grilla
    const [fechaInicio, setFechaInicio] = useState('');
    const [fechaFin, setFechaFin] = useState('');
    const [grilla, setGrilla] = useState<Fila[]>([]);
    const [filtroTipo, setFiltroTipo] = useState<string>('Todos');
    const [cargando, setCargando] = useState(false);

    // Selección
    const [primerClick, setPrimerClick] = useState<{ idHab: string, idx: number } | null>(null);
    const [seleccion, setSeleccion] = useState<Seleccion | null>(null);
    const [modalConflicto, setModalConflicto] = useState<{show: boolean, data: Seleccion | null, huespedNombre?: string}>({show: false, data: null});

    // Gestión Huéspedes
    // Inicializamos el tipo en 'DNI' por defecto para el select
    const [busqueda, setBusqueda] = useState({ nombre: '', apellido: '', tipo: 'DNI', documento: '' });
    const [listaResultados, setListaResultados] = useState<Huesped[]>([]);
    const [huespedTemporal, setHuespedTemporal] = useState<Huesped | null>(null);
    const [listaHuespedesAsignados, setListaHuespedesAsignados] = useState<Huesped[]>([]);

    // --- HELPERS ---
    const convertirFechaIso = (f: string) => { const [d,m,a] = f.split('/'); return `${a}-${m}-${d}`; }

    const sumarDia = (fechaStr: string) => {
        const [d, m, a] = fechaStr.split('/').map(Number);
        const fecha = new Date(a, m - 1, d);
        fecha.setDate(fecha.getDate() + 1);
        const dd = String(fecha.getDate()).padStart(2, '0');
        const mm = String(fecha.getMonth() + 1).padStart(2, '0');
        const aa = fecha.getFullYear();
        return { iso: `${aa}-${mm}-${dd}`, display: `${dd}/${mm}/${aa}` };
    }

    // --- PASO 4: TECLA PARA CONTINUAR ---
    useEffect(() => {
        if (paso === 'PINTAR_OCUPADA') {
            const handleInput = (e: Event) => {
                e.preventDefault();
                e.stopPropagation();
                setPaso('BUSQUEDA_HUESPED');
            };
            window.addEventListener('keydown', handleInput, {once:true});
            window.addEventListener('mousedown', handleInput, {once:true});
            return () => {
                window.removeEventListener('keydown', handleInput);
                window.removeEventListener('mousedown', handleInput);
            };
        }
    }, [paso]);

    // --- ACCIONES ---

    const handleConsultar = async () => {
        if (!fechaInicio || !fechaFin) { setModal({show: true, msg: 'Debe ingresar ambas fechas'}); return; }
        setCargando(true);
        try {
            const res = await fetch(`http://localhost:8081/api/habitaciones/estado?desde=${fechaInicio}&hasta=${fechaFin}`);
            const data: Fila[] = await res.json();
            setGrilla(data);
            setPaso('SELECCION_GRILLA');
            // Resets
            setSeleccion(null); setPrimerClick(null); setListaHuespedesAsignados([]);
        } catch (e) { setModal({show: true, msg: 'Error de conexión'}); }
        finally { setCargando(false); }
    };

    const handleCellClick = (celda: Celda, idxFila: number) => {
        // Bloquear si ya está ocupada
        if (celda.estado === 'OCUPADA') {
            setModal({show: true, msg: 'La habitación ya está ocupada.'});
            return;
        }

        if (!primerClick) {
            setPrimerClick({ idHab: celda.idHabitacion, idx: idxFila });
        } else {
            if (primerClick.idHab !== celda.idHabitacion) {
                setModal({show: true, msg: 'Seleccione inicio y fin en la misma habitación'});
                setPrimerClick(null);
                return;
            }

            const ini = Math.min(primerClick.idx, idxFila);
            const fin = Math.max(primerClick.idx, idxFila);

            // Validar conflictos intermedios
            let tieneReservadas = false;
            let nombreReserva = "";

            for(let i=ini; i<=fin; i++) {
                const c = grilla[i].celdas.find(x => x.idHabitacion === celda.idHabitacion);
                if(c?.estado === 'OCUPADA') {
                    setModal({show: true, msg: 'El rango contiene días ocupados.'});
                    setPrimerClick(null);
                    return;
                }
                if(c?.estado === 'RESERVADA') {
                    tieneReservadas = true;
                    nombreReserva = c.texto || "Reservado";
                }
            }

            const fechaFinCalc = sumarDia(grilla[fin].fechaStr);
            const nuevaSeleccion: Seleccion = {
                idHabitacion: celda.idHabitacion,
                numeroHabitacion: celda.numero,
                tipoHabitacion: celda.tipoHabitacion,
                fechaInicio: convertirFechaIso(grilla[ini].fechaStr),
                fechaFin: fechaFinCalc.iso,
                fechaInicioDisplay: grilla[ini].fechaStr,
                fechaFinDisplay: fechaFinCalc.display,
                indiceInicio: ini,
                indiceFin: fin,
                conflictoConReserva: tieneReservadas
            };

            if (tieneReservadas) {
                setModalConflicto({ show: true, data: nuevaSeleccion, huespedNombre: nombreReserva });
            } else {
                confirmarSeleccion(nuevaSeleccion);
            }
        }
    };

    const confirmarSeleccion = (sel: Seleccion) => {
        setSeleccion(sel);
        setPrimerClick(null);
        setModalConflicto({show:false, data:null});
        setPaso('PINTAR_OCUPADA');
    };

    // --- BÚSQUEDA HUÉSPED (CORREGIDO EL FILTRO) ---
    const handleBuscarHuesped = async (e?: React.FormEvent) => {
        if(e) e.preventDefault();

        try {
            const params = new URLSearchParams();
            if(busqueda.nombre) params.append('nombre', busqueda.nombre);
            if(busqueda.apellido) params.append('apellido', busqueda.apellido);
            if(busqueda.documento) params.append('documento', busqueda.documento);

            // --- CORRECCIÓN IMPORTANTE: ENVIAR EL TIPO DE DOCUMENTO ---
            if(busqueda.tipo) params.append('tipoDocumento', busqueda.tipo);

            const res = await fetch(`http://localhost:8081/api/huespedes/buscar?${params.toString()}`);
            if(res.ok) {
                const data: Huesped[] = await res.json();
                if(data.length === 0) {
                    setModal({show:true, msg: "No se encontraron huéspedes con ese criterio."});
                } else {
                    setListaResultados(data);
                    setPaso('RESULTADOS_HUESPED');
                    setHuespedTemporal(null);
                }
            } else {
                setModal({show:true, msg: "Error al buscar huéspedes."});
            }
        } catch (e) {
            setModal({show:true, msg: "Error de conexión con el servidor."});
        }
    };

    const handleAceptarHuesped = () => {
        if (!huespedTemporal) {
            setModal({show: true, msg: "Debe seleccionar un huésped de la lista."});
            return;
        }

        // Validación de Duplicados
        const yaExiste = listaHuespedesAsignados.some(h => h.id === huespedTemporal.id);
        if (yaExiste) {
            setModal({show: true, msg: "Este huésped ya está cargado para el check-in."});
            return;
        }

        setListaHuespedesAsignados([...listaHuespedesAsignados, huespedTemporal]);
        setPaso('GESTION_FINAL');
    };

    // --- GUARDADO FINAL ---
    const crearEstadia = async (accion: 'SALIR' | 'CARGAR_OTRA') => {
        if (!seleccion || listaHuespedesAsignados.length === 0) return;

        const dto = {
            idHabitacion: Number(seleccion.idHabitacion),
            fechaInicio: seleccion.fechaInicio,
            fechaFin: seleccion.fechaFin,
            idsHuespedes: listaHuespedesAsignados.map(h => h.id),
            esOverrideReserva: seleccion.conflictoConReserva
        };

        try {
            const res = await fetch('http://localhost:8081/api/ocupacion/crear', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(dto)
            });

            if (res.ok) {
                setModal({show: true, msg: "Estadía cargada con éxito."});
                setTimeout(() => {
                    setModal({show:false, msg:''});
                    if (accion === 'SALIR') {
                        onCancel();
                    } else {
                        setSeleccion(null);
                        setListaHuespedesAsignados([]);
                        setBusqueda({nombre:'', apellido:'', tipo:'DNI', documento:''});
                        handleConsultar(); // Al refrescar, la estadía creada debe venir como OCUPADA (Rosa)
                    }
                }, 1500);
            } else {
                const errorText = await res.text();
                setModal({show: true, msg: "Error: " + errorText});
            }
        } catch(e) {
            setModal({show: true, msg: "Error de conexión."});
        }
    };

    // --- ESTILOS DE CELDA ---
    const getCellStyle = (celda: Celda, idxFila: number) => {
        let bg = '#ccc'; let color = '#000'; let cursor = 'default'; let textoCelda = celda.texto;

        // 1. Estados que vienen del Backend
        if (celda.estado === 'LIBRE') {
            bg = '#4a7c35'; color = 'white'; cursor = 'pointer';
        }
        else if (celda.estado === 'OCUPADA') {
            bg = '#ffd1dc'; // COLOR ROSA (Solicitado)
            color = 'black';
            cursor = 'not-allowed';
            textoCelda = 'OCUPADA'; // Forzamos texto por requerimiento
        }
        else if (celda.estado === 'RESERVADA') {
            bg = '#f4b942'; color = 'black'; cursor = 'pointer';
        }

        // 2. Selección Actual (Paso de Pintar)
        // Esto sobrescribe visualmente para que el usuario vea qué está seleccionando ahora
        if (paso === 'PINTAR_OCUPADA' && seleccion) {
            if (celda.idHabitacion === seleccion.idHabitacion && idxFila >= seleccion.indiceInicio && idxFila <= seleccion.indiceFin) {
                bg = '#ffcccc'; // Rojo Clarito / Rosa distinto para diferenciar "Nueva" de "Vieja"
                color = 'black';
                textoCelda = 'OCUPADA';
            }
        }

        // 3. Borde Primer Click
        let border = '1px solid #999';
        if (primerClick?.idHab === celda.idHabitacion && primerClick.idx === idxFila) border = '3px solid yellow';

        return { style: { backgroundColor: bg, color, cursor, border, padding: '10px', textAlign: 'center' as const, fontWeight: 'bold' }, texto: textoCelda };
    };

    // --- MEMOS ---
    const indicesColumnas = useMemo(() => {
        if(grilla.length===0) return [];
        return grilla[0].celdas.map((c,i) => (filtroTipo==='Todos'||c.tipoHabitacion===filtroTipo) ? i : -1).filter(i=>i!==-1);
    }, [grilla, filtroTipo]);
    const tipos = useMemo(() => grilla.length>0 ? ['Todos', ...Array.from(new Set(grilla[0].celdas.map(c=>c.tipoHabitacion)))] : [], [grilla]);

    // --- VISTAS ---

    // 1. FECHAS
    if (paso === 'INGRESO_FECHAS') {
        return (
            <div style={{ width: '800px', margin: '0 auto', border: '1px solid #000', backgroundColor: '#f9f9f9', display: 'flex' }}>
                {modal.show && <ModalAlerta mensaje={modal.msg} onAceptar={()=>setModal({show:false, msg:''})} />}
                <div style={{ backgroundColor: '#dceca4', width: '50%', padding: '40px', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                    <h1 style={{ fontSize: '3rem', margin: 0, lineHeight: 1.2 }}>OCUPAR<br/>HABITACION</h1>
                    <button style={{ marginTop: '50px', backgroundColor: '#ffbdad', border: '1px solid #cc8b79', padding: '10px 20px', fontWeight: 'bold', width: 'fit-content' }} onClick={onCancel}>Cancelar</button>
                </div>
                <div style={{ width: '50%', padding: '40px', display: 'flex', flexDirection: 'column', gap: '20px' }}>
                    <div><label style={{fontWeight:'bold'}}>Fecha Inicio</label><input type="date" value={fechaInicio} onChange={e=>setFechaInicio(e.target.value)} style={{width: '100%', padding:'5px'}}/></div>
                    <div><label style={{fontWeight:'bold'}}>Fecha Final</label><input type="date" value={fechaFin} onChange={e=>setFechaFin(e.target.value)} style={{width: '100%', padding:'5px'}}/></div>
                    <div style={{display:'flex', justifyContent:'flex-end', marginTop:'20px'}}>
                        <button style={{ backgroundColor: '#dceca4', border: '1px solid #999', padding: '10px 30px', fontWeight: 'bold' }} onClick={handleConsultar} disabled={cargando}>
                            {cargando ? 'Cargando...' : 'Consultar'}
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    // 2. GRILLA & CONFLICTO & PINTAR
    if (paso === 'SELECCION_GRILLA' || paso === 'PINTAR_OCUPADA') {
        return (
            <div style={{ width: '900px', margin: '0 auto', border: '1px solid #000', backgroundColor: '#f9f9f9', position: 'relative' }}>
                {modal.show && <ModalAlerta mensaje={modal.msg} onAceptar={()=>setModal({show:false, msg:''})} />}

                {paso === 'PINTAR_OCUPADA' && (
                    <div style={{ position: 'fixed', bottom: '0', left: '0', width: '100%', backgroundColor: '#bee3f8', borderTop: '2px solid #3182ce', padding: '20px', textAlign: 'center', fontSize: '1.5rem', fontWeight: 'bold', color: '#2c5282', zIndex: 9999 }}>
                        ℹ️ Pulse cualquier tecla para CONTINUAR
                    </div>
                )}

                {modalConflicto.show && (
                    <div style={{ position: 'fixed', top:0, left:0, width:'100%', height:'100%', background:'rgba(0,0,0,0.5)', display:'flex', justifyContent:'center', alignItems:'center', zIndex:1000 }}>
                        <div style={{ backgroundColor: '#e0e0e0', padding: '20px', border: '1px solid #000', textAlign: 'center', width: '450px' }}>
                            <h2 style={{marginTop:0}}>Reservada por:</h2>
                            <div style={{ border: '1px solid #999', padding: '15px', backgroundColor: '#fff', marginBottom: '20px', textAlign:'left' }}>
                                <div><strong>Nombre:</strong> {modalConflicto.huespedNombre}</div>
                                <div><strong>Habitación:</strong> {modalConflicto.data?.tipoHabitacion}</div>
                                <div><strong>Ingreso:</strong> {modalConflicto.data?.fechaInicioDisplay}</div>
                                <div><strong>Egreso:</strong> {modalConflicto.data?.fechaFinDisplay}</div>
                            </div>
                            <div style={{ display: 'flex', justifyContent: 'space-between', gap:'10px' }}>
                                <button onClick={() => { setModalConflicto({show:false, data:null}); setPrimerClick(null); }} style={{ flex:1, backgroundColor: '#ffbdad', border: '1px solid #cc8b79', padding: '10px', fontWeight: 'bold' }}>Rechazar</button>
                                <button onClick={() => confirmarSeleccion(modalConflicto.data!)} style={{ flex:1, backgroundColor: '#dceca4', border: '1px solid #999', padding: '10px', fontWeight: 'bold' }}>Ocupar Igual</button>
                            </div>
                        </div>
                    </div>
                )}

                <div style={{ backgroundColor: '#dceca4', padding: '15px', textAlign: 'center', borderBottom: '1px solid #999' }}>
                    <h2 style={{ margin: 0 }}>Estado de Habitaciones</h2>
                </div>
                <div style={{ padding: '20px' }}>
                    <div style={{ marginBottom: '15px' }}>
                        <select value={filtroTipo} onChange={(e) => setFiltroTipo(e.target.value)} style={{ padding: '8px' }}>
                            {tipos.map(t => <option key={t} value={t}>{t}</option>)}
                        </select>
                    </div>
                    <div style={{ overflowX: 'auto', marginBottom: '20px', maxHeight:'400px' }}>
                        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                            <thead>
                            <tr>
                                <th style={{padding:'10px', border:'1px solid #999', background:'#ddd'}}>Día \ Hab</th>
                                {indicesColumnas.map(i => <th key={i} style={{padding:'10px', border:'1px solid #999', background:'#ddd'}}>{grilla[0].celdas[i].numero}</th>)}
                            </tr>
                            </thead>
                            <tbody>
                            {grilla.map((f, i) => (
                                <tr key={i}>
                                    <td style={{padding:'10px', background:'#eee', border:'1px solid #999', fontWeight:'bold'}}>{f.fechaStr}</td>
                                    {indicesColumnas.map(cIdx => {
                                        const celda = f.celdas[cIdx];
                                        const {style, texto} = getCellStyle(celda, i);
                                        return <td key={celda.idHabitacion} style={style} onClick={()=>handleCellClick(celda, i)}>{texto}</td>
                                    })}
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                    <button style={{ backgroundColor: '#ffbdad', border: '1px solid #cc8b79', padding: '10px 30px', fontWeight: 'bold' }} onClick={onCancel}>Cancelar</button>
                </div>
            </div>
        );
    }

    // 6. GESTIONAR HUÉSPED (SPLIT LAYOUT)
    if (paso === 'BUSQUEDA_HUESPED') {
        return (
            <div style={{ width: '800px', margin: '20px auto', display: 'flex', border: '1px solid #000', backgroundColor: '#fff', minHeight: '400px' }}>
                {/* Panel Izquierdo */}
                <div style={{ width: '35%', backgroundColor: '#dceca4', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', borderRight: '1px solid #000', padding: '20px' }}>
                    <h1 style={{ fontSize: '2.5rem', margin: '0 0 30px 0', textAlign: 'center', lineHeight: '1.1' }}>GESTIONAR<br/>HUESPED</h1>
                    <button onClick={onCancel} style={{ backgroundColor: '#ffbdad', border: '1px solid #cc8b79', padding: '10px 30px', fontWeight: 'bold', cursor: 'pointer' }}>Cancelar</button>
                </div>

                {/* Panel Derecho */}
                <div style={{ width: '65%', padding: '40px', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                    <h3 style={{ marginTop: 0, color: '#666', marginBottom: '20px', textAlign: 'center' }}>Huéspedes asignados: <strong>{listaHuespedesAsignados.length}</strong></h3>
                    <form onSubmit={handleBuscarHuesped} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                        <div>
                            <label style={{ display: 'block', fontWeight: 'bold', marginBottom: '5px' }}>Apellido</label>
                            <input value={busqueda.apellido} onChange={e=>setBusqueda({...busqueda, apellido: e.target.value.toUpperCase()})} style={{ width: '100%', padding: '8px', border: '1px solid #ccc', textTransform: 'uppercase' }} placeholder="Apellido"/>
                        </div>
                        <div>
                            <label style={{ display: 'block', fontWeight: 'bold', marginBottom: '5px' }}>Nombre</label>
                            <input value={busqueda.nombre} onChange={e=>setBusqueda({...busqueda, nombre: e.target.value.toUpperCase()})} style={{ width: '100%', padding: '8px', border: '1px solid #ccc', textTransform: 'uppercase' }} placeholder="Nombre"/>
                        </div>
                        <div style={{ display: 'flex', gap: '15px' }}>
                            <div style={{ width: '30%' }}>
                                <label style={{ display: 'block', fontWeight: 'bold', marginBottom: '5px' }}>Tipo</label>
                                <select value={busqueda.tipo} onChange={e=>setBusqueda({...busqueda, tipo: e.target.value})} style={{ width: '100%', padding: '8px', border: '1px solid #ccc' }}>
                                    <option value="DNI">DNI</option>
                                    <option value="LE">LE</option>
                                    <option value="LC">LC</option>
                                    <option value="Pasaporte">Pasaporte</option>
                                </select>
                            </div>
                            <div style={{ flexGrow: 1 }}>
                                <label style={{ display: 'block', fontWeight: 'bold', marginBottom: '5px' }}>Documento</label>
                                <input value={busqueda.documento} onChange={e=>setBusqueda({...busqueda, documento: e.target.value})} style={{ width: '100%', padding: '8px', border: '1px solid #ccc' }} placeholder="Número"/>
                            </div>
                        </div>
                        <div style={{ textAlign: 'right', marginTop: '20px' }}>
                            <button type="submit" style={{ backgroundColor: '#dceca4', border: '1px solid #999', padding: '10px 40px', fontWeight: 'bold', cursor: 'pointer' }}>ACEPTAR</button>
                        </div>
                    </form>
                </div>
            </div>
        );
    }

    // 7. RESULTADOS
    if (paso === 'RESULTADOS_HUESPED') {
        return (
            <div style={{ width: '700px', margin: '20px auto', border: '1px solid #000', backgroundColor: '#f9f9f9' }}>
                {modal.show && <ModalAlerta mensaje={modal.msg} onAceptar={()=>setModal({show:false, msg:''})} />}
                <div style={{ backgroundColor: '#dceca4', padding: '15px', textAlign: 'center', borderBottom: '1px solid #999' }}>
                    <h2 style={{ margin: 0 }}>Resultados</h2>
                </div>
                <div style={{ padding: '20px' }}>
                    <table style={{ width: '100%', borderCollapse: 'collapse', border: '1px solid #ccc' }}>
                        <thead style={{ backgroundColor: '#e0e0e0' }}>
                        <tr><th>Nombre</th><th>Apellido</th><th>Tipo</th><th>Documento</th></tr>
                        </thead>
                        <tbody>
                        {listaResultados.map(h => {
                            const yaAsignado = listaHuespedesAsignados.some(ha => ha.id === h.id);
                            return (
                                <tr key={h.id} onClick={() => setHuespedTemporal(h)}
                                    style={{
                                        backgroundColor: huespedTemporal?.id === h.id ? '#dceca4' : (yaAsignado ? '#eee' : '#fff'),
                                        cursor: 'pointer',
                                        borderBottom: '1px solid #eee',
                                        color: yaAsignado ? '#999' : '#000'
                                    }}>
                                    <td style={{padding:'10px'}}>{h.nombre}</td>
                                    <td>{h.apellido}</td>
                                    <td>{h.tipoDocumento}</td>
                                    <td>{h.documento} {yaAsignado ? '(Cargado)' : ''}</td>
                                </tr>
                            );
                        })}
                        </tbody>
                    </table>
                    <div style={{ display: 'flex', justifyContent: 'center', gap: '20px', marginTop: '30px' }}>
                        <button style={{ backgroundColor: '#ffbdad', border: '1px solid #cc8b79', padding: '10px 30px', fontWeight: 'bold' }} onClick={() => setPaso('BUSQUEDA_HUESPED')}>Cancelar</button>
                        <button style={{ backgroundColor: '#dceca4', border: '1px solid #999', padding: '10px 30px', fontWeight: 'bold' }} onClick={handleAceptarHuesped}>ACEPTAR</button>
                    </div>
                </div>
            </div>
        );
    }

    // 10. GESTION FINAL
    if (paso === 'GESTION_FINAL') {
        return (
            <div style={{ width: '700px', margin: '20px auto', border: '1px solid #000', backgroundColor: '#fff', minHeight:'500px', display:'flex', flexDirection:'column' }}>
                <div style={{ backgroundColor: '#dceca4', padding: '40px', textAlign: 'center', borderBottom: '1px solid #000' }}>
                    <h1 style={{ margin: 0, fontSize: '3rem' }}>INFORMACION</h1>
                    <div style={{ marginTop:'20px', textAlign:'left', padding:'0 20px' }}>
                        <strong>Habitación:</strong> {seleccion?.numeroHabitacion} ({seleccion?.tipoHabitacion})<br/>
                        <strong>Huéspedes a registrar:</strong>
                        <ul style={{ maxHeight: '100px', overflowY: 'auto' }}>
                            {listaHuespedesAsignados.map((h, i) => <li key={i}>{h.apellido}, {h.nombre} ({h.documento})</li>)}
                        </ul>
                    </div>
                </div>
                <div style={{ flex: 1, display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '20px', padding: '20px' }}>
                    <button style={{ backgroundColor: '#ffbdad', border: '1px solid #cc8b79', padding: '20px 30px', fontWeight: 'bold', width:'150px' }} onClick={() => crearEstadia('SALIR')}>SALIR</button>
                    <button style={{ backgroundColor: '#eee', border: '1px solid #999', padding: '20px 30px', fontWeight: 'bold', width:'150px' }} onClick={() => { setBusqueda({nombre:'', apellido:'', tipo:'DNI', documento:''}); setPaso('BUSQUEDA_HUESPED'); }}>SEGUIR CARGANDO</button>
                    <button style={{ backgroundColor: '#dceca4', border: '1px solid #999', padding: '20px 30px', fontWeight: 'bold', width:'150px' }} onClick={() => crearEstadia('CARGAR_OTRA')}>CARGAR OTRA HABITACION</button>
                </div>
            </div>
        )
    }

    return null;
}