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

### Prerrequisitos
- Java 17 (ya instalado en Replit)
- PostgreSQL (ya configurado en Replit)

### Comando de Ejecución

```bash
cd backend
mvn compile exec:java
```

El servidor se iniciará en: `http://0.0.0.0:8080`

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