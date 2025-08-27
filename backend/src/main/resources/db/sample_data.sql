-- Script de datos de ejemplo para UX Dual Portal
-- Ejecutar después de crear el esquema con V3__create_erd_schema.sql

-- Insertar empresa de ejemplo
INSERT INTO empresa (id, nombre, direccion, telefono, email, website, logo_url, created_at, updated_at) 
VALUES (1, 'UX Dual Demo', 'Av. Corrientes 1234, Buenos Aires', '+54 11 4555-0123', 'demo@uxdual.com', 'https://uxdual.com', null, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insertar sucursales de ejemplo
INSERT INTO sucursal (id, nombre, direccion, telefono, email, horario_apertura, horario_cierre, activa, empresa_id, created_at, updated_at) 
VALUES 
(1, 'Sucursal Centro', 'Av. Corrientes 1234, CABA', '+54 11 4555-0124', 'centro@uxdual.com', '09:00:00', '18:00:00', true, 1, NOW(), NOW()),
(2, 'Sucursal Norte', 'Av. Cabildo 2567, CABA', '+54 11 4555-0125', 'norte@uxdual.com', '08:00:00', '20:00:00', true, 1, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insertar cajas de ejemplo
INSERT INTO caja (id, nombre, numero, activa, sucursal_id, created_at, updated_at) 
VALUES 
(1, 'Caja 1 - Centro', 1, true, 1, NOW(), NOW()),
(2, 'Caja 2 - Centro', 2, true, 1, NOW(), NOW()),
(3, 'Caja 1 - Norte', 1, true, 2, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insertar clientes de ejemplo
INSERT INTO customers (id, customer_name, email, phone, address, created_at, updated_at) 
VALUES 
(1, 'Juan Pérez', 'juan.perez@email.com', '+54 11 6555-0101', 'Av. Santa Fe 1234, CABA', NOW(), NOW()),
(2, 'María García', 'maria.garcia@email.com', '+54 11 6555-0102', 'Av. Rivadavia 5678, CABA', NOW(), NOW()),
(3, 'Carlos López', 'carlos.lopez@email.com', '+54 11 6555-0103', 'Av. Las Heras 9012, CABA', NOW(), NOW()),
(4, 'Ana Martínez', 'ana.martinez@email.com', '+54 11 6555-0104', 'Av. Belgrano 3456, CABA', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insertar ventas de ejemplo
INSERT INTO venta (id, numero_venta, fecha_venta, total, estado, caja_id, sucursal_id, created_at, updated_at) 
VALUES 
(1, 'V-001', '2025-08-27 10:30:00', 45000.00, 'COMPLETADA', 1, 1, '2025-08-27 10:30:00', NOW()),
(2, 'V-002', '2025-08-27 11:15:00', 28750.00, 'COMPLETADA', 2, 1, '2025-08-27 11:15:00', NOW()),
(3, 'V-003', '2025-08-27 14:20:00', 67500.00, 'COMPLETADA', 1, 1, '2025-08-27 14:20:00', NOW()),
(4, 'V-004', '2025-08-26 09:00:00', 35000.00, 'COMPLETADA', 3, 2, '2025-08-26 09:00:00', NOW()),
(5, 'V-005', '2025-08-26 15:30:00', 42250.00, 'COMPLETADA', 1, 1, '2025-08-26 15:30:00', NOW()),
(6, 'V-006', '2025-08-26 16:45:00', 28250.00, 'COMPLETADA', 2, 1, '2025-08-26 16:45:00', NOW())
ON CONFLICT (id) DO NOTHING;

-- Insertar pagos de ejemplo (corresponden a las ventas)
INSERT INTO payments (id, payment_id, order_id, customer_name, branch_name, cashier_name, amount, method, status, created_at, updated_at, venta_id) 
VALUES 
(13, 'PAY201', 'ORD201', 'Juan Pérez', 'Sucursal Centro', 'Caja 1 - Centro', 45000.00, 'SET_AMOUNT', 'APROBADA', '2025-08-27 10:30:00', NOW(), 1),
(14, 'PAY202', 'ORD202', 'María García', 'Sucursal Norte', 'Caja 2 - Centro', 28750.00, 'SET_AMOUNT', 'PEND_LIQ', '2025-08-27 11:15:00', NOW(), 2),
(15, 'PAY203', 'ORD203', 'Juan Pérez', 'Sucursal Centro', 'Caja 1 - Centro', 67500.00, 'SET_AMOUNT', 'APROBADA', '2025-08-27 14:20:00', NOW(), 3),
(8, 'PAY008', 'ORD008', 'Carlos López', 'Sucursal Norte', 'Caja 1 - Norte', 35000.00, 'SET_AMOUNT', 'LIQUIDADA', '2025-08-26 09:00:00', NOW(), 4),
(9, 'PAY009', 'ORD009', 'Ana Martínez', 'Sucursal Centro', 'Caja 1 - Centro', 42250.00, 'SET_AMOUNT', 'LIQUIDADA', '2025-08-26 15:30:00', NOW(), 5),
(10, 'PAY010', 'ORD010', 'María García', 'Sucursal Centro', 'Caja 2 - Centro', 28250.00, 'SET_AMOUNT', 'LIQUIDADA', '2025-08-26 16:45:00', NOW(), 6)
ON CONFLICT (id) DO NOTHING;

-- Insertar roles de ejemplo
INSERT INTO ux_rol (id, nombre, descripcion, activo, created_at, updated_at) 
VALUES 
(1, 'OWNER', 'Propietario con acceso completo', true, NOW(), NOW()),
(2, 'MANAGER', 'Gerente con acceso de gestión', true, NOW(), NOW()),
(3, 'CASHIER', 'Cajero con acceso limitado', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Insertar usuario de ejemplo
INSERT INTO users (id, username, password, email, activo, empresa_id, created_at, updated_at) 
VALUES 
(1, 'admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin@uxdual.com', true, 1, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Asignar rol al usuario
INSERT INTO ux_usuario_rol (id, usuario_id, rol_id, activo, created_at, updated_at) 
VALUES 
(1, 1, 1, true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Mostrar resumen de datos insertados
SELECT 'Datos de ejemplo insertados correctamente:' as mensaje;
SELECT 'Empresas: ' || COUNT(*) as empresas FROM empresa;
SELECT 'Sucursales: ' || COUNT(*) as sucursales FROM sucursal;
SELECT 'Cajas: ' || COUNT(*) as cajas FROM caja;
SELECT 'Clientes: ' || COUNT(*) as clientes FROM customers;
SELECT 'Ventas: ' || COUNT(*) as ventas FROM venta;
SELECT 'Pagos: ' || COUNT(*) as pagos FROM payments;
SELECT 'Usuarios: ' || COUNT(*) as usuarios FROM users;
SELECT 'Roles: ' || COUNT(*) as roles FROM ux_rol;