-- =========================================
-- Datos de ejemplo para el nuevo esquema UXDUAL
-- =========================================

-- Insertar estados
INSERT INTO uxdual.Estado (estadoId, nombre, descripcion) VALUES 
(1, 'ACTIVO', 'Estado activo'),
(2, 'INACTIVO', 'Estado inactivo'),
(3, 'PENDIENTE', 'Estado pendiente')
ON CONFLICT (estadoId) DO NOTHING;

-- Insertar estados de transacción
INSERT INTO uxdual.EstadoTransaccion (estadoTrxId, nombre, descripcion) VALUES 
(1, 'APROBADA', 'Transacción aprobada'),
(2, 'RECHAZADA', 'Transacción rechazada'),
(3, 'PENDIENTE', 'Transacción pendiente de liquidación'),
(4, 'LIQUIDADA', 'Transacción liquidada')
ON CONFLICT (estadoTrxId) DO NOTHING;

-- Insertar direcciones
INSERT INTO uxdual.Direccion (direccionId, calle, numero, barrio, fechaCreacion) VALUES 
(1, 'Av. Corrientes', 1234, 'Centro', CURRENT_DATE),
(2, 'Av. Cabildo', 2567, 'Belgrano', CURRENT_DATE),
(3, 'Av. Santa Fe', 3890, 'Barrio Norte', CURRENT_DATE)
ON CONFLICT (direccionId) DO NOTHING;

-- Insertar comercio
INSERT INTO uxdual.Comercio (comercioId, nombre, cuit, estadoId, fechaCreacion) VALUES 
(1, 'UX Dual Demo', '30-12345678-9', 1, CURRENT_DATE)
ON CONFLICT (comercioId) DO NOTHING;

-- Insertar sucursales
INSERT INTO uxdual.Sucursal (sucursalId, comercioId, direccionId, nombre, descripcion) VALUES 
(1, 1, 1, 'Sucursal Centro', 'Sucursal principal en el centro de la ciudad'),
(2, 1, 2, 'Sucursal Norte', 'Sucursal en zona norte'),
(3, 1, 3, 'Sucursal Barrio Norte', 'Sucursal en Barrio Norte')
ON CONFLICT (sucursalId) DO NOTHING;

-- Insertar cuentas bancarias
INSERT INTO uxdual.CuentaBancaria (cuentaId, sucursalId, alias, cbu, banco, estadoId) VALUES 
(1, 1, 'CENTRO.PRINCIPAL', '0110599520000001234567', 'Banco Nación', 1),
(2, 2, 'NORTE.SECUNDARIA', '0110599520000007654321', 'Banco Nación', 1),
(3, 3, 'BNORTE.TERCIARIA', '0170299520000009876543', 'Banco BBVA', 1)
ON CONFLICT (cuentaId) DO NOTHING;

-- Insertar puntos de venta
INSERT INTO uxdual.PuntoVta (puntoVtaId, sucursalId, estadoId, descripcion) VALUES 
(1, 1, 1, 'Punto de venta Centro - Caja 1'),
(2, 1, 1, 'Punto de venta Centro - Caja 2'),
(3, 2, 1, 'Punto de venta Norte - Caja 1'),
(4, 3, 1, 'Punto de venta Barrio Norte - Caja 1')
ON CONFLICT (puntoVtaId) DO NOTHING;

-- Insertar roles
INSERT INTO uxdual.Rol (rolId, nombre, descripcion) VALUES 
(1, 'OWNER', 'Propietario con acceso completo'),
(2, 'MANAGER', 'Gerente con acceso de gestión'),
(3, 'CASHIER', 'Cajero con acceso limitado')
ON CONFLICT (rolId) DO NOTHING;

-- Insertar usuarios
INSERT INTO uxdual.Usuario (empleadoId, sucursalId, nombre, apellido, email, telefono, estadoId, fechaCreacion, dni, pinHash) VALUES 
(1, 1, 'Administrador', 'Sistema', 'admin@uxdual.com', '+54 11 4555-0100', 1, CURRENT_DATE, '12345678', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(2, 1, 'Juan', 'Pérez', 'juan.perez@uxdual.com', '+54 11 4555-0101', 1, CURRENT_DATE, '23456789', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'),
(3, 2, 'María', 'García', 'maria.garcia@uxdual.com', '+54 11 4555-0102', 1, CURRENT_DATE, '34567890', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi')
ON CONFLICT (empleadoId) DO NOTHING;

-- Asignar roles a usuarios
INSERT INTO uxdual.UserRol (empRolId, empleadoId, rolId) VALUES 
(1, 1, 1), -- Admin es Owner
(2, 2, 3), -- Juan es Cashier
(3, 3, 3)  -- María es Cashier
ON CONFLICT (empRolId) DO NOTHING;

-- Insertar QRs
INSERT INTO uxdual.Qr (qrId, estadoId, puntoVtaId, fechaCreacion, tokenHash) VALUES 
(1, 1, 1, CURRENT_DATE, 'hash_qr_centro_caja1'),
(2, 1, 2, CURRENT_DATE, 'hash_qr_centro_caja2'),
(3, 1, 3, CURRENT_DATE, 'hash_qr_norte_caja1'),
(4, 1, 4, CURRENT_DATE, 'hash_qr_bnorte_caja1')
ON CONFLICT (qrId) DO NOTHING;

-- Insertar transacciones de ejemplo (datos de ayer)
INSERT INTO uxdual.Trx (
    trxId, qrId, estadoTrxId, monto, metodo, moneda, fechaCreacion, fechaAprobacion,
    paymentId, orderId, customerName, branchName, cashierName,
    createdAt, updatedAt, lastSyncCursor
) VALUES 
(1, 1, 1, 45000.00, 'SET_AMOUNT', 'ARS', '2025-08-28', '2025-08-28',
 'PAY201', 'ORD201', 'Juan Pérez', 'Sucursal Centro', 'Caja 1 - Centro',
 '2025-08-28 10:30:00', '2025-08-28 10:30:00', '2025-08-28 10:30:00'),

(2, 2, 3, 28750.00, 'SET_AMOUNT', 'ARS', '2025-08-28', NULL,
 'PAY202', 'ORD202', 'María García', 'Sucursal Norte', 'Caja 2 - Centro',
 '2025-08-28 11:15:00', '2025-08-28 11:15:00', '2025-08-28 11:15:00'),

(3, 1, 1, 67500.00, 'SET_AMOUNT', 'ARS', '2025-08-28', '2025-08-28',
 'PAY203', 'ORD203', 'Juan Pérez', 'Sucursal Centro', 'Caja 1 - Centro',
 '2025-08-28 14:20:00', '2025-08-28 14:20:00', '2025-08-28 14:20:00'),

(4, 3, 4, 35000.00, 'SET_AMOUNT', 'ARS', '2025-08-28', '2025-08-28',
 'PAY008', 'ORD008', 'Carlos López', 'Sucursal Norte', 'Caja 1 - Norte',
 '2025-08-28 09:00:00', '2025-08-28 09:00:00', '2025-08-28 09:00:00')

ON CONFLICT (trxId) DO NOTHING;

-- Insertar transacciones de ejemplo para hoy (29 de agosto)
INSERT INTO uxdual.Trx (
    qrId, estadoTrxId, monto, metodo, moneda, fechaCreacion, fechaAprobacion,
    paymentId, orderId, customerName, branchName, cashierName,
    createdAt, updatedAt, lastSyncCursor
) VALUES 
(1, 1, 125000.00, 'SET_AMOUNT', 'ARS', CURRENT_DATE, CURRENT_DATE,
 'PAY301', 'ORD301', 'Ana Martínez', 'Sucursal Centro', 'Caja 1 - Centro',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2, 3, 89500.00, 'SET_AMOUNT', 'ARS', CURRENT_DATE, NULL,
 'PAY302', 'ORD302', 'Carlos López', 'Sucursal Norte', 'Caja 2 - Centro',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(3, 1, 156750.00, 'SET_AMOUNT', 'ARS', CURRENT_DATE, CURRENT_DATE,
 'PAY303', 'ORD303', 'Laura Rodríguez', 'Sucursal Norte', 'Caja 1 - Norte',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(4, 3, 67890.00, 'SET_AMOUNT', 'ARS', CURRENT_DATE, NULL,
 'PAY304', 'ORD304', 'Pedro Fernández', 'Sucursal Centro', 'Caja 1 - Centro',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(1, 1, 234500.00, 'SET_AMOUNT', 'ARS', CURRENT_DATE, CURRENT_DATE,
 'PAY305', 'ORD305', 'Sofía González', 'Sucursal Centro', 'Caja 2 - Centro',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)

ON CONFLICT (paymentId) DO NOTHING;

-- Verificación de datos
SELECT 'Datos insertados correctamente en el nuevo esquema UXDUAL' as status;