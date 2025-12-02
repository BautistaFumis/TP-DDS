'use client';

import { useState } from 'react';
import ModalAlerta from './ModalAlerta'; // Asegúrate de importar el componente creado arriba

// --- TIPOS EXPORTADOS ---
export interface Celda {
    idHabitacion: string;
    numero: string;
    tipoHabitacion: string;
    estado: 'LIBRE' | 'OCUPADA' | 'RESERVADA' | 'MANTENIMIENTO';
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
    fechaInicio: string; // YYYY-MM-DD
    fechaFin: string;    // YYYY-MM-DD
    fechaInicioDisplay: string;
    fechaFinDisplay: string;
}

interface Props {
    onCancel: () => void;
    modoSeleccion?: boolean;
    onContinuarSeleccion?: (selecciones: SeleccionReserva[]) => void;
}

export default function MostrarEstadoHabitaciones({ onCancel, modoSeleccion = false, onContinuarSeleccion }: Props) {
    const [fechaDesde, setFechaDesde] = useState('');
    const [fechaHasta, setFechaHasta] = useState('');
    const [grilla, setGrilla] = useState<Fila[]>([]);
    const [cargando, setCargando] = useState(false);

    const [modalError, setModalError] = useState<{show: boolean, msg: string | React.ReactNode}>({show: false, msg: ''});

    // estado para manejar los clicks en la seleccion
    const [primerClick, setPrimerClick] = useState<{ idHabitacion: string, fechaIndex: number } | null>(null);
    const [selecciones, setSelecciones] = useState<SeleccionReserva[]>([]);

    const handleConsultar = async (e: React.FormEvent) => {
        e.preventDefault();
        setSelecciones([]);
        setPrimerClick(null);

        if (!fechaDesde || !fechaHasta) {
            setModalError({ show: true, msg: "Debe ingresar ambas fechas." });
            return;
        }

        if (fechaDesde > fechaHasta) {
            setModalError({
                show: true,
                msg: <>Fecha invalidada por el siguiente motivo: <br/>La fecha de fin es anterior a la de inicio</>
            });
            return;
        }

        setCargando(true);
        try {
            const res = await fetch(`http://localhost:8081/api/habitaciones/estado?desde=${fechaDesde}&hasta=${fechaHasta}`);
            if(!res.ok) throw new Error("Error de servidor");
            const data: Fila[] = await res.json();

            // verificamos si hay al menos una celda libre en toda la matriz
            let hayDisponibilidad = false;
            if (data.length > 0) {
                // Revisamos todas las celdas
                for(const fila of data) {
                    if (fila.celdas.some(c => c.estado === 'LIBRE')) {
                        hayDisponibilidad = true;
                        break;
                    }
                }
            }

            if (!hayDisponibilidad) {
                setModalError({
                    show: true,
                    msg: "No existen habitaciones disponibles con las comodidades deseadas para el rango de fechas solicitado."
                });
                setGrilla([]);
            } else {
                setGrilla(data);
            }

        } catch (err: any) {
            setModalError({ show: true, msg: "Error de conexión." });
        } finally {
            setCargando(false);
        }
    };

    const handleCeldaClick = (idHabitacion: string, numero: string, tipo: string, rowIndex: number, estado: string) => {
        if (!modoSeleccion) return;

        if (estado !== 'LIBRE') {
            setModalError({ show: true, msg: "La habitación seleccionada no está disponible." });
            return;
        }

        if (!primerClick) {
            setPrimerClick({ idHabitacion, fechaIndex: rowIndex });
            return;
        }

        if (primerClick.idHabitacion !== idHabitacion) {
            setModalError({ show: true, msg: "Debe seleccionar inicio y fin en la misma habitación." });
            setPrimerClick(null);
            return;
        }

        const inicioIdx = Math.min(primerClick.fechaIndex, rowIndex);
        const finIdx = Math.max(primerClick.fechaIndex, rowIndex);

        for (let i = inicioIdx; i <= finIdx; i++) {
            const celda = grilla[i].celdas.find(c => c.idHabitacion === idHabitacion);
            if (celda?.estado !== 'LIBRE') {
                setModalError({ show: true, msg: "El rango seleccionado contiene días ocupados." });
                setPrimerClick(null);
                return;
            }
        }

        const nuevaSeleccion: SeleccionReserva = {
            idHabitacion,
            numeroHabitacion: numero,
            tipoHabitacion: tipo,
            fechaInicioDisplay: grilla[inicioIdx].fechaStr,
            fechaFinDisplay: grilla[finIdx].fechaStr,
            fechaInicio: convertirFechaIso(grilla[inicioIdx].fechaStr),
            fechaFin: convertirFechaIso(grilla[finIdx].fechaStr)
        };

        setSelecciones([...selecciones, nuevaSeleccion]);
        setPrimerClick(null);
    };

    const convertirFechaIso = (fechaStr: string) => {
        const [dia, mes, anio] = fechaStr.split('/');
        return `${anio}-${mes}-${dia}`;
    }

    const getEstiloCelda = (estado: string, idHab: string, idx: number) => {
        let style: any = { cursor: modoSeleccion && estado === 'LIBRE' ? 'pointer' : 'default' };
        switch(estado) {
            case 'LIBRE': style.backgroundColor = '#4a7c35'; style.color = 'white'; break;
            case 'OCUPADA': style.backgroundColor = '#fcd5d5'; style.color = '#333'; break;
            case 'RESERVADA': style.backgroundColor = '#1a3b70'; style.color = 'white'; break;
            case 'MANTENIMIENTO': style.backgroundColor = '#ccc'; style.color = '#333'; break;
        }
        if (primerClick && primerClick.idHabitacion === idHab && primerClick.fechaIndex === idx) {
            style.border = '3px solid yellow';
        }
        return style;
    };

    return (
        <div className="search-container" style={{flexDirection: 'column', width: '950px'}}>
            {modalError.show && (
                <ModalAlerta
                    mensaje={modalError.msg}
                    onAceptar={() => setModalError({show: false, msg: ''})}
                />
            )}

            <div style={{ backgroundColor: '#d4e59f', padding: '15px', textAlign: 'center' }}>
                <h1 style={{ margin: 0, fontSize: '2rem', color: 'black' }}>
                    {modoSeleccion ? 'Reservar Habitación' : 'Estado de Habitaciones'}
                </h1>
            </div>

            <div style={{ padding: '20px', display: 'flex', flexDirection: 'column', gap: '20px' }}>
                {/* Formulario Fechas */}
                <form onSubmit={handleConsultar} style={{ display: 'flex', gap: '20px', alignItems: 'flex-end' }}>
                    <div className="form-group"><label>Desde:</label><input type="date" value={fechaDesde} onChange={e => setFechaDesde(e.target.value)} /></div>
                    <div className="form-group"><label>Hasta:</label><input type="date" value={fechaHasta} onChange={e => setFechaHasta(e.target.value)} /></div>
                    <button type="submit" className="search-button" style={{ width: '150px' }} disabled={cargando}>{cargando ? '...' : 'Consultar'}</button>
                </form>

                {modoSeleccion && grilla.length > 0 && (
                    <div className="success-message">
                        Seleccione rango (Click Inicio + Click Fin). Selecciones: <strong>{selecciones.length}</strong>
                    </div>
                )}


                {grilla.length > 0 && (
                    <div style={{ overflowX: 'auto', border: '1px solid #999', maxHeight: '400px' }}>
                        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                            <thead>
                            <tr>
                                <th style={{ backgroundColor: '#e0e0e0', padding: '10px', position: 'sticky', top: 0 }}>Día \ Hab</th>
                                {grilla[0].celdas.map(hab => (
                                    <th key={hab.idHabitacion} style={{ backgroundColor: '#e0e0e0', padding: '10px', position: 'sticky', top: 0 }}>{hab.numero}</th>
                                ))}
                            </tr>
                            </thead>
                            <tbody>
                            {grilla.map((fila, rowIndex) => (
                                <tr key={rowIndex}>
                                    <td style={{ padding: '8px', backgroundColor: '#eee', fontWeight: 'bold' }}>{fila.fechaStr}</td>
                                    {fila.celdas.map(celda => (
                                        <td
                                            key={celda.idHabitacion}
                                            onClick={() => handleCeldaClick(celda.idHabitacion, celda.numero, celda.tipoHabitacion, rowIndex, celda.estado)}
                                            style={{
                                                border: '1px solid #999', padding: '8px', textAlign: 'center', fontWeight: 'bold', fontSize: '0.8rem',
                                                ...getEstiloCelda(celda.estado, celda.idHabitacion, rowIndex)
                                            }}
                                        >
                                            {celda.texto}
                                        </td>
                                    ))}
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}

                <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '20px' }}>
                    <button className="cancel-button" style={{ width: '150px' }} onClick={onCancel}>Cancelar</button>
                    {modoSeleccion && selecciones.length > 0 && (
                        <button className="search-button" style={{ width: '200px' }} onClick={() => onContinuarSeleccion && onContinuarSeleccion(selecciones)}>
                            Continuar ({selecciones.length})
                        </button>
                    )}
                </div>
            </div>
        </div>
    );
}