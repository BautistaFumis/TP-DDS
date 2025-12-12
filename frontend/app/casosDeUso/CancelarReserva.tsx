'use client';
import { useState, useRef } from 'react';
import ModalAlerta from './ModalAlerta';

interface ReservaResult {
    idReserva: number;
    nombre: string;
    apellido: string;
    numeroHabitacion: string;
    tipoHabitacion: string;
    fechaInicio: string;
    fechaFin: string;
}

export default function CancelarReserva({ onCancel }: { onCancel: () => void }) {
    const [busqueda, setBusqueda] = useState({ apellido: '', nombre: '' });
    const [resultados, setResultados] = useState<ReservaResult[]>([]);
    const [seleccionados, setSeleccionados] = useState<number[]>([]);
    const [modal, setModal] = useState<{show: boolean, msg: string}>({show: false, msg: ''});
    const [vista, setVista] = useState<'BUSQUEDA' | 'CONFIRMACION'>('BUSQUEDA');

    const apellidoRef = useRef<HTMLInputElement>(null);

    const handleBuscar = async (e: React.FormEvent) => {
        e.preventDefault();
        setResultados([]);

        if (!busqueda.apellido.trim()) {
            setModal({ show: true, msg: "El campo apellido no puede estar vacío" });
            apellidoRef.current?.focus();
            return;
        }

        try {
            const params = new URLSearchParams({ apellido: busqueda.apellido, nombre: busqueda.nombre });
            const res = await fetch(`http://localhost:8081/api/reservas/buscar?${params.toString()}`);
            if (res.ok) {
                const data = await res.json();
                if (data.length === 0) {
                    setModal({ show: true, msg: "No existen reservas para los criterios de búsqueda" });
                    apellidoRef.current?.focus();
                } else {
                    setResultados(data);
                }
            } else {
                setModal({ show: true, msg: "Error al buscar reservas" });
            }
        } catch (e) {
            setModal({ show: true, msg: "Error de conexión" });
        }
    };

    const toggleSeleccion = (id: number) => {
        setSeleccionados(prev =>
            prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]
        );
    };

    const handleCancelarReservas = async () => {
        try {
            const res = await fetch('http://localhost:8081/api/reservas/cancelar', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(seleccionados)
            });

            if (res.ok) {
                setModal({ show: true, msg: "Reservas cancelada/s PRESIONE OK PARA CONTINUAR..." });
                setSeleccionados([]);
                setResultados([]);
                setBusqueda({apellido:'', nombre:''});
                setVista('BUSQUEDA');
            } else {
                setModal({ show: true, msg: "Error al cancelar reservas" });
            }
        } catch (e) {
            setModal({ show: true, msg: "Error de conexión" });
        }
    };

    return (
        <div className="search-container">
            {modal.show && <ModalAlerta mensaje={modal.msg} onAceptar={() => setModal({show:false, msg:''})} />}

            <div className="search-panel-left">
                <h1 style={{ paddingTop: '150px' }}>CANCELAR RESERVA</h1>
                <button className="cancel-button" onClick={onCancel} style={{ marginTop: 'auto' }}>Volver</button>
            </div>

            <div className="search-panel-right">
                {vista === 'BUSQUEDA' && (
                    <>
                        <form onSubmit={handleBuscar} className="search-form">
                            <div className="form-group">
                                <label>Apellido (*)</label>
                                <input
                                    ref={apellidoRef}
                                    value={busqueda.apellido}
                                    onChange={e => setBusqueda({...busqueda, apellido: e.target.value.toUpperCase()})}
                                    style={{textTransform: 'uppercase'}}
                                />
                            </div>
                            <div className="form-group">
                                <label>Nombre</label>
                                <input
                                    value={busqueda.nombre}
                                    onChange={e => setBusqueda({...busqueda, nombre: e.target.value.toUpperCase()})}
                                    style={{textTransform: 'uppercase'}}
                                />
                            </div>
                            <button type="submit" className="search-button">Buscar</button>
                        </form>

                        {resultados.length > 0 && (
                            <div style={{marginTop: '20px'}}>
                                <h3>Seleccione reservas a cancelar:</h3>
                                <div style={{maxHeight: '300px', overflowY: 'auto', border: '1px solid #ccc'}}>
                                    <table style={{width: '100%', borderCollapse: 'collapse'}}>
                                        <thead style={{backgroundColor: '#e0e0e0'}}>
                                            <tr>
                                                <th>Sel</th>
                                                <th>Huésped</th>
                                                <th>Habitación</th>
                                                <th>Desde</th>
                                                <th>Hasta</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {resultados.map(r => (
                                                <tr key={r.idReserva} style={{borderBottom: '1px solid #eee'}}>
                                                    <td style={{textAlign:'center'}}>
                                                        <input
                                                            type="checkbox"
                                                            checked={seleccionados.includes(r.idReserva)}
                                                            onChange={() => toggleSeleccion(r.idReserva)}
                                                        />
                                                    </td>
                                                    <td>{r.apellido}, {r.nombre}</td>
                                                    <td>{r.numeroHabitacion} ({r.tipoHabitacion})</td>
                                                    <td>{r.fechaInicio}</td>
                                                    <td>{r.fechaFin}</td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                </div>
                                <button
                                    className="search-button"
                                    style={{marginTop: '20px', backgroundColor: seleccionados.length > 0 ? '#e74c3c' : '#ccc'}}
                                    disabled={seleccionados.length === 0}
                                    onClick={() => setVista('CONFIRMACION')}
                                >
                                    CANCELAR SELECCIONADAS
                                </button>
                            </div>
                        )}
                    </>
                )}

                {vista === 'CONFIRMACION' && (
                     <div style={{textAlign: 'center', padding: '20px'}}>
                        <h2>¿Está seguro que desea cancelar {seleccionados.length} reserva(s)?</h2>
                        <div style={{display: 'flex', gap: '20px', justifyContent: 'center', marginTop: '30px'}}>
                            <button className="search-button" onClick={handleCancelarReservas}>ACEPTAR</button>
                            <button className="cancel-button" onClick={() => setVista('BUSQUEDA')}>CANCELAR</button>
                        </div>
                     </div>
                )}
            </div>
        </div>
    );
}