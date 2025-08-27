# UX Dual Portal - Frontend

Frontend de la aplicación UX Dual desarrollado en **Vue 3** con **Quasar Framework**.

## Tecnologías

- **Vue 3** - Framework JavaScript reactivo
- **Quasar Framework** - Componentes UI Material Design
- **Express.js** - Servidor proxy para desarrollo
- **Chart.js** - Gráficos y visualizaciones
- **Axios** - Cliente HTTP para API

## Estructura del Proyecto

```
frontend/
├── src/
│   ├── components/        # Componentes Vue reutilizables
│   ├── pages/            # Páginas de la aplicación
│   ├── stores/           # Pinia stores (estado global)
│   ├── services/         # Servicios API
│   ├── utils/           # Utilidades y helpers
│   └── router/          # Configuración de rutas
├── professional-index.html  # Página principal
├── simple-server.js         # Servidor de desarrollo
├── package.json            # Dependencias Node.js
└── quasar.config.js       # Configuración de Quasar
```

## Cómo Ejecutar

### Prerrequisitos
- Node.js (ya instalado en Replit)

### Comando de Ejecución

```bash
cd frontend
node simple-server.js
```

El servidor se iniciará en: `http://0.0.0.0:5000`

## Características Principales

### Dashboard
- Métricas en tiempo real
- Gráficos de ventas
- Indicadores KPI
- Actualización automática cada 30 segundos

### Gestión de Pagos
- Lista de transacciones
- Estados: Aprobada, Pendiente de Liquidar, Liquidada
- Filtros y búsqueda
- Exportación de datos

### Sistema de Autenticación
- Login con JWT
- Roles de usuario (owner, manager, cashier)
- Protección de rutas

### Responsive Design
- Compatible con móviles y tablets
- Interfaz Material Design
- Tema UX Capital con colores corporativos

## Configuración del Proxy

El `simple-server.js` incluye un proxy que redirige las llamadas API al backend:

```javascript
// Todas las rutas /api/* se redirigen a localhost:8080
app.use('/api', createProxyMiddleware({
  target: 'http://localhost:8080',
  changeOrigin: true
}));
```

## Arquitectura de Componentes

### Stores (Pinia)
- `authStore` - Autenticación y usuario actual
- `dashboardStore` - Datos del dashboard
- `paymentsStore` - Gestión de pagos

### Servicios API
- `apiService.js` - Cliente HTTP base
- Manejo automático de tokens JWT
- Interceptores para errores

### Utilidades
- `auth.js` - Helpers de autenticación
- `exportUtils.js` - Exportación de datos
- `formatters.js` - Formateo de números y fechas

## Colores del Tema

```css
/* Colores principales UX Capital */
--primary: #1C304F      /* Azul principal */
--primary-green: #1BD696 /* Verde primario */
--secondary-green: #54F8A6 /* Verde secundario */
--dark-blue: #0E1B38    /* Azul oscuro */
--light-gray: #EEEEEE   /* Gris claro */
```

## Tipografía

- **Principal**: Anek Odia (títulos y encabezados)
- **Secundaria**: Montserrat (texto del cuerpo)

## Desarrollo

Para hacer cambios en el frontend:

1. Editar archivos en `src/`
2. El servidor se recarga automáticamente
3. Usar herramientas de desarrollo del navegador

## API Backend

El frontend se comunica con el backend en `http://localhost:8080`

Principales endpoints usados:
- `/api/dashboard/stats` - Estadísticas
- `/api/payments` - Gestión de pagos
- `/api/auth/login` - Autenticación

## Logs

Los logs aparecen en la consola del navegador y en la consola del servidor Node.js.