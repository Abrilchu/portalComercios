-- Script para verificar que la base de datos UX Dual esté configurada correctamente

-- 1. Verificar que estamos conectados a la base de datos correcta
SELECT current_database() as "Base de Datos Actual";

-- 2. Verificar las tablas creadas
SELECT table_name as "Tablas Creadas" 
FROM information_schema.tables 
WHERE table_schema = 'public' 
ORDER BY table_name;

-- 3. Verificar los tipos de datos personalizados (ENUMs)
SELECT typname as "Tipos Personalizados" 
FROM pg_type 
WHERE typtype = 'e' 
ORDER BY typname;

-- 4. Contar registros en cada tabla
SELECT 
    'users' as tabla, COUNT(*) as registros FROM users
UNION ALL
SELECT 
    'branches' as tabla, COUNT(*) as registros FROM branches
UNION ALL
SELECT 
    'cashiers' as tabla, COUNT(*) as registros FROM cashiers
UNION ALL
SELECT 
    'customers' as tabla, COUNT(*) as registros FROM customers
UNION ALL
SELECT 
    'payments' as tabla, COUNT(*) as registros FROM payments
ORDER BY tabla;

-- 5. Verificar datos de ejemplo en payments
SELECT 
    payment_id as "ID Pago",
    customer_name as "Cliente", 
    amount as "Monto",
    status as "Estado",
    created_at::date as "Fecha"
FROM payments 
ORDER BY created_at DESC 
LIMIT 5;

-- 6. Verificar estadísticas del dashboard
SELECT 
    COUNT(*) as "Total Pagos",
    COUNT(CASE WHEN status = 'APROBADA' THEN 1 END) as "Aprobados",
    COUNT(CASE WHEN status = 'PEND_LIQ' THEN 1 END) as "Pendientes",
    COUNT(CASE WHEN status = 'LIQUIDADA' THEN 1 END) as "Liquidados",
    SUM(amount) as "Monto Total"
FROM payments;

-- 7. Verificar usuario admin
SELECT 
    username as "Usuario",
    email as "Email",
    active as "Activo"
FROM users 
WHERE username = 'admin';

SELECT 'Verificación completa - Revise los resultados arriba' as "Estado";