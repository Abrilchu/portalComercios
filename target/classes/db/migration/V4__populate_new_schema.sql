-- V4: Populate new schema with initial data

-- Insert default empresa (UX Dual company)
INSERT INTO empresa (id, nombre, ruc, direccion, telefono, email, activo, configuracion) VALUES
(1, 'UX Dual Demo', '12345678901', 'Av. Principal 123, Buenos Aires', '+5411-4444-5555', 'admin@uxdual.com', true,
 '{"theme": "ux-capital", "currency": "ARS", "timezone": "America/Argentina/Buenos_Aires", "polling_interval": 3000}');

-- Insert recursos (system resources)
INSERT INTO recurso (nombre, descripcion, modulo, endpoint) VALUES
('dashboard', 'Dashboard principal con métricas', 'dashboard', '/dashboard'),
('payments', 'Gestión de pagos y transacciones', 'payments', '/payments'),
('customers', 'Gestión de clientes', 'customers', '/customers'),
('branches', 'Gestión de sucursales', 'branches', '/branches'),
('users', 'Gestión de usuarios', 'users', '/users'),
('reports', 'Reportes y análisis', 'reports', '/reports'),
('settings', 'Configuración del sistema', 'settings', '/settings'),
('roles', 'Gestión de roles y permisos', 'roles', '/roles');

-- Insert permisos for each resource (using correct enum values)
INSERT INTO permiso (recurso_id, tipo, nombre, descripcion) 
SELECT r.id, 'READ'::permission_type, CONCAT('read_', r.nombre), CONCAT('Leer ', r.descripcion)
FROM recurso r;

INSERT INTO permiso (recurso_id, tipo, nombre, descripcion) 
SELECT r.id, 'WRITE'::permission_type, CONCAT('write_', r.nombre), CONCAT('Escribir/Editar ', r.descripcion)
FROM recurso r;

INSERT INTO permiso (recurso_id, tipo, nombre, descripcion) 
SELECT r.id, 'DELETE'::permission_type, CONCAT('delete_', r.nombre), CONCAT('Eliminar ', r.descripcion)
FROM recurso r;

INSERT INTO permiso (recurso_id, tipo, nombre, descripcion) 
SELECT r.id, 'ADMIN'::permission_type, CONCAT('admin_', r.nombre), CONCAT('Administrar ', r.descripcion)
FROM recurso r;

-- Insert roles based on your ERD
INSERT INTO rol (nombre, descripcion, empresa_id) VALUES
('owner', 'Propietario con acceso total al sistema', 1),
('manager', 'Gerente con acceso a gestión y reportes', 1),
('cashier', 'Cajero con acceso básico a ventas', 1),
('viewer', 'Solo lectura de reportes básicos', 1);

-- Assign permissions to roles

-- Owner: All permissions
INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id 
FROM rol r, permiso p 
WHERE r.nombre = 'owner' AND r.empresa_id = 1;

-- Manager: Read/Write on most resources, admin on branches/users
INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id 
FROM rol r, permiso p, recurso res
WHERE r.nombre = 'manager' AND r.empresa_id = 1
AND p.recurso_id = res.id
AND (
    (p.tipo IN ('READ', 'WRITE') AND res.nombre IN ('dashboard', 'payments', 'customers', 'reports'))
    OR (p.tipo = 'ADMIN' AND res.nombre IN ('branches'))
    OR (p.tipo IN ('READ', 'WRITE') AND res.nombre = 'users')
);

-- Cashier: Read dashboard/payments/customers, write payments
INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id 
FROM rol r, permiso p, recurso res
WHERE r.nombre = 'cashier' AND r.empresa_id = 1
AND p.recurso_id = res.id
AND (
    (p.tipo = 'READ' AND res.nombre IN ('dashboard', 'payments', 'customers'))
    OR (p.tipo = 'WRITE' AND res.nombre = 'payments')
);

-- Viewer: Only read dashboard and basic reports
INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id 
FROM rol r, permiso p, recurso res
WHERE r.nombre = 'viewer' AND r.empresa_id = 1
AND p.recurso_id = res.id
AND p.tipo = 'READ' AND res.nombre IN ('dashboard', 'reports');

-- Update existing data to reference new empresa
UPDATE branches SET empresa_id = 1 WHERE empresa_id IS NULL;
UPDATE users SET empresa_id = 1 WHERE empresa_id IS NULL;
UPDATE customers SET empresa_id_new = 1 WHERE empresa_id_new IS NULL;

-- Update users with additional info
UPDATE users SET 
    primer_nombre = CASE username
        WHEN 'owner1' THEN 'Propietario'
        WHEN 'manager1' THEN 'Gerente'
        WHEN 'cashier1' THEN 'Cajero'
        WHEN 'cashier2' THEN 'Cajero'
    END,
    apellidos = CASE username
        WHEN 'owner1' THEN 'Principal'
        WHEN 'manager1' THEN 'Sucursal'
        WHEN 'cashier1' THEN 'Uno'
        WHEN 'cashier2' THEN 'Dos'
    END,
    telefono = CASE username
        WHEN 'owner1' THEN '+5411-1111-1111'
        WHEN 'manager1' THEN '+5411-2222-2222'
        WHEN 'cashier1' THEN '+5411-3333-3333'
        WHEN 'cashier2' THEN '+5411-4444-4444'
    END;

-- Assign roles to existing users
INSERT INTO usuario_rol (usuario_id, rol_id)
SELECT u.id, r.id
FROM users u, rol r
WHERE u.role::text = r.nombre AND r.empresa_id = 1;

-- Create sample ventas for existing payments
INSERT INTO venta (numero_venta, empresa_id, sucursal_id, caja_id, cliente_id, usuario_id, total, subtotal, fecha_venta)
SELECT 
    'V' || LPAD(p.id::text, 6, '0'),
    1,
    p.branch_id,
    p.cashier_id,
    p.customer_id,
    COALESCE(c.user_id, 3), -- Default to cashier1 if no user assigned
    p.amount,
    p.amount * 0.9, -- Simulate subtotal (90% of total, 10% tax)
    p.created_at
FROM payments p
LEFT JOIN cashiers c ON c.id = p.cashier_id;

-- Link payments to ventas
UPDATE payments SET venta_id = (
    SELECT v.id FROM venta v 
    WHERE v.numero_venta = 'V' || LPAD(payments.id::text, 6, '0')
);

-- Set sequences to continue from existing data
SELECT setval('empresa_id_seq', 1, true);
SELECT setval('recurso_id_seq', (SELECT MAX(id) FROM recurso), true);
SELECT setval('rol_id_seq', (SELECT MAX(id) FROM rol), true);
SELECT setval('permiso_id_seq', (SELECT MAX(id) FROM permiso), true);
SELECT setval('venta_id_seq', (SELECT MAX(id) FROM venta), true);