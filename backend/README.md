# UX Dual Portal - Backend

Backend de la aplicación UX Dual desarrollado en **Java 17** con **Micronaut Framework**.

## Tecnologías

- **Java 17** - Lenguaje de programación
- **Micronaut 3.10.4** - Framework web
- **PostgreSQL** - Base de datos
- **Maven** - Gestión de dependencias
- **Flyway** - Migraciones de base de datos (deshabilitado temporalmente)

## Estructura del Proyecto

```
backend/
├── src/main/java/com/uxdual/portal/
│   ├── controller/          # Controladores REST API
│   ├── service/            # Lógica de negocio
│   ├── repository/         # Acceso a datos
│   ├── dto/               # Objetos de transferencia de datos
│   └── Application.java   # Clase principal
├── src/main/resources/
│   ├── application.yml    # Configuración de la aplicación
│   └── db/migration/      # Scripts de migración (no usados actualmente)
├── pom.xml               # Dependencias Maven
└── target/               # Archivos compilados
```

## Cómo Ejecutar

### En Replit (Ya Configurado)

```bash
cd backend
mvn compile exec:java
```

### Configuración Local en tu PC

#### Paso 1: Instalar PostgreSQL

**En Windows:**
1. Descargar PostgreSQL desde: https://www.postgresql.org/download/windows/
2. Ejecutar el instalador y seguir las instrucciones
3. Durante la instalación, recordar la contraseña que estableciste para el usuario `postgres`
4. Por defecto se instala en puerto 5432

**En macOS:**
```bash
# Usando Homebrew
brew install postgresql
brew services start postgresql
```

**En Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

#### Paso 2: Crear la Base de Datos

1. **Conectar a PostgreSQL:**

**En Windows:**
```cmd
# Desde el menú de PostgreSQL o cmd
psql -U postgres
```

**En macOS (con Homebrew):**
```bash
# PostgreSQL instalado con Homebrew usa tu usuario actual
psql postgres
# O directamente crear la base de datos
createdb uxdual_portal
psql uxdual_portal
```

**En Linux (Ubuntu/Debian):**
```bash
# Si el usuario postgres existe
sudo -u postgres psql

# Si no existe el usuario postgres, usar tu usuario actual
psql -d postgres
```

**Si recibes error "unknown user postgres":**
```bash
# Usar tu usuario actual del sistema
psql -d postgres
# O directamente
psql postgres
```

2. **Crear la base de datos:**
```sql
CREATE DATABASE uxdual_portal;
CREATE USER uxdual_user WITH PASSWORD 'uxdual123';
GRANT ALL PRIVILEGES ON DATABASE uxdual_portal TO uxdual_user;
\q
```

#### Paso 3: Configurar Variables de Entorno

**En Windows (Command Prompt):**
```cmd
set PGHOST=localhost
set PGPORT=5432
set PGDATABASE=uxdual_portal
set PGUSER=uxdual_user
set PGPASSWORD=uxdual123
```

**En Windows (PowerShell):**
```powershell
$env:PGHOST="localhost"
$env:PGPORT="5432"
$env:PGDATABASE="uxdual_portal"
$env:PGUSER="uxdual_user"
$env:PGPASSWORD="uxdual123"
```

**En macOS/Linux:**
```bash
export PGHOST=localhost
export PGPORT=5432
export PGDATABASE=uxdual_portal
export PGUSER=uxdual_user
export PGPASSWORD=uxdual123
```

#### Paso 4: Crear las Tablas

1. **Crear el esquema de base de datos:**
```bash
# Conectar a la base de datos
psql -h localhost -U uxdual_user -d uxdual_portal

# En psql, ejecutar:
\i backend/src/main/resources/db/migration/V1__initial_schema.sql
```

2. **Insertar datos de ejemplo (opcional pero recomendado):**
```sql
# Mientras estés en psql, ejecutar:
\i backend/src/main/resources/db/sample_data.sql
\q
```

**Los datos de ejemplo incluyen:**
- 1 empresa (UX Dual Demo)
- 2 sucursales (Centro y Norte)
- 3 cajas
- 4 clientes
- 6 ventas con sus respectivos pagos
- 3 roles de usuario (Owner, Manager, Cashier)
- 1 usuario admin (usuario: `admin`, contraseña: `password`)

#### Paso 6: Configurar el Frontend

1. **Configurar variables de entorno para el frontend:**
```bash
# En la carpeta raíz del proyecto
cd frontend

# Crear archivo .env (opcional, usa localhost por defecto)
echo "VUE_APP_API_URL=http://localhost:8080" > .env
```

2. **Ejecutar el frontend:**
```bash
# Instalar dependencias (solo la primera vez)
npm install

# Ejecutar el servidor de desarrollo
node simple-server.js
```

El frontend estará disponible en: `http://localhost:5000`

### Solución de Problemas Comunes

**Error: "Connection refused" al conectar a PostgreSQL**
- Verificar que PostgreSQL esté ejecutándose: `pg_ctl status`
- En Windows: verificar que el servicio PostgreSQL esté iniciado

**Error: "database does not exist"**
- Asegurarse de haber creado la base de datos `uxdual_portal`
- Verificar las variables de entorno

**Error: "role does not exist"**
- Crear el usuario `uxdual_user` según las instrucciones del Paso 2

**Error de compilación de Maven**
- Verificar que Java 17 esté instalado: `java -version`
- Instalar Java 17 si es necesario

### Verificación de la Instalación

Una vez que ambos servicios estén ejecutándose:

1. **Backend funcionando:** `curl http://localhost:8080/api/dashboard/stats`
2. **Frontend funcionando:** Abrir `http://localhost:5000` en el navegador
3. **Datos cargados:** Deberías ver las estadísticas del dashboard con datos reales

#### Paso 5: Ejecutar el Backend

```bash
cd backend
mvn compile exec:java
```

El servidor se iniciará en: `http://localhost:8080`

## Endpoints Disponibles

### Dashboard
- `GET /api/dashboard/stats` - Estadísticas del dashboard

### Pagos
- `GET /api/payments` - Lista de pagos con paginación
- `GET /api/payments/{id}` - Obtener pago por ID
- `POST /api/payments/{id}/approve` - Aprobar pago
- `POST /api/payments/{id}/liquidate` - Liquidar pago

### Clientes
- `GET /api/customers` - Lista de clientes

### Autenticación
- `POST /api/auth/login` - Iniciar sesión

## Base de Datos

La aplicación se conecta automáticamente a PostgreSQL usando las variables de entorno:
- `DATABASE_URL`
- `PGHOST`, `PGPORT`, `PGUSER`, `PGPASSWORD`, `PGDATABASE`

### Estructura de Tablas Principales

- `empresa` - Información de la empresa/tenant
- `users` - Usuarios del sistema
- `branches` - Sucursales
- `cashiers` - Cajas
- `customers` - Clientes
- `payments` - Pagos/transacciones
- `venta` - Ventas/órdenes
- `ux_rol` - Roles de usuario
- `ux_usuario_rol` - Asignación de roles

## Configuración

La configuración principal está en `src/main/resources/application.yml`:

- Puerto del servidor: 8080
- Base de datos: PostgreSQL
- Autenticación: JWT
- Flyway: Deshabilitado (migraciones manuales)

## Desarrollo

Para hacer cambios en el código:

1. Editar archivos en `src/main/java/`
2. Maven recompilará automáticamente
3. Reiniciar el servidor si es necesario

## Logs

Los logs de la aplicación aparecen en la consola donde se ejecuta Maven.