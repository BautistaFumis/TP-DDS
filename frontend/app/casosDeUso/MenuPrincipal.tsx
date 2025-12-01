'use client';
import { useState } from 'react';
import BuscarHuesped from './BuscarHuesped';
import ReservarHabitacion from './ReservarHabitacion';

type Vista = 'MENU' | 'BUSCAR_HUESPED' | 'RESERVAR_HABITACION';

export default function MenuPrincipal() {
    const [vista, setVista] = useState<Vista>('MENU');

    const irAlMenu = () => setVista('MENU');

    const renderizarVista = () => {
        switch (vista) {
            case 'BUSCAR_HUESPED':
                return <BuscarHuesped onCancel={irAlMenu} />;
            case 'RESERVAR_HABITACION':
                return <ReservarHabitacion onCancel={irAlMenu} />;
            default:
                return (
                    <div className="login-container">
                        <div className="login-panel-left">
                            <h1>SISTEMA HOTELERO</h1>
                        </div>
                        <div className="login-panel-right">
                            <h2>Menú Principal</h2>
                            <div className="menu-opciones">
                                <button className="menu-opcion-button" onClick={() => setVista('BUSCAR_HUESPED')}>
                                    Buscar Huésped
                                </button>
                                <button className="menu-opcion-button" onClick={() => setVista('RESERVAR_HABITACION')}
                                        style={{ marginTop: '15px', borderColor: '#4a7c35' }}>
                                    Reservar Habitación
                                </button>
                            </div>
                        </div>
                    </div>
                );
        }
    };

    return <main>{renderizarVista()}</main>;
}