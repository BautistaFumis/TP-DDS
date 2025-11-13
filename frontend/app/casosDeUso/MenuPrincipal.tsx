'use client';
import { useState } from 'react';

import BuscarHuesped from './BuscarHuesped'; 

type Vista = 'menu' | 'buscarHuesped';

export default function MenuPrincipal() {
    const [vistaActual, setVistaActual] = useState<Vista>('menu');

    const irAlMenu = () => setVistaActual('menu');

    const renderizarVista = () => {
        switch (vistaActual) {
            case 'buscarHuesped':
                return <BuscarHuesped onCancel={irAlMenu} />;
            
            case 'menu':
            default:
                return (
                    <div className="login-container"> 

                        <div className="login-panel-left">
                            <h1>SISTEMA DE GESTIÓN HOTELERA</h1>
                        </div>

                        <div className="login-panel-right">
                            <h2>Menú Principal</h2>
                            <p className="menu-descripcion">
                                Seleccione una operación para continuar.
                            </p>

                            <div className="menu-opciones">
                                <button 
                                    className="menu-opcion-button"
                                    onClick={() => setVistaActual('buscarHuesped')}
                                >
                                    Buscar Huésped
                                </button>

                            </div>
                        </div>
                    </div>
                );
        }
    };

    return (
        <main>
            {renderizarVista()}
        </main>
    );
}