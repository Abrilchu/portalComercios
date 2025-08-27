@echo off
echo ===============================================
echo    UX Dual Portal - Configuracion Local
echo ===============================================
echo.

echo [1/5] Configurando variables de entorno...
set PGHOST=localhost
set PGPORT=5432
set PGDATABASE=uxdual_portal
set PGUSER=uxdual_user
set PGPASSWORD=uxdual123

echo Variables de entorno configuradas:
echo   PGHOST=%PGHOST%
echo   PGPORT=%PGPORT%
echo   PGDATABASE=%PGDATABASE%
echo   PGUSER=%PGUSER%
echo.

echo [2/5] Verificando conexion a PostgreSQL...
psql -h %PGHOST% -U %PGUSER% -d %PGDATABASE% -c "SELECT 'Conexion exitosa a PostgreSQL' as status;" 2>nul
if %errorlevel% neq 0 (
    echo ERROR: No se pudo conectar a PostgreSQL
    echo Verifique que:
    echo   1. PostgreSQL este instalado y ejecutandose
    echo   2. La base de datos 'uxdual_portal' exista
    echo   3. El usuario 'uxdual_user' tenga permisos
    echo.
    echo Para crear la base de datos, ejecute:
    echo   psql -U postgres
    echo   CREATE DATABASE uxdual_portal;
    echo   CREATE USER uxdual_user WITH PASSWORD 'uxdual123';
    echo   GRANT ALL PRIVILEGES ON DATABASE uxdual_portal TO uxdual_user;
    echo   \q
    pause
    exit /b 1
)

echo [3/5] Creando esquema de base de datos...
psql -h %PGHOST% -U %PGUSER% -d %PGDATABASE% -f backend/src/main/resources/db/migration/V1__initial_schema.sql
if %errorlevel% neq 0 (
    echo ERROR: No se pudo crear el esquema de base de datos
    pause
    exit /b 1
)

echo [4/5] Insertando datos de ejemplo...
psql -h %PGHOST% -U %PGUSER% -d %PGDATABASE% -f backend/src/main/resources/db/sample_data.sql
if %errorlevel% neq 0 (
    echo ADVERTENCIA: No se pudieron insertar los datos de ejemplo
    echo La aplicacion funcionara pero sin datos de prueba
)

echo [5/5] Configuracion completada exitosamente!
echo.
echo ===============================================
echo           PROXIMOS PASOS
echo ===============================================
echo.
echo 1. BACKEND: Abrir una ventana de cmd y ejecutar:
echo    cd backend
echo    mvn compile exec:java -Dmicronaut.environments=local
echo.
echo 2. FRONTEND: Abrir otra ventana de cmd y ejecutar:
echo    cd frontend
echo    npm install
echo    node simple-server.js
echo.
echo 3. Abrir navegador en: http://localhost:5000
echo.
echo Usuario demo: admin / password
echo ===============================================
pause