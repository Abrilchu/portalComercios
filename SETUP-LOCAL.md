# 🚀 Configuración Local - UX Dual Portal

Esta guía te ayudará a configurar el proyecto UX Dual Portal en tu PC local con PostgreSQL.

## 📋 Requisitos Previos

- **Java 17** - [Descargar aquí](https://adoptium.net/teapot/)
- **Maven** - [Descargar aquí](https://maven.apache.org/download.cgi)
- **Node.js 16+** - [Descargar aquí](https://nodejs.org/)
- **PostgreSQL** - [Descargar aquí](https://www.postgresql.org/download/)

## ⚡ Configuración Automática (Recomendado)

### Windows:
```cmd
# Ejecutar en el directorio raíz del proyecto
setup-local.bat
```

### macOS/Linux:
```bash
# Ejecutar en el directorio raíz del proyecto
chmod +x setup-local.sh
./setup-local.sh
```

Los scripts automáticos harán:
1. ✅ Configurar variables de entorno
2. ✅ Verificar conexión a PostgreSQL
3. ✅ Crear esquema de base de datos
4. ✅ Insertar datos de ejemplo
5. ✅ Mostrar próximos pasos

## 🔧 Configuración Manual

Si prefieres configurar paso a paso, sigue la [Guía Detallada del Backend](backend/README.md).

## 🎯 Próximos Pasos

Después de ejecutar el script de configuración:

### 1. Iniciar el Backend
```bash
cd backend
mvn compile exec:java
```
Estará disponible en: `http://localhost:8080`

### 2. Iniciar el Frontend
```bash
cd frontend
npm install
node simple-server.js
```
Estará disponible en: `http://localhost:5000`

### 3. Verificar la Instalación
- **Backend:** `curl http://localhost:8080/api/dashboard/stats`
- **Frontend:** Abrir `http://localhost:5000` en el navegador

## 👤 Usuario Demo

- **Usuario:** `admin`
- **Contraseña:** `password`

## 📊 Datos de Ejemplo

El sistema incluye datos de prueba:
- 1 empresa (UX Dual Demo)
- 2 sucursales (Centro y Norte)
- 3 cajas
- 4 clientes
- 6 ventas con pagos
- Usuario administrador

## 🆘 Solución de Problemas

### Error de conexión a PostgreSQL
```bash
# Verificar que PostgreSQL esté ejecutándose
# Windows: Servicios → PostgreSQL
# macOS: brew services list | grep postgresql
# Linux: systemctl status postgresql
```

### Error "database does not exist"
```sql
-- Conectar como superusuario y crear la base de datos
psql -U postgres
CREATE DATABASE uxdual_portal;
CREATE USER uxdual_user WITH PASSWORD 'uxdual123';
GRANT ALL PRIVILEGES ON DATABASE uxdual_portal TO uxdual_user;
\q
```

### Error de compilación Java
```bash
# Verificar versión de Java
java -version
# Debe ser Java 17 o superior
```

## 🏗️ Arquitectura del Proyecto

```
uxdual-portal/
├── backend/          # Java 17 + Micronaut
├── frontend/         # Vue 3 + Quasar
├── setup-local.bat   # Script Windows
├── setup-local.sh    # Script Unix/Linux/macOS
└── SETUP-LOCAL.md    # Esta guía
```

## 📞 Soporte

Si encuentras problemas durante la configuración:
1. Revisa la [documentación del backend](backend/README.md)
2. Verifica que todos los requisitos previos estén instalados
3. Asegúrate de que PostgreSQL esté ejecutándose correctamente

---
*UX Dual Portal - Tu sistema de gestión comercial completo*