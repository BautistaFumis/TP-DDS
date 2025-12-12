'use client';
import { useState } from 'react';
import BuscarHuesped from './BuscarHuesped';
import ReservarHabitacion from './ReservarHabitacion';
import OcuparHabitacion from './OcuparHabitacion';
import CancelarReserva from './CancelarReserva'; // Importar
import Facturar from './Facturar'; // Importar

type Vista = 'MENU' | 'BUSCAR_HUESPED' | 'RESERVAR_HABITACION' | 'OCUPAR_HABITACION' | 'CANCELAR_RESERVA' | 'FACTURAR';

export default function MenuPrincipal() {
    const [vista, setVista] = useState<Vista>('MENU');

    const irAlMenu = () => setVista('MENU');

    const renderizarVista = () => {
        switch (vista) {
            case 'BUSCAR_HUESPED': return <BuscarHuesped onCancel={irAlMenu} />;
            case 'RESERVAR_HABITACION': return <ReservarHabitacion onCancel={irAlMenu} />;
            case 'OCUPAR_HABITACION': return <OcuparHabitacion onCancel={irAlMenu} />;
            case 'CANCELAR_RESERVA': return <CancelarReserva onCancel={irAlMenu} />;
            case 'FACTURAR': return <Facturar onCancel={irAlMenu} />;
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
                                <button className="menu-opcion-button" onClick={() => setVista('RESERVAR_HABITACION')}>
                                    Reservar Habitación
                                </button>
                                <button className="menu-opcion-button" onClick={() => setVista('OCUPAR_HABITACION')}>
                                    Ocupar Habitación
                                </button>
                                <button className="menu-opcion-button" onClick={() => setVista('CANCELAR_RESERVA')}>
                                    Cancelar Reserva
                                </button>
                                <button className="menu-opcion-button" onClick={() => setVista('FACTURAR')}>
                                    Facturar / Check-out
                                </button>
                            </div>
                        </div>
                    </div>
                );
        }
    };

    return <main>{renderizarVista()}</main>;
}