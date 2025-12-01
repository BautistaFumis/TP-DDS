import React from 'react';

interface Props {
    mensaje: string | React.ReactNode;
    onAceptar: () => void;
    icono?: string;
}

export default function ModalAlerta({ mensaje, onAceptar, icono = "⚠️" }: Props) {
    return (
        <div style={{
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            backgroundColor: 'rgba(0,0,0,0.5)', // Fondo semitransparente (Overlay)
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            zIndex: 1000
        }}>
            <div style={{
                backgroundColor: 'white',
                padding: '20px',
                borderRadius: '5px',
                boxShadow: '0 2px 10px rgba(0,0,0,0.3)',
                minWidth: '300px',
                textAlign: 'center',
                border: '1px solid #999'
            }}>
                <div style={{ fontSize: '2rem', marginBottom: '10px' }}>{icono}</div>
                <div style={{ margin: '20px 0', fontWeight: 'bold', color: '#333' }}>
                    {mensaje}
                </div>
                <button
                    onClick={onAceptar}
                    style={{
                        backgroundColor: '#e0e0e0',
                        border: '1px solid #999',
                        padding: '8px 25px',
                        fontWeight: 'bold',
                        cursor: 'pointer',
                        borderRadius: '3px'
                    }}
                >
                    Ok
                </button>
            </div>
        </div>
    );
}