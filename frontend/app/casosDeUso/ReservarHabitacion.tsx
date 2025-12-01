'use client';
import { useState, useMemo, useRef } from 'react';
import ModalAlerta from './ModalAlerta';

// --- DEFINICIONES ---
interface Celda {
    idHabitacion: string;
    numero: string;
    estado: string;
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
}

type Paso = 'INGRESO_FECHAS' | 'SELECCION_GRILLA' | 'CONFIRMACION' | 'DATOS_HUESPED' | 'FINAL';

export default function ReservarHabitacion({ onCancel }: { onCancel: () => void }) {
    // ESTADOS
    const [paso, setPaso] = useState<Paso>('INGRESO_FECHAS');
    const [fechaInicio, setFechaInicio] = useState('');
    const [fechaFin, setFechaFin] = useState('');

    const [grilla, setGrilla] = useState<Fila[]>([]);
    const [filtroTipo, setFiltroTipo] = useState<string>('Todos');
    const [selecciones, setSelecciones] = useState<Seleccion[]>([]);
    const [huesped, setHuesped] = useState({ nombre: '', apellido: '', telefono: '' });

    const [modal, setModal] = useState<{show: boolean, msg: string | React.ReactNode}>({show:false, msg:''});
    const [primerClick, setPrimerClick] = useState<{ idHab: string, idx: number } | null>(null);

    // --- FILTROS ---
    const tiposDisponibles = useMemo(() => {
        if (grilla.length === 0) return [];
        const tipos = new Set(grilla[0].celdas.map(c => c.tipoHabitacion));
        return ['Todos', ...Array.from(tipos)];
    }, [grilla]);

    const indicesColumnasVisibles = useMemo(() => {
        if (grilla.length === 0) return [];
        return grilla[0].celdas
            .map((c, i) => (filtroTipo === 'Todos' || c.tipoHabitacion === filtroTipo) ? i : -1)
            .filter(i => i !== -1);
    }, [grilla, filtroTipo]);

    // --- ACCIONES ---

    const handleConsultar = async () => {
        // Limpiamos selecciones anteriores al hacer una nueva consulta
        setSelecciones([]);

        if (!fechaInicio || !fechaFin) {
            setModal({show: true, msg: 'Debe ingresar ambas fechas'});
            return;
        }
        if (fechaInicio > fechaFin) {
            setModal({show: true, msg: <>Fecha invalidada por el siguiente motivo: <br/><b>La fecha de fin es anterior a la de inicio</b></>});
            return;
        }

        try {
            const res = await fetch(`http://localhost:8081/api/habitaciones/estado?desde=${fechaInicio}&hasta=${fechaFin}`);
            const data: Fila[] = await res.json();

            const hayLibres = data.some(f => f.celdas.some(c => c.estado === 'LIBRE'));
            if (!hayLibres) {
                setModal({show: true, msg: 'No existen habitaciones disponibles para el periodo indicado'});
            }

            setGrilla(data);
            setPaso('SELECCION_GRILLA');

        } catch (e) {
            setModal({show: true, msg: 'Error de conexión'});
        }
    };

    const handleCellClick = (celda: Celda, idxFila: number) => {
        // 1. Validar Backend (Si está ocupada/reservada en BD)
        if (celda.estado !== 'LIBRE') {
            setModal({show: true, msg: 'La habitación seleccionada no está disponible.'});
            return;
        }

        // 2. Validar Local (Si YA la seleccioné yo mismo en azul)
        // Verificamos si el click cae sobre una selección existente
        const yaSeleccionada = selecciones.some(s =>
            s.idHabitacion === celda.idHabitacion && idxFila >= s.indiceInicio && idxFila <= s.indiceFin
        );

        if (yaSeleccionada) {
            setModal({show: true, msg: 'Esta habitación ya se encuentra seleccionada para reservar.'});
            setPrimerClick(null);
            return;
        }

        if (!primerClick) {
            // Primer Click
            setPrimerClick({ idHab: celda.idHabitacion, idx: idxFila });
        } else {
            // Segundo Click
            if (primerClick.idHab !== celda.idHabitacion) {
                setModal({show: true, msg: 'Debe seleccionar inicio y fin en la misma habitación'});
                setPrimerClick(null);
                return;
            }

            const ini = Math.min(primerClick.idx, idxFila);
            const fin = Math.max(primerClick.idx, idxFila);

            // Validar Rango
            for(let i=ini; i<=fin; i++) {
                // A. Check Backend
                const c = grilla[i].celdas.find(x => x.idHabitacion === celda.idHabitacion);
                if(c?.estado !== 'LIBRE') {
                    setModal({show: true, msg: 'El rango seleccionado contiene días ocupados.'});
                    setPrimerClick(null);
                    return;
                }

                // B. Check Local (Superposición con mis otras selecciones azules)
                const superponeLocal = selecciones.some(s =>
                    s.idHabitacion === celda.idHabitacion && i >= s.indiceInicio && i <= s.indiceFin
                );

                if (superponeLocal) {
                    setModal({show: true, msg: 'El rango se superpone con una selección previa.'});
                    setPrimerClick(null);
                    return;
                }
            }

            const nueva: Seleccion = {
                idHabitacion: celda.idHabitacion,
                numeroHabitacion: celda.numero,
                tipoHabitacion: celda.tipoHabitacion,
                fechaInicio: convertirFechaIso(grilla[ini].fechaStr),
                fechaFin: convertirFechaIso(grilla[fin].fechaStr),
                fechaInicioDisplay: grilla[ini].fechaStr,
                fechaFinDisplay: grilla[fin].fechaStr,
                indiceInicio: ini,
                indiceFin: fin
            };

            setSelecciones([...selecciones, nueva]);
            setPrimerClick(null);
        }
    };

    const guardarReserva = async () => {
        if (!huesped.nombre || !huesped.apellido || !huesped.telefono) {
            setModal({show:true, msg: "Debe completar todos los campos para continuar con la reserva"});
            return;
        }

        const dto = {
            nombre: huesped.nombre,
            apellido: huesped.apellido,
            telefono: huesped.telefono,
            items: selecciones.map(s => ({
                idHabitacion: Number(s.idHabitacion),
                fechaInicio: s.fechaInicio,
                fechaFin: s.fechaFin
            }))
        };

        try {
            const res = await fetch('http://localhost:8081/api/reservas/crear', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(dto)
            });

            if (res.ok) {
                setModal({show: true, msg: "Reserva confirmada exitosamente. Presione el boton para continuar"});
                // Al cerrar el modal, volvemos al menú principal
                setTimeout(() => onCancel(), 2000);
            } else {
                setModal({show: true, msg: "Error al guardar la reserva"});
            }
        } catch(e) {
            setModal({show: true, msg: "Error de conexión al guardar"});
        }
    };

    const convertirFechaIso = (f: string) => { const [d,m,a] = f.split('/'); return `${a}-${m}-${d}`; }

    // --- RENDERIZADO VISUAL ---

    const getCellStyle = (celda: Celda, idxFila: number) => {
        let bg = '#ccc'; let color = '#000'; let cursor = 'not-allowed'; let texto = celda.texto;

        if (celda.estado === 'LIBRE') { bg = '#4a7c35'; color = 'white'; cursor = 'pointer'; }
        else if (celda.estado === 'OCUPADA') { bg = '#ffd1dc'; color = 'black'; }
        else if (celda.estado === 'RESERVADA') { bg = '#f4b942'; color = 'black'; }

        // Sobre-escribir si es Selección del Usuario (AZUL)
        const estaSeleccionada = selecciones.some(sel =>
            sel.idHabitacion === celda.idHabitacion && idxFila >= sel.indiceInicio && idxFila <= sel.indiceFin
        );

        if (estaSeleccionada) {
            bg = '#1a3b70'; color = 'white'; cursor = 'not-allowed'; // Cursor bloqueado porque ya está elegida
            texto = "RESERVADA";
        }

        let border = '1px solid #999';
        if (primerClick?.idHab === celda.idHabitacion && primerClick.idx === idxFila) {
            border = '3px solid yellow';
        }

        return { style: { backgroundColor: bg, color, cursor, border, padding: '10px', textAlign: 'center' as const, fontWeight: 'bold' }, texto };
    };

    // --- VISTAS ---

    if (paso === 'INGRESO_FECHAS') {
        return (
            <div style={{ width: '800px', margin: '0 auto', border: '1px solid #000', backgroundColor: '#f9f9f9', display: 'flex' }}>
                {modal.show && <ModalAlerta mensaje={modal.msg} onAceptar={()=>setModal({show:false, msg:''})} />}
                <div style={{ backgroundColor: '#dceca4', width: '50%', padding: '40px', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                    <h1 style={{ fontSize: '3rem', margin: 0, lineHeight: 1.2 }}>RESERVAR<br/>HABITACION</h1>
                    <button style={{ marginTop: '50px', backgroundColor: '#ffbdad', border: '1px solid #cc8b79', padding: '10px 20px', fontWeight: 'bold', width: 'fit-content' }} onClick={onCancel}>Cancelar</button>
                </div>
                <div style={{ width: '50%', padding: '40px', display: 'flex', flexDirection: 'column', gap: '20px' }}>
                    <div><label style={{fontWeight:'bold'}}>Fecha Inicio</label><input type="date" value={fechaInicio} onChange={e=>setFechaInicio(e.target.value)} style={{width: '100%', padding:'5px'}}/></div>
                    <div><label style={{fontWeight:'bold'}}>Fecha Final</label><input type="date" value={fechaFin} onChange={e=>setFechaFin(e.target.value)} style={{width: '100%', padding:'5px'}}/></div>
                    <div style={{display:'flex', justifyContent:'flex-end', marginTop:'20px'}}>
                        <button style={{ backgroundColor: '#dceca4', border: '1px solid #999', padding: '10px 30px', fontWeight: 'bold' }} onClick={handleConsultar}>Consultar</button>
                    </div>
                </div>
            </div>
        );
    }

    if (paso === 'SELECCION_GRILLA') {
        return (
            <div style={{ width: '900px', margin: '0 auto', border: '1px solid #000', backgroundColor: '#f9f9f9' }}>
                {modal.show && <ModalAlerta mensaje={modal.msg} onAceptar={()=>setModal({show:false, msg:''})} />}
                <div style={{ backgroundColor: '#dceca4', padding: '15px', textAlign: 'center', borderBottom: '1px solid #999' }}>
                    <h2 style={{ margin: 0, fontSize: '2rem' }}>Estado de Habitaciones</h2>
                </div>

                <div style={{ padding: '20px' }}>
                    <div style={{ marginBottom: '15px' }}>
                        <select
                            value={filtroTipo}
                            onChange={(e) => setFiltroTipo(e.target.value)}
                            style={{ padding: '8px', fontSize: '1rem', border: '1px solid #ccc', borderRadius: '4px' }}
                        >
                            {tiposDisponibles.map(t => <option key={t} value={t}>{t}</option>)}
                        </select>
                    </div>

                    <div style={{ overflowX: 'auto', marginBottom: '20px' }}>
                        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                            <thead>
                            <tr>
                                <th style={{padding:'10px', background:'#ddd', border:'1px solid #999'}}>Día \ Habitación</th>
                                {indicesColumnasVisibles.map(idx => {
                                    const c = grilla[0].celdas[idx];
                                    return <th key={c.idHabitacion} style={{padding:'10px', background:'#ddd', border:'1px solid #999'}}>{c.numero}</th>
                                })}
                            </tr>
                            </thead>
                            <tbody>
                            {grilla.map((fila, i) => (
                                <tr key={i}>
                                    <td style={{padding:'10px', background:'#eee', border:'1px solid #999', fontWeight:'bold'}}>{fila.fechaStr}</td>
                                    {indicesColumnasVisibles.map(idx => {
                                        const celda = fila.celdas[idx];
                                        const { style, texto } = getCellStyle(celda, i);
                                        return (
                                            <td key={celda.idHabitacion}
                                                style={style}
                                                onClick={() => handleCellClick(celda, i)}
                                            >
                                                {texto}
                                            </td>
                                        );
                                    })}
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>

                    <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                        {/* BOTÓN CANCELAR: Limpia selecciones y vuelve al inicio */}
                        <button style={{ backgroundColor: '#ffbdad', border: '1px solid #cc8b79', padding: '10px 30px', fontWeight: 'bold' }}
                                onClick={() => { setSelecciones([]); setPaso('INGRESO_FECHAS'); }}>Cancelar</button>

                        <button
                            disabled={selecciones.length === 0}
                            style={{ backgroundColor: selecciones.length > 0 ? '#999' : '#ccc', border: '1px solid #666', padding: '10px 30px', fontWeight: 'bold', color: 'white', cursor: selecciones.length>0?'pointer':'default' }}
                            onClick={() => setPaso('CONFIRMACION')}
                        >
                            Continuar
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    if (paso === 'CONFIRMACION') {
        return (
            <div className="modal-overlay">
                <div style={{ backgroundColor: '#e0e0e0', padding: '20px', width: '700px', border: '1px solid #000', textAlign: 'center' }}>
                    <div style={{ backgroundColor: '#dceca4', margin: '-20px -20px 20px -20px', padding: '10px' }}>
                        <h2 style={{margin:0, color: '#777'}}>Estado de Habitaciones</h2>
                    </div>
                    <h1 style={{ marginBottom: '20px' }}>Reservar</h1>

                    <div style={{ display: 'flex', gap: '0', border: '1px solid #000', backgroundColor: '#eee', overflowX: 'auto', whiteSpace: 'nowrap' }}>
                        {selecciones.map((sel, idx) => (
                            <div key={idx} style={{ padding: '15px', borderRight: '1px solid #000', minWidth: '220px', textAlign: 'left', fontSize: '0.9rem', display: 'inline-block', verticalAlign: 'top' }}>
                                <div><strong>tipoDeHabitación:</strong> {sel.tipoHabitacion} {sel.numeroHabitacion}</div>
                                <div style={{marginTop:'5px'}}>✓ Ingreso: {sel.fechaInicioDisplay}, 12:00hs.</div>
                                <div style={{marginTop:'5px'}}>✓ Egreso: {sel.fechaFinDisplay}, 10:00hs</div>
                            </div>
                        ))}
                    </div>

                    <div style={{ display: 'flex', justifyContent: 'center', gap: '20px', marginTop: '30px' }}>
                        <button style={{ backgroundColor: '#ffbdad', border: '1px solid #cc8b79', padding: '10px 30px', fontWeight: 'bold' }}
                                onClick={() => { setSelecciones([]); setPaso('SELECCION_GRILLA'); }}>Rechazar</button>
                        <button style={{ backgroundColor: '#dceca4', border: '1px solid #999', padding: '10px 30px', fontWeight: 'bold' }}
                                onClick={() => setPaso('DATOS_HUESPED')}>Aceptar</button>
                    </div>
                </div>
            </div>
        );
    }

    if (paso === 'DATOS_HUESPED') {
        return (
            <div style={{ width: '600px', margin: '0 auto', border: '1px solid #000', backgroundColor: '#f9f9f9' }}>
                <div style={{ backgroundColor: '#dceca4', padding: '15px', textAlign: 'center', borderBottom: '1px solid #999' }}>
                    <h2 style={{ margin: 0 }}>Ingrese los datos solicitados</h2>
                </div>
                {modal.show && <ModalAlerta mensaje={modal.msg} onAceptar={()=>setModal({show:false, msg:''})} />}

                <div style={{ padding: '40px', display: 'flex', flexDirection: 'column', gap: '20px' }}>
                    <input placeholder="Nombre" value={huesped.nombre} onChange={e=>setHuesped({...huesped, nombre: e.target.value.toUpperCase()})} style={{padding:'10px', fontStyle:'italic', border: '1px solid #999'}}/>
                    <input placeholder="Apellido" value={huesped.apellido} onChange={e=>setHuesped({...huesped, apellido: e.target.value.toUpperCase()})} style={{padding:'10px', fontStyle:'italic', border: '1px solid #999'}}/>
                    <input placeholder="Telefono" value={huesped.telefono} onChange={e=>setHuesped({...huesped, telefono: e.target.value})} style={{padding:'10px', fontStyle:'italic', border: '1px solid #999'}}/>

                    <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '20px' }}>
                        <button style={{ backgroundColor: '#ffbdad', border: '1px solid #cc8b79', padding: '10px 30px', fontWeight: 'bold' }} onClick={() => setPaso('SELECCION_GRILLA')}>Cancelar</button>
                        <button style={{ backgroundColor: '#999', border: '1px solid #666', padding: '10px 30px', fontWeight: 'bold', color:'white' }} onClick={guardarReserva}>Continuar</button>
                    </div>
                </div>
            </div>
        );
    }

    return null;
}