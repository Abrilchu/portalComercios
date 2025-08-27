# 🐧 Configuración para macOS/Linux - Error "unknown user postgres"

Si recibes el error `sudo: unknown user postgres`, sigue estas instrucciones específicas:

## 🔍 Identificar tu Configuración de PostgreSQL

Primero identifica cómo fue instalado PostgreSQL en tu sistema:

```bash
# Verificar si PostgreSQL está ejecutándose
ps aux | grep postgres

# Verificar versión instalada
psql --version
```

## ⚠️ Si PostgreSQL no está ejecutándose

Si recibes el error `No such file or directory` o `Is the server running locally`, necesitas iniciar PostgreSQL:

### macOS - Iniciar PostgreSQL

**Si se instaló con Homebrew:**
```bash
# Iniciar PostgreSQL
brew services start postgresql

# Verificar que esté ejecutándose
brew services list | grep postgresql
```

**Si se instaló con el instalador oficial de PostgreSQL.org:**
```bash
# Buscar la ubicación de PostgreSQL
ls /Library/PostgreSQL/*/bin/

# Iniciar usando pg_ctl (ajusta la versión según tu instalación)
sudo -u postgres /Library/PostgreSQL/17/bin/pg_ctl -D /Library/PostgreSQL/17/data start

# O usar el comando de servicio
sudo launchctl load -w /Library/LaunchDaemons/com.edb.launchd.postgresql-17.plist
```

**Método alternativo - Iniciar manualmente:**
```bash
# Encontrar el directorio de datos
sudo find /usr/local -name "postgresql.conf" 2>/dev/null
sudo find /opt -name "postgresql.conf" 2>/dev/null

# Una vez encontrado el directorio, iniciar PostgreSQL
# Ejemplo si está en /usr/local/var/postgres
pg_ctl -D /usr/local/var/postgres start
```

## 🚀 Solución Rápida

### Opción 1: Conectar con tu Usuario Actual
```bash
# Intentar conectar directamente
psql postgres

# Si funciona, crear la base de datos:
CREATE DATABASE uxdual_portal;
CREATE USER uxdual_user WITH PASSWORD 'uxdual123';
GRANT ALL PRIVILEGES ON DATABASE uxdual_portal TO uxdual_user;
\q
```

### Opción 2: macOS con Homebrew
```bash
# PostgreSQL con Homebrew usa tu usuario actual
psql postgres

# O crear la base directamente
createdb uxdual_portal

# Luego conectar y crear el usuario
psql uxdual_portal
CREATE USER uxdual_user WITH PASSWORD 'uxdual123';
GRANT ALL PRIVILEGES ON DATABASE uxdual_portal TO uxdual_user;
\q
```

### Opción 3: Linux sin usuario postgres
```bash
# Conectar como tu usuario actual
psql -d postgres

# Ejecutar los mismos comandos SQL de arriba
```

## ⚡ Continuar con la Configuración

Una vez creada la base de datos:

1. **Configurar variables de entorno:**
```bash
export PGHOST=localhost
export PGPORT=5432
export PGDATABASE=uxdual_portal
export PGUSER=uxdual_user
export PGPASSWORD=uxdual123
```

2. **Verificar conexión:**
```bash
psql -h localhost -U uxdual_user -d uxdual_portal -c "SELECT 'Conexión exitosa' as status;"
```

3. **Crear tablas:**
```bash
psql -h localhost -U uxdual_user -d uxdual_portal -f backend/src/main/resources/db/migration/V3__create_erd_schema.sql
```

4. **Insertar datos de ejemplo:**
```bash
psql -h localhost -U uxdual_user -d uxdual_portal -f backend/src/main/resources/db/sample_data.sql
```

5. **Ejecutar el backend:**
```bash
cd backend
mvn compile exec:java
```

6. **Ejecutar el frontend:**
```bash
cd frontend
npm install
node simple-server.js
```

## 🔧 Solución de Problemas Adicionales

### Error: "database does not exist"
```bash
createdb uxdual_portal
```

### Error: "role uxdual_user does not exist"
```bash
psql postgres
CREATE USER uxdual_user WITH PASSWORD 'uxdual123';
\q
```

### Error: "permission denied"
```bash
psql postgres
GRANT ALL PRIVILEGES ON DATABASE uxdual_portal TO uxdual_user;
\q
```

### PostgreSQL no está ejecutándose
```bash
# macOS con Homebrew
brew services start postgresql

# Linux con systemctl
sudo systemctl start postgresql

# Verificar estado
brew services list | grep postgres  # macOS
systemctl status postgresql         # Linux
```

## ✅ Verificación Final

Una vez configurado todo:
- Backend: `curl http://localhost:8080/api/dashboard/stats`
- Frontend: `http://localhost:5000`
- Usuario demo: `admin` / `password`

---
*¿Sigues teniendo problemas? Revisa la [guía principal](SETUP-LOCAL.md) o la [documentación del backend](backend/README.md)*