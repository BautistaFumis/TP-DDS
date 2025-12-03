'use client';

import { useState, useMemo } from 'react';
import ModalAlerta from './ModalAlerta';


export interface Celda {
    idHabitacion: string;
    numero: string;
    tipoHabitacion: string;
    estado: string;
    texto: string;
}

export interface Fila {
    fechaStr: string;
    celdas: Celda[];
}

export interface SeleccionReserva {
    idHabitacion: string;
    numeroHabitacion: string;
    tipoHabitacion: string;
    fechaInicio: string;
    fechaFin: string;
    fechaInicioDisplay: string;
    fechaFinDisplay: string;
}


interface Props {
    onCancel?: () => void;
    grillaExterna?: Fila[];
    onCeldaClickExterna?: (celda: Celda, idxFila: number) => void;
    getEstiloCeldaExterna?: (celda: Celda, idxFila: number) => { style: React.CSSProperties, texto: string };
    children?: React.ReactNode;
}

export default function MostrarEstadoHabitaciones({
                                                      onCancel,
                                                      grillaExterna,
                                                      onCeldaClickExterna,
                                                      getEstiloCeldaExterna,
                                                      children
                                                  }: Props) {


    const [fechaDesde, setFechaDesde] = useState('');
    const [fechaHasta, setFechaHasta] = useState('');
    const [grillaInterna, setGrillaInterna] = useState<Fila[]>([]);
    const [cargando, setCargando] = useState(false);
    const [modalError, setModalError] = useState<{show: boolean, msg: string | React.ReactNode}>({show: false, msg: ''});


    const [filtroTipo, setFiltroTipo] = useState<string>('Todos');


    const grilla = grillaExterna || grillaInterna;
    const esModoExterno = !!grillaExterna;


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


    const handleConsultar = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!fechaDesde || !fechaHasta) { setModalError({ show: true, msg: "Debe ingresar ambas fechas." }); return; }
        setCargando(true);
        try {
            const res = await fetch(`http://localhost:8081/api/habitaciones/estado?desde=${fechaDesde}&hasta=${fechaHasta}`);
            if(!res.ok) throw new Error("Error de servidor");
            const data: Fila[] = await res.json();

            const hayLibres = data.some(f => f.celdas.some(c => c.estado === 'LIBRE'));
            if (!hayLibres && !esModoExterno) {

                setModalError({ show: true, msg: "No existen habitaciones disponibles en el rango." });
            }
            setGrillaInterna(data);
        } catch (err: any) {
            setModalError({ show: true, msg: "Error de conexión." });
        } finally {
            setCargando(false);
        }
    };


    const getEstiloDefecto = (celda: Celda) => {
        let style: React.CSSProperties = { cursor: 'default', border: '1px solid #999', padding: '8px', textAlign: 'center', fontWeight: 'bold' };
        switch(celda.estado) {
            case 'LIBRE': style.backgroundColor = '#4a7c35'; style.color = 'white'; break;
            case 'OCUPADA': style.backgroundColor = '#fcd5d5'; style.color = '#333'; break;
            case 'RESERVADA': style.backgroundColor = '#1a3b70'; style.color = 'white'; break;
            case 'MANTENIMIENTO': style.backgroundColor = '#ccc'; style.color = '#333'; break;
            default: style.backgroundColor = '#ccc';
        }
        return { style, texto: celda.texto };
    };

    return (
        <div style={{ width: '900px', margin: '0 auto', display: 'flex', flexDirection: 'column' }}>
            {modalError.show && (
                <ModalAlerta mensaje={modalError.msg} onAceptar={() => setModalError({show: false, msg: ''})} />
            )}

            <div style={{ backgroundColor: '#dceca4', padding: '15px', textAlign: 'center', borderBottom: '1px solid #999' }}>
                <h1 style={{ margin: 0, fontSize: '2rem', color: 'black' }}>Estado de Habitaciones</h1>
            </div>

            <div style={{ padding: '20px', display: 'flex', flexDirection: 'column', gap: '20px', backgroundColor: '#f9f9f9', border: '1px solid #000', borderTop: 'none' }}>

                {/* Solo mostramos el buscador de fechas si NO estamos en modo externo */}
                {!esModoExterno && (
                    <form onSubmit={handleConsultar} style={{ display: 'flex', gap: '20px', alignItems: 'flex-end' }}>
                        <div><label>Desde:</label><input type="date" value={fechaDesde} onChange={e => setFechaDesde(e.target.value)} /></div>
                        <div><label>Hasta:</label><input type="date" value={fechaHasta} onChange={e => setFechaHasta(e.target.value)} /></div>
                        <button type="submit" disabled={cargando} style={{padding: '5px 15px'}}>{cargando ? '...' : 'Consultar'}</button>
                    </form>
                )}

                {/* Filtro de Tipo de Habitación */}
                {grilla.length > 0 && (
                    <div>
                        <select
                            value={filtroTipo}
                            onChange={(e) => setFiltroTipo(e.target.value)}
                            style={{ padding: '8px', fontSize: '1rem', border: '1px solid #ccc', borderRadius: '4px' }}
                        >
                            {tiposDisponibles.map(t => <option key={t} value={t}>{t}</option>)}
                        </select>
                    </div>
                )}


                {grilla.length > 0 ? (
                    <div style={{ overflowX: 'auto', border: '1px solid #999', maxHeight: '400px' }}>
                        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                            <thead>
                            <tr>
                                <th style={{ backgroundColor: '#e0e0e0', padding: '10px', position: 'sticky', top: 0, border: '1px solid #999' }}>Día \ Hab</th>
                                {indicesColumnasVisibles.map(idx => {
                                    const c = grilla[0].celdas[idx];
                                    return <th key={c.idHabitacion} style={{ backgroundColor: '#e0e0e0', padding: '10px', position: 'sticky', top: 0, border: '1px solid #999' }}>{c.numero}</th>
                                })}
                            </tr>
                            </thead>
                            <tbody>
                            {grilla.map((fila, rowIndex) => (
                                <tr key={rowIndex}>
                                    <td style={{ padding: '8px', backgroundColor: '#eee', fontWeight: 'bold', border: '1px solid #999' }}>{fila.fechaStr}</td>
                                    {indicesColumnasVisibles.map(idx => {
                                        const celda = fila.celdas[idx];


                                        const { style, texto } = getEstiloCeldaExterna
                                            ? getEstiloCeldaExterna(celda, rowIndex)
                                            : getEstiloDefecto(celda);

                                        return (
                                            <td
                                                key={celda.idHabitacion}
                                                onClick={() => {

                                                    if (onCeldaClickExterna) onCeldaClickExterna(celda, rowIndex);
                                                }}
                                                style={style}
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
                ) : (
                    !esModoExterno && <p>Ingrese fechas para consultar.</p>
                )}

                {/* Area de Botones: Si hay children (botones de Reservar/Ocupar), los renderiza. Si no, botón cancelar por defecto */}
                <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '20px' }}>
                    {children ? children : (
                        onCancel && <button onClick={onCancel} style={{padding:'10px 20px'}}>Volver</button>
                    )}
                </div>
            </div>
        </div>
    );
}