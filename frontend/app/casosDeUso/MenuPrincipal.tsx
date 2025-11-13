'use client';
import { useState } from 'react';
// Asegúrate de que esta ruta sea correcta según tu estructura
import BuscarHuesped from './BuscarHuesped'; 

// CAMBIO 1: Simplificamos los tipos de Vista
// Solo existen 'menu' y 'buscarHuesped' por ahora
type Vista = 'menu' | 'buscarHuesped';

export default function MenuPrincipal() {
    // Estado para saber qué componente mostrar
    const [vistaActual, setVistaActual] = useState<Vista>('menu');

    // Función para volver al menú
    const irAlMenu = () => setVistaActual('menu');

    // Renderizado condicional
    const renderizarVista = () => {
        switch (vistaActual) {
            case 'buscarHuesped':
                // Muestra el componente de búsqueda y le pasa la función para volver
                return <BuscarHuesped onCancel={irAlMenu} />;
            
            case 'menu':
            default:
                // CAMBIO 2: Reutilizamos el layout de dos paneles
                // Usamos las mismas clases CSS que la pantalla de Login
                return (
                    <div className="login-container"> 
                        
                        {/* Panel Izquierdo (Consistente con el Login) */}
                        <div className="login-panel-left">
                            <h1>SISTEMA DE GESTIÓN HOTELERA</h1>
                        </div>

                        {/* Panel Derecho (El menú) */}
                        <div className="login-panel-right">
                            <h2>Menú Principal</h2>
                            <p className="menu-descripcion">
                                Seleccione una operación para continuar.
                            </p>
                            
                            {/* CAMBIO 3: Contenedor de botones de casos de uso */}
                            <div className="menu-opciones">
                                <button 
                                    className="menu-opcion-button"
                                    onClick={() => setVistaActual('buscarHuesped')}
                                >
                                    Buscar Huésped
                                </button>
                                
                                {/* Como pediste, eliminamos los otros botones.
                                  Cuando quieras agregarlos, simplemente añade 
                                  más <button> aquí dentro de "menu-opciones".
                                */}
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