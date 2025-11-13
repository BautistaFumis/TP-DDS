
'use client';

import { useState, FormEvent, ChangeEvent } from 'react';

import MenuPrincipal from './casosDeUso/MenuPrincipal'; 

const API_LOGIN_URL = 'http://localhost:8081/api/auth/login';

export default function Home() {

    const [id, setId] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);


    const handleLogin = async (e: FormEvent) => {
        e.preventDefault(); 
        setError(null);

        try {
            const response = await fetch(API_LOGIN_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id, password }),
            });

            if (response.ok) {
                setIsLoggedIn(true);
            } else {
                const errorMsg = await response.text();
                setError(errorMsg || 'Error al autenticar. Verifique sus datos.');
            }
        } catch (err) {
            setError('No se pudo conectar con el servidor. ¿Está el backend en ejecución?');
        }
    };

    if (isLoggedIn) {
        return <MenuPrincipal />;
    }

    return (
        <main>
            <div className="login-container">
                <div className="login-panel-left">
                    <h1>SISTEMA DE GESTIÓN HOTELERA</h1>
                </div>
                <div className="login-panel-right">
                    <h2>Iniciar Sesión</h2>
                    <form onSubmit={handleLogin}>
                        {}
                        <div className="form-group">
                            <label htmlFor="usuario">Usuario</label>
                            <input
                                type="text"
                                id="usuario"
                                value={id}
                                onChange={(e: ChangeEvent<HTMLInputElement>) => setId(e.target.value)}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="password">Contraseña</label>
                            <input
                                type="password"
                                id="password"
                                value={password}
                                onChange={(e: ChangeEvent<HTMLInputElement>) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                        <button type="submit" className="login-button">
                            Ingresar
                        </button>
                        {error && (
                            <div className="error-message">
                                {error}
                            </div>
                        )}
                    </form>
                </div>
            </div>
        </main>
    );
}