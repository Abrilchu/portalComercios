#!/bin/bash

echo "==============================================="
echo "    UX Dual Portal - Configuración Local"
echo "==============================================="
echo

echo "[1/5] Configurando variables de entorno..."
export PGHOST=localhost
export PGPORT=5432
export PGDATABASE=uxdual_portal
export PGUSER=uxdual_user
export PGPASSWORD=uxdual123

echo "Variables de entorno configuradas:"
echo "  PGHOST=$PGHOST"
echo "  PGPORT=$PGPORT"
echo "  PGDATABASE=$PGDATABASE"
echo "  PGUSER=$PGUSER"
echo

echo "[2/5] Verificando conexión a PostgreSQL..."
if psql -h $PGHOST -U $PGUSER -d $PGDATABASE -c "SELECT 'Conexión exitosa a PostgreSQL' as status;" &>/dev/null; then
    echo "✓ Conexión a PostgreSQL exitosa"
else
    echo "ERROR: No se pudo conectar a PostgreSQL"
    echo "Verifique que:"
    echo "  1. PostgreSQL esté instalado y ejecutándose"
    echo "  2. La base de datos 'uxdual_portal' exista"
    echo "  3. El usuario 'uxdual_user' tenga permisos"
    echo
    echo "Para crear la base de datos según su sistema:"
    echo
    echo "macOS (Homebrew):"
    echo "  psql postgres"
    echo "  CREATE DATABASE uxdual_portal;"
    echo "  CREATE USER uxdual_user WITH PASSWORD 'uxdual123';"
    echo "  GRANT ALL PRIVILEGES ON DATABASE uxdual_portal TO uxdual_user;"
    echo "  \\q"
    echo
    echo "Linux (si existe usuario postgres):"
    echo "  sudo -u postgres psql"
    echo "  [mismos comandos SQL de arriba]"
    echo
    echo "Linux (si NO existe usuario postgres):"
    echo "  psql -d postgres"
    echo "  [mismos comandos SQL de arriba]"
    echo
    read -p "Presione Enter para continuar..."
    exit 1
fi

echo "[3/5] Creando esquema de base de datos..."
if psql -h $PGHOST -U $PGUSER -d $PGDATABASE -f backend/src/main/resources/db/migration/V3__create_erd_schema.sql; then
    echo "✓ Esquema de base de datos creado"
else
    echo "ERROR: No se pudo crear el esquema de base de datos"
    read -p "Presione Enter para continuar..."
    exit 1
fi

echo "[4/5] Insertando datos de ejemplo..."
if psql -h $PGHOST -U $PGUSER -d $PGDATABASE -f backend/src/main/resources/db/sample_data.sql; then
    echo "✓ Datos de ejemplo insertados"
else
    echo "ADVERTENCIA: No se pudieron insertar los datos de ejemplo"
    echo "La aplicación funcionará pero sin datos de prueba"
fi

echo "[5/5] Configuración completada exitosamente!"
echo

echo "==============================================="
echo "           PRÓXIMOS PASOS"
echo "==============================================="
echo
echo "1. BACKEND: Abrir una terminal y ejecutar:"
echo "   cd backend"
echo "   mvn compile exec:java"
echo
echo "2. FRONTEND: Abrir otra terminal y ejecutar:"
echo "   cd frontend"
echo "   npm install"
echo "   node simple-server.js"
echo
echo "3. Abrir navegador en: http://localhost:5000"
echo
echo "Usuario demo: admin / password"
echo "==============================================="

# Hacer el script ejecutable
chmod +x setup-local.sh