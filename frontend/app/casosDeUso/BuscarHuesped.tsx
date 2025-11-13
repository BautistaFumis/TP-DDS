'use client';
// --- ¡CAMBIO 1: IMPORTAMOS EL COMPONENTE REAL! ---
import AltaHuesped from './AltaHuesped'; 
import { useState, FormEvent, useMemo } from 'react';

// --- (FIN DE FUTUROS COMPONENTES) ---
// Dejamos el "stub" de Modificar, porque aún no lo hemos creado.
const ModificarHuesped = ({ huespedId, onCancel }: { huespedId: number, onCancel: () => void }) => (
    <div className="search-container">
        <div className="search-panel-left"><h1>MODIFICAR HUÉSPED</h1></div>
        <div className="search-panel-right">
            <h2>Modificando Huésped ID: {huespedId}</h2>
            <p>(Aquí irá el formulario del CU10)</p>
            <button onClick={onCancel} className="cancel-button" style={{ width: 200, alignSelf: 'center' }}>Volver</button>
        </div>
    </div>
);

// --- ¡CAMBIO 2: BORRAMOS EL 'const AltaHuesped = ...' FALSO DE AQUÍ! ---


// CAMBIO: La interfaz ahora coincide con el HuespedDTO y los mockups
interface Huesped {
    id: number;
    nombre: string;
    apellido: string;
    tipoDocumento: string;
    documento: string;
    email: string; // Lo mantenemos aunque no se muestre, puede ser útil
}

// Definimos las props que recibe el componente (solo 'onCancel' desde MenuPrincipal)
interface BuscarHuespedProps {
    onCancel: () => void; // Función para manejar el clic en "Cancelar" (vuelve al menú)
}

// El tipo para las vistas que este componente puede mostrar
type VistaInterna = 'buscar' | 'resultados' | 'alta' | 'modificar';

// El tipo para las opciones de ordenamiento
type OrdenType = 'apellidoAsc' | 'apellidoDesc' | 'nombreAsc' | 'nombreDesc';

// URL del endpoint (sin cambios)
const API_SEARCH_URL = 'http://localhost:8081/api/huespedes/buscar';

/**
 * Componente Modal para el flujo 4.A (No encontrado)
 */
const ModalNoEncontrado = ({ onAceptar }: { onAceptar: () => void }) => (
    <div className="modal-overlay">
        <div className="modal-content">
            <span className="modal-icon">⚠️</span>
            <h3>Sin resultados</h3>
            <p>No existe ninguna concordancia según los criterios de búsqueda.</p>
            <button onClick={onAceptar} className="modal-button-aceptar">
                Aceptar
            </button>
        </div>
    </div>
);

/**
 * Componente principal del Caso de Uso "Buscar Huésped" (CU-02)
 */
export default function BuscarHuesped({ onCancel }: BuscarHuespedProps) {

    // --- Estado de la Vista ---
    // Controla qué "pantalla" se muestra: 'buscar', 'resultados', 'alta', 'modificar'
    const [vista, setVista] = useState<VistaInterna>('buscar');

    // --- Estados del Formulario de Búsqueda ---
    const [nombre, setNombre] = useState('');
    const [apellido, setApellido] = useState('');
    const [tipoDocumento, setTipoDocumento] = useState('DNI');
    const [documento, setDocumento] = useState('');

    // --- Estados de Resultados y Modal ---
    const [resultados, setResultados] = useState<Huesped[]>([]);
    const [huespedSeleccionadoId, setHuespedSeleccionadoId] = useState<number | null>(null);
    const [orden, setOrden] = useState<OrdenType>('apellidoAsc'); // Para el sorting
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [mostrarModalNoEncontrado, setMostrarModalNoEncontrado] = useState(false); // Para el Mockup 1

    // --- Lógica de Búsqueda (CU Flujo Principal 3 & 4) ---
    const handleSearch = async (e: FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setError(null);
        setResultados([]); // Limpia resultados anteriores

        const params = new URLSearchParams();
        if (nombre) params.append('nombre', nombre);
        if (apellido) params.append('apellido', apellido);
        if (tipoDocumento) params.append('tipoDocumento', tipoDocumento);
        if (documento) params.append('documento', documento);

        try {
            const response = await fetch(`${API_SEARCH_URL}?${params.toString()}`);
            if (!response.ok) {
                const errorMsg = await response.text();
                throw new Error(errorMsg || 'Error en el servidor');
            }
            
            const data: Huesped[] = await response.json();
            
            if (data.length > 0) {
                // Éxito: Se encontraron huéspedes
                setResultados(data);
                setVista('resultados'); // CAMBIO: Muestra la pantalla de resultados
            } else {
                // Flujo 4.A: No se encontraron huéspedes
                setMostrarModalNoEncontrado(true);
            }

        } catch (err: any) {
            setError(err.message || 'No se pudo conectar con el servidor.');
        } finally {
            setIsLoading(false);
        }
    };

    // --- Lógica de la Pantalla de Resultados ---

    // Flujo 4.A.1: Al presionar "Aceptar" en el modal de no encontrado
    const handleModalAceptar = () => {
        setMostrarModalNoEncontrado(false);
        setVista('alta'); // Pasa al CU11 "Dar alta de Huésped"
    };

    // Flujo 5: Al presionar "Siguiente"
    const handleSiguiente = () => {
        if (huespedSeleccionadoId) {
            // Flujo 5: Pasa al CU10 "Modificar Huésped"
            setVista('modificar');
        } else {
            // Flujo 5.A: Pasa al CU11 "Dar alta de Huésped"
            setVista('alta');
        }
    };

    // Memoiza los resultados ordenados para no recalcular en cada render
    const resultadosOrdenados = useMemo(() => {
        return [...resultados].sort((a, b) => {
            switch (orden) {
                case 'apellidoAsc':
                    return a.apellido.localeCompare(b.apellido);
                case 'apellidoDesc':
                    return b.apellido.localeCompare(a.apellido);
                case 'nombreAsc':
                    return a.nombre.localeCompare(b.nombre);
                case 'nombreDesc':
                    return b.nombre.localeCompare(a.nombre);
                default:
                    return 0;
            }
        });
    }, [resultados, orden]);


    // --- Renderizado Condicional de Vistas ---

    // Vista 1: Formulario de Búsqueda (Default)
    if (vista === 'buscar') {
        return (
            <>
                {/* Modal (Mockup 1) - se muestra si es true */}
                {mostrarModalNoEncontrado && (
                    <ModalNoEncontrado onAceptar={handleModalAceptar} />
                )}

                <div className="search-container">
                    <div className="search-panel-left">
                        <h1>BUSCAR HUÉSPED</h1>
                        {/* El botón "Cancelar" aquí te saca del CU completo */}
                        <button onClick={onCancel} className="cancel-button">
                            Cancelar
                        </button>
                    </div>

                    {/* --- ¡CAMBIO 3: CLASE AÑADIDA AQUÍ! --- */}
                    <div className="search-panel-right search-panel-centered">
                        <form onSubmit={handleSearch} className="search-form">
                            <div className="form-group">
                                <label htmlFor="apellido">Apellido</label>
                                <input
                                    type="text"
                                    id="apellido"
                                    value={apellido}
                                    onChange={(e) => setApellido(e.target.value)}
                                    placeholder="Apellido"
                                    style={{ textTransform: 'uppercase' }} // REQUERIMIENTO: Mayúsculas
                                />
                            </div>
                            
                            <div className="form-group">
                                <label htmlFor="nombre">Nombre</label>
                                <input
                                    type="text"
                                    id="nombre"
                                    value={nombre}
                                    onChange={(e) => setNombre(e.target.value)}
                                    placeholder="Nombre"
                                    style={{ textTransform: 'uppercase' }} // REQUERIMIENTO: Mayúsculas
                                />
                            </div>

                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="tipoDocumento">Tipo</label>
                                    <select
                                        id="tipoDocumento"
                                        value={tipoDocumento}
                                        onChange={(e) => setTipoDocumento(e.target.value)}
                                    >
                                        <option value="DNI">DNI</option>
                                        <option value="LE">LE</option>
                                        <option value="LC">LC</option>
                                        <option value="Pasaporte">Pasaporte</option>
                                        <option value="Otro">Otro</option>
                                    </select>
                                </div>
                                <div className="form-group" style={{ flexGrow: 2, marginLeft: '10px' }}>
                                    <label htmlFor="documento">Documento</label>
                                    <input
                                        type="text"
                                        id="documento"
                                        value={documento}
                                        onChange={(e) => setDocumento(e.target.value)}
                                        placeholder="Número de Documento"
                                        style={{ textTransform: 'uppercase' }} // REQUERIMIENTO: Mayúsculas
                                    />
                                </div>
                            </div>

                            <button type="submit" className="search-button" disabled={isLoading}>
                                {isLoading ? 'Buscando...' : 'Buscar'}
                            </button>

                            {/* Mensaje de error de conexión/servidor */}
                            {error && <div className="error-message" style={{marginTop: '15px'}}>{error}</div>}
                        </form>
                    </div>
                </div>
            </>
        );
    }

    // Vista 2: Pantalla de Resultados (Mockups 2 y 3)
    if (vista === 'resultados') {
        return (
            <div className="results-container">
                <div className="results-header">
                    <h2>Resultados</h2>
                </div>

                <div className="results-controls">
                    <label htmlFor="orden">Ordenar por:</label>
                    <select 
                        id="orden" 
                        value={orden} 
                        onChange={(e) => setOrden(e.target.value as OrdenType)}
                    >
                        <option value="apellidoAsc">Apellido (A-Z)</option>
                        <option value="apellidoDesc">Apellido (Z-A)</option>
                        <option value="nombreAsc">Nombre (A-Z)</option>
                        <option value="nombreDesc">Nombre (Z-A)</option>
                    </select>
                </div>

                <div className="results-table-container">
                    <table className="results-table">
                        <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Apellido</th>
                                <th>Tipo</th>
                                <th>Documento</th>
                            </tr>
                        </thead>
                        <tbody>
                            {resultadosOrdenados.map((huesped) => (
                                <tr 
                                    key={huesped.id}
                                    onClick={() => setHuespedSeleccionadoId(huesped.id)}
                                    className={huesped.id === huespedSeleccionadoId ? 'fila-seleccionada' : ''}
                                >
                                    <td>{huesped.nombre}</td>
                                    <td>{huesped.apellido}</td>
                                    <td>{huesped.tipoDocumento}</td>
                                    <td>{huesped.documento}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                <div className="results-actions">
                    <button className="cancel-button" onClick={onCancel}>
                        Cancelar
                    </button>
                    <button className="search-button" onClick={handleSiguiente}>
                        Siguiente
                    </button>
                </div>
            </div>
        );
    }

    // Vista 3: Alta de Huésped (CU11)
    if (vista === 'alta') {
        // --- ¡CONEXIÓN REAL! ---
        // Ahora llama al componente importado
        return <AltaHuesped onCancel={() => setVista('buscar')} />;
    }

    // Vista 4: Modificar Huésped (CU10)
    if (vista === 'modificar' && huespedSeleccionadoId) {
        // Sigue usando el "stub" falso por ahora
        return <ModificarHuesped 
                    huespedId={huespedSeleccionadoId} 
                    onCancel={() => setVista('resultados')} 
                />;
    }

    // Fallback (no debería ocurrir)
    return <button onClick={onCancel}>Volver al Menú</button>;
}