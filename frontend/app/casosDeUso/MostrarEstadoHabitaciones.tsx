'use client';

import { useState } from 'react';

// Tipos
interface Celda {
    idHabitacion: string;
    numero: string;
    estado: 'LIBRE' | 'OCUPADA' | 'RESERVADA' | 'MANTENIMIENTO';
    texto: string;
}

interface Fila {
    fechaStr: string;
    celdas: Celda[];
}

export default function MostrarEstadoHabitaciones({ onCancel }: { onCancel: () => void }) {
    const [fechaDesde, setFechaDesde] = useState('');
    const [fechaHasta, setFechaHasta] = useState('');
    const [grilla, setGrilla] = useState<Fila[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [cargando, setCargando] = useState(false);

    // Obtener columnas para el header (usamos la primera fila si existe)
    const habitacionesHeader = grilla.length > 0 ? grilla[0].celdas : [];

    const handleConsultar = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setCargando(true);

        if(!fechaDesde || !fechaHasta) {
            setError("Debe ingresar ambas fechas.");
            setCargando(false);
            return;
        }

        try {
            const res = await fetch(`http://localhost:8081/api/habitaciones/estado?desde=${fechaDesde}&hasta=${fechaHasta}`);
            if(!res.ok) throw new Error("Error al cargar datos");
            const data = await res.json();
            setGrilla(data);
        } catch (err: any) {
            setError(err.message);
        } finally {
            setCargando(false);
        }
    };

    // Mapeo de colores EXACTO a tu mockup
    const getEstiloCelda = (estado: string) => {
        switch(estado) {
            case 'LIBRE': return { backgroundColor: '#4a7c35', color: 'white' }; // Verde Oscuro/Disponible
            case 'OCUPADA': return { backgroundColor: '#fcd5d5', color: '#333' }; // Rosa/Ocupada
            case 'RESERVADA': return { backgroundColor: '#1a3b70', color: 'white' }; // Azul/Reservada
            case 'MANTENIMIENTO': return { backgroundColor: '#ccc', color: '#333' };
            default: return {};
        }
    };

    return (
        <div className="search-container" style={{flexDirection: 'column', width: '900px'}}>
            {/* Header Amarillo/Verde Lima del Mockup */}
            <div style={{ backgroundColor: '#d4e59f', padding: '15px', textAlign: 'center', borderBottom: '1px solid #ccc' }}>
                <h1 style={{ margin: 0, fontSize: '2rem', color: 'black' }}>Estado de Habitaciones</h1>
            </div>

            <div style={{ padding: '30px', display: 'flex', flexDirection: 'column', gap: '20px' }}>

                {/* Filtros */}
                <form onSubmit={handleConsultar} style={{ display: 'flex', gap: '20px', alignItems: 'flex-end' }}>
                    <div className="form-group">
                        <label>Desde:</label>
                        <input type="date" value={fechaDesde} onChange={e => setFechaDesde(e.target.value)} />
                    </div>
                    <div className="form-group">
                        <label>Hasta:</label>
                        <input type="date" value={fechaHasta} onChange={e => setFechaHasta(e.target.value)} />
                    </div>
                    <button type="submit" className="search-button" style={{ width: '150px', height: '42px' }} disabled={cargando}>
                        {cargando ? '...' : 'Continuar'}
                    </button>
                </form>

                {error && <div className="error-message">{error}</div>}

                {/* TABLA ESTILO MOCKUP */}
                {grilla.length > 0 && (
                    <div style={{ overflowX: 'auto', border: '1px solid #999' }}>
                        <table style={{ width: '100%', borderCollapse: 'collapse', fontFamily: 'Arial, sans-serif' }}>
                            <thead>
                            <tr>
                                {/* Celda esquina "Día \ Habitación" */}
                                <th style={{ border: '1px solid #999', padding: '10px', backgroundColor: '#e0e0e0', minWidth: '100px' }}>
                                    Día \ Habitación
                                </th>
                                {/* Columnas de Habitaciones (01, 02, 03...) */}
                                {habitacionesHeader.map(hab => (
                                    <th key={hab.idHabitacion} style={{ border: '1px solid #999', padding: '10px', backgroundColor: '#e0e0e0', minWidth: '80px', textAlign: 'center' }}>
                                        {hab.numero}
                                    </th>
                                ))}
                            </tr>
                            </thead>
                            <tbody>
                            {grilla.map((fila, idx) => (
                                <tr key={idx}>
                                    {/* Columna Fecha */}
                                    <td style={{ border: '1px solid #999', padding: '8px', backgroundColor: '#eee', fontWeight: 'bold' }}>
                                        {fila.fechaStr}
                                    </td>
                                    {/* Celdas de Estado */}
                                    {fila.celdas.map(celda => (
                                        <td
                                            key={celda.idHabitacion}
                                            style={{
                                                border: '1px solid #999',
                                                padding: '8px',
                                                textAlign: 'center',
                                                fontWeight: 'bold',
                                                fontSize: '0.9rem',
                                                ...getEstiloCelda(celda.estado) // Aplicamos color aquí
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

                <div style={{ display: 'flex', justifyContent: 'flex-start', marginTop: '20px' }}>
                    <button className="cancel-button" style={{ width: '150px', backgroundColor: '#f5b7b1' }} onClick={onCancel}>
                        Cancelar
                    </button>
                </div>
            </div>
        </div>
    );
}