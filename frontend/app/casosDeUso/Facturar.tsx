'use client';
import { useState } from 'react';
import ModalAlerta from './ModalAlerta';

interface Huesped {
    id: number;
    nombre: string;
    apellido: string;
    documento: string;
    cuit: string;
    categoriaIVA: string;
    fechaNacimiento: string;
}

interface ItemFactura {
    descripcion: string;
    monto: number;
    esEstadia: boolean;
    seleccionado: boolean;
}

interface DatosFacturacion {
    idEstadia: number;
    huespedes: Huesped[];
    items: ItemFactura[];
}

export default function Facturar({ onCancel }: { onCancel: () => void }) {
    const [paso, setPaso] = useState<'BUSQUEDA' | 'SELECCION_PAGO' | 'TERCERO_CUIT' | 'ITEMS'>('BUSQUEDA');

    // Inputs
    const [habitacion, setHabitacion] = useState('');
    const [hora, setHora] = useState('');
    const [cuitTercero, setCuitTercero] = useState('');

    // Data
    const [datos, setDatos] = useState<DatosFacturacion | null>(null);
    const [responsable, setResponsable] = useState<Huesped | null>(null);
    const [esTercero, setEsTercero] = useState(false);

    // UI
    const [modal, setModal] = useState<{show: boolean, msg: string, onOk?:()=>void}>({show: false, msg: ''});
    const [cargando, setCargando] = useState(false);

    const esMenor = (fechaNac: string) => {
        const hoy = new Date();
        const nacimiento = new Date(fechaNac);
        let edad = hoy.getFullYear() - nacimiento.getFullYear();
        const m = hoy.getMonth() - nacimiento.getMonth();
        if (m < 0 || (m === 0 && hoy.getDate() < nacimiento.getDate())) {
            edad--;
        }
        return edad < 18;
    };

    const buscarHabitacion = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!habitacion || !hora) {
            setModal({show: true, msg: "Número de habitación faltante o incorrecto; hora faltante"});
            return;
        }

        setCargando(true);
        try {
            const res = await fetch(`http://localhost:8081/api/facturacion/preparar?habitacion=${habitacion}`);
            if (res.ok) {
                const data = await res.json();
                // Mapear items para agregar campo 'seleccionado'
                data.items = data.items.map((i: any) => ({...i, seleccionado: true}));
                setDatos(data);
                setPaso('SELECCION_PAGO');
            } else {
                const msg = await res.text();
                setModal({show: true, msg: msg || "Error al buscar habitación"});
            }
        } catch (e) {
            setModal({show: true, msg: "Error de conexión"});
        } finally {
            setCargando(false);
        }
    };

    const seleccionarResponsable = (h: Huesped) => {
        if (esMenor(h.fechaNacimiento)) {
            setModal({show: true, msg: "La persona seleccionada es menor de edad. Por favor elija otra"});
            return;
        }
        setResponsable(h);
        setEsTercero(false);
        setPaso('ITEMS');
    };

    const handleTercero = () => {
        setPaso('TERCERO_CUIT');
    };

    const confirmarTercero = () => {
        if (!cuitTercero) {
            // Flujo Alternativo 5.C: CUIT Vacío -> Alta Responsable (Simulado)
            setModal({show: true, msg: "CUIT Vacío: Se redirigiría a Alta Responsable (Simulado). Volviendo..."});
            setPaso('SELECCION_PAGO');
            return;
        }
        // Mock de búsqueda de razón social
        setModal({
            show: true,
            msg: `Razón Social: EMPRESA MOCK S.A. (CUIT: ${cuitTercero})`,
            onOk: () => {
                setModal({show:false, msg:''});
                // Seteamos un responsable dummy para avanzar
                setResponsable({ id: 0, nombre: 'EMPRESA MOCK', apellido: 'S.A.', cuit: cuitTercero, categoriaIVA: 'Responsable Inscripto', documento: '', fechaNacimiento: '1900-01-01' });
                setEsTercero(true);
                setPaso('ITEMS');
            }
        });
    };

    const toggleItem = (idx: number) => {
        if (!datos) return;
        const nuevosItems = [...datos.items];
        nuevosItems[idx].seleccionado = !nuevosItems[idx].seleccionado;
        setDatos({...datos, items: nuevosItems});
    };

    const calcularTotal = () => {
        return datos?.items.filter(i => i.seleccionado).reduce((acc, curr) => acc + curr.monto, 0) || 0;
    };

    const finalizarFacturacion = async () => {
        const seleccionados = datos?.items.filter(i => i.seleccionado);
        if (!seleccionados || seleccionados.length === 0) {
            setModal({show: true, msg: "Debe seleccionar al menos un ítem para facturar."});
            return;
        }

        const dto = {
            idEstadia: datos!.idEstadia,
            idResponsablePago: responsable!.id,
            cuitTercero: esTercero ? cuitTercero : null,
            itemsSeleccionados: seleccionados,
            montoTotal: calcularTotal(),
            tipoFactura: responsable?.categoriaIVA === 'Responsable Inscripto' ? 'A' : 'B'
        };

        try {
            const res = await fetch('http://localhost:8081/api/facturacion/generar', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(dto)
            });

            if (res.ok) {
                setModal({
                    show: true,
                    msg: "Factura Generada Correctamente. Imprimiendo...",
                    onOk: onCancel
                });
            } else {
                setModal({show: true, msg: "Error al generar factura"});
            }
        } catch(e) {
            setModal({show: true, msg: "Error de conexión"});
        }
    };

    return (
        <div className="search-container">
            {modal.show && <ModalAlerta mensaje={modal.msg} onAceptar={modal.onOk || (()=>setModal({show:false, msg:''}))} />}

            <div className="search-panel-left">
                <h1 style={{ paddingTop: '150px' }}>FACTURAR</h1>
                <button className="cancel-button" onClick={onCancel} style={{ marginTop: 'auto' }}>Cancelar</button>
            </div>

            <div className="search-panel-right">

                {paso === 'BUSQUEDA' && (
                    <form onSubmit={buscarHabitacion} className="search-form">
                        <div className="form-group">
                            <label>Número de Habitación</label>
                            <input value={habitacion} onChange={e=>setHabitacion(e.target.value)} placeholder="Ej: 101" />
                        </div>
                        <div className="form-group">
                            <label>Hora de Salida</label>
                            <input type="time" value={hora} onChange={e=>setHora(e.target.value)} />
                        </div>
                        <button type="submit" className="search-button" disabled={cargando}>
                            {cargando ? 'Buscando...' : 'BUSCAR'}
                        </button>
                    </form>
                )}

                {paso === 'SELECCION_PAGO' && datos && (
                    <div>
                        <h3>Seleccione Responsable de Pago</h3>
                        <div style={{display:'flex', flexDirection:'column', gap:'10px'}}>
                            {datos.huespedes.map(h => (
                                <button key={h.id} onClick={() => seleccionarResponsable(h)} style={{padding:'15px', textAlign:'left', cursor:'pointer', border:'1px solid #ccc', background:'#fff'}}>
                                    {h.apellido}, {h.nombre} (DNI: {h.documento})
                                </button>
                            ))}
                            <button onClick={handleTercero} style={{padding:'15px', textAlign:'left', cursor:'pointer', border:'1px solid #999', background:'#e0e0e0', fontWeight:'bold'}}>
                                FACTURAR A UN TERCERO (OTRO CUIT)
                            </button>
                        </div>
                    </div>
                )}

                {paso === 'TERCERO_CUIT' && (
                    <div className="search-form">
                        <h3>Facturar a Tercero</h3>
                        <div className="form-group">
                            <label>Ingrese CUIT</label>
                            <input value={cuitTercero} onChange={e=>setCuitTercero(e.target.value)} placeholder="XX-XXXXXXXX-X" />
                        </div>
                        <div style={{display:'flex', gap:'10px'}}>
                            <button className="search-button" onClick={confirmarTercero}>ACEPTAR</button>
                            <button className="cancel-button" onClick={()=>setPaso('SELECCION_PAGO')}>VOLVER</button>
                        </div>
                    </div>
                )}

                {paso === 'ITEMS' && datos && responsable && (
                    <div>
                        <div style={{borderBottom:'1px solid #ccc', marginBottom:'10px'}}>
                            <h3>Detalle de Facturación</h3>
                            <p><strong>Cliente:</strong> {responsable.apellido}, {responsable.nombre}</p>
                            <p><strong>Tipo Factura:</strong> {responsable.categoriaIVA === 'Responsable Inscripto' ? 'A' : 'B'}</p>
                        </div>

                        <div style={{maxHeight:'300px', overflowY:'auto'}}>
                            <table style={{width:'100%', borderCollapse:'collapse'}}>
                                <thead>
                                <tr style={{background:'#eee'}}>
                                    <th>Sel</th>
                                    <th>Descripción</th>
                                    <th>Monto</th>
                                </tr>
                                </thead>
                                <tbody>
                                {datos.items.map((item, idx) => (
                                    <tr key={idx} style={{borderBottom:'1px solid #eee'}}>
                                        <td style={{textAlign:'center'}}>
                                            <input type="checkbox" checked={item.seleccionado} onChange={()=>toggleItem(idx)} />
                                        </td>
                                        <td>{item.descripcion}</td>
                                        <td>${item.monto.toFixed(2)}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>

                        <div style={{marginTop:'20px', textAlign:'right', fontSize:'1.2rem'}}>
                            <strong>TOTAL A PAGAR: ${calcularTotal().toFixed(2)}</strong>
                        </div>

                        <button className="search-button" style={{marginTop:'20px'}} onClick={finalizarFacturacion}>
                            ACEPTAR Y GENERAR
                        </button>
                    </div>
                )}

            </div>
        </div>
    );
}