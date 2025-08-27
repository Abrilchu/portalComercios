# UX Dual Portal

Portal de gestión comercial UX Dual con seguimiento de pagos QR en tiempo real, soporte multi-sucursal y sistema de roles.

## Arquitectura del Proyecto

```
/
├── backend/              # Backend Java (Micronaut)
│   ├── src/             # Código fuente Java
│   ├── pom.xml          # Dependencias Maven
│   └── README.md        # Documentación del backend
├── frontend/            # Frontend Vue (Quasar)
│   ├── src/             # Código fuente Vue/JavaScript
│   ├── package.json     # Dependencias Node.js
│   └── README.md        # Documentación del frontend
└── replit.md           # Configuración del proyecto
```

## Inicio Rápido

### 1. Backend (Puerto 8080)
```bash
cd backend
mvn compile exec:java
```

### 2. Frontend (Puerto 5000)
```bash
cd frontend
node simple-server.js
```

### 3. Acceder a la Aplicación
Abrir: `http://localhost:5000`

## Stack Tecnológico

### Backend
- **Java 17** con **Micronaut Framework**
- **PostgreSQL** para base de datos
- **Maven** para gestión de dependencias
- **JWT** para autenticación

### Frontend
- **Vue 3** con **Composition API**
- **Quasar Framework** para UI Material Design
- **Pinia** para gestión de estado
- **Chart.js** para gráficos
- **Axios** para comunicación HTTP

## Características Principales

✅ **Dashboard en Tiempo Real**
- Métricas de ventas actualizadas automáticamente
- Gráficos interactivos con Chart.js
- Indicadores KPI con comparativas

✅ **Gestión de Pagos QR**
- Estados: Aprobada, Pendiente de Liquidar, Liquidada
- Tracking en tiempo real de transacciones
- Exportación de datos en CSV/JSON

✅ **Sistema Multi-tenant**
- Soporte para múltiples empresas
- Gestión de sucursales y cajas
- Base de datos compartida con separación lógica

✅ **Control de Acceso por Roles**
- Roles: Owner, Manager, Cashier, Viewer
- Permisos granulares por recurso
- Autenticación JWT segura

✅ **Interfaz Responsiva**
- Design System UX Capital
- Tema corporativo con colores oficiales
- Compatible móvil y desktop

## Base de Datos

### Entidades Principales
- **empresa** - Configuración del tenant
- **users** - Usuarios del sistema con roles
- **branches** - Sucursales por empresa
- **cashiers** - Cajas por sucursal
- **payments** - Transacciones de pago
- **venta** - Órdenes de venta
- **customers** - Base de clientes

### Roles y Permisos
- **ux_rol** - Definición de roles
- **ux_usuario_rol** - Asignación de roles a usuarios

## Variables de Entorno

Las siguientes variables están configuradas automáticamente en Replit:

- `DATABASE_URL` - URL de conexión PostgreSQL
- `PGHOST`, `PGPORT`, `PGUSER`, `PGPASSWORD`, `PGDATABASE`
- `JWT_SECRET` - Clave secreta para tokens JWT

## Desarrollo

### Estructura de Archivos
Cada carpeta (`backend/` y `frontend/`) tiene su propio README con instrucciones específicas.

### Flujo de Desarrollo
1. Cambios en backend: Editar archivos Java y reiniciar
2. Cambios en frontend: Editar archivos Vue (auto-recarga)
3. Cambios en BD: Usar herramientas SQL directamente

### Comandos Útiles

```bash
# Verificar estado de servicios
curl http://localhost:8080/api/dashboard/stats
curl http://localhost:5000

# Logs del sistema
# Backend: logs en consola Maven
# Frontend: logs en consola Node.js y navegador

# Base de datos
# Usar herramientas de Replit Database o psql
```

## Despliegue

El proyecto está configurado para funcionar en Replit con workflows automáticos:
- Workflow `Backend`: Ejecuta automáticamente el servidor Java
- Workflow `Frontend`: Ejecuta automáticamente el servidor Node.js

## Soporte

Para problemas o preguntas:
1. Revisar logs en las consolas de cada workflow
2. Verificar conexión de base de datos
3. Consultar documentación específica en cada carpeta

---

**UX Dual Portal** - *Tus finanzas. Simples*