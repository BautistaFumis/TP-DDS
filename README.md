# TRABAJO PRACTICO - DDS
Repositorio de GIT para el Trabajo Practico de las materia Desarrollo de Software y Diseño de Sistemas 2025 - UTN FRSF.

Integrantes: 

    - Fumis Bautista
    - Moreno Bautista
    - Locatelli Cristian
    - Soto Payva Juana


## Requisitos Previos

* **Java JDK 17** o superior.
* **Maven** 3.6+ (para el backend).
* **Node.js** 18+ y **npm** (para el frontend).
* **Base de Datos Relacional**: MySQL (o la configurada en `application.properties`).

## Instrucciones de Ejecución

El proyecto consta de dos módulos: API (Backend) y Cliente (Frontend).

### 1. Ejecutar el Backend (API)

1.  Navegar a la carpeta del backend:
    ```bash
    cd src
    ```
2.  Ejecutar con Maven (esto descarga dependencias e inicia el servidor):
    ```bash
    mvn spring-boot:run
    ```
    *El servidor iniciará en el puerto `8081`.*

### 2. Ejecutar el Frontend (Next.js)

1.  Navegar a la carpeta del frontend:
    ```bash
    cd frontend
    ```
2.  Instalar dependencias (solo la primera vez):
    ```bash
    npm install
    ```
3.  Iniciar el servidor de desarrollo:
    ```bash
    npm run dev
    ```
    *La aplicación estará disponible en `http://localhost:3000`.*

---

## Seed de Datos (Carga Inicial)

El sistema incluye un mecanismo de **Auto-Seeding** mediante la clase `CargadorDeDatos.java`.

**Comando para ejecutar el seed:**
No requiere un comando manual. El seed se ejecuta **automáticamente** cada vez que se levanta el backend (`mvn spring-boot:run`).

**Datos que se cargan:**
* **Usuario:** `Conserje` / `conserje123`
* **Escenarios de Prueba:**
    * Habitaciones ocupadas hoy (para probar Facturación).
    * Reservas futuras (para probar Cancelación).
    * Huéspedes con historial (para probar validaciones de Baja).

---

##  Pruebas Unitarias

Para ejecutar la suite de tests (JUnit 5 + Mockito) y verificar la cobertura:

1.  Ubíquese en la carpeta `src`.
2.  Ejecute el siguiente comando:
    ```bash
    mvn test
    ```

---

## Endpoints por Caso de Uso (CU)

A continuación, se detallan los endpoints principales mapeados a los requisitos funcionales.

### CU04: Reservar Habitación
* `POST /src/reservas/crear`: Crea una reserva validando disponibilidad de fechas.

### CU05: Mostrar Estado de Habitaciones
* `GET /src/habitaciones/estado?desde={f1}&hasta={f2}`: Devuelve la grilla de disponibilidad por rango de fechas.

### CU06: Cancelar Reserva
* `GET /src/reservas/buscar?apellido={apellido}`: Busca reservas activas.
* `POST /src/reservas/cancelar`: Cancela las reservas seleccionadas y libera las habitaciones.

### CU07: Facturar
* `GET /src/facturacion/preparar?habitacion={nro}`: Calcula montos y detecta tipo de factura (A/B).
* `POST /src/facturacion/generar`: Genera la factura, cierra la estadía y guarda el historial.

### CU11: Dar Baja de Huésped
* `DELETE /src/huespedes/{id}`: Elimina al huésped. Lanza error 409 si el huésped posee historial de estadías (validación de negocio).

### Autenticación
* `POST /src/auth/login`: Endpoint para inicio de sesión del Conserje.