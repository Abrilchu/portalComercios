-- Migración V2: Implementación del nuevo esquema basado en el ERD
-- Este esquema reemplaza completamente el esquema anterior

-- Primero, eliminar las tablas del esquema anterior si existen
DROP TABLE IF EXISTS payments CASCADE;
DROP TABLE IF EXISTS customers CASCADE;
DROP TABLE IF EXISTS cashiers CASCADE;
DROP TABLE IF EXISTS branches CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Eliminar tipos personalizados del esquema anterior
DROP TYPE IF EXISTS user_role CASCADE;
DROP TYPE IF EXISTS payment_status CASCADE;
DROP TYPE IF EXISTS payment_method CASCADE;

-- Crear extensiones necesarias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Crear tipos ENUM para el nuevo esquema
CREATE TYPE estado_pago AS ENUM ('APROBADA', 'RECHAZADA', 'PEND_LIQ', 'LIQUIDADA');
CREATE TYPE metodo_pago AS ENUM ('OPEN_AMOUNT', 'SET_AMOUNT');
CREATE TYPE estado_venta AS ENUM ('PENDIENTE', 'COMPLETADA', 'CANCELADA');

-- Tabla: empresa (entidad principal del sistema multi-tenant)
CREATE TABLE empresa (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    direccion TEXT,
    telefono VARCHAR(50),
    email VARCHAR(255),
    website VARCHAR(255),
    logo_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: sucursal (sucursales de cada empresa)
CREATE TABLE sucursal (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    direccion TEXT,
    telefono VARCHAR(50),
    email VARCHAR(255),
    horario_apertura TIME,
    horario_cierre TIME,
    activa BOOLEAN DEFAULT true,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: caja (cajas registradoras de cada sucursal)
CREATE TABLE caja (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    numero INTEGER NOT NULL,
    activa BOOLEAN DEFAULT true,
    sucursal_id BIGINT NOT NULL REFERENCES sucursal(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(sucursal_id, numero)
);

-- Tabla: customers (clientes del sistema)
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: venta (ventas realizadas)
CREATE TABLE venta (
    id BIGSERIAL PRIMARY KEY,
    numero_venta VARCHAR(100) NOT NULL,
    fecha_venta TIMESTAMP NOT NULL,
    total DECIMAL(15,2) NOT NULL,
    estado estado_venta NOT NULL DEFAULT 'PENDIENTE',
    caja_id BIGINT NOT NULL REFERENCES caja(id),
    sucursal_id BIGINT NOT NULL REFERENCES sucursal(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(numero_venta)
);

-- Tabla: payments (pagos - tabla principal del sistema)
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    payment_id VARCHAR(100) NOT NULL UNIQUE,
    order_id VARCHAR(100) NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    branch_name VARCHAR(255) NOT NULL,
    cashier_name VARCHAR(255) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    method metodo_pago NOT NULL,
    status estado_pago NOT NULL,
    settlement_eta TIMESTAMP,
    status_timeline TEXT,
    venta_id BIGINT REFERENCES venta(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_sync_cursor TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: ux_rol (roles del sistema)
CREATE TABLE ux_rol (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: users (usuarios del sistema)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    activo BOOLEAN DEFAULT true,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: ux_usuario_rol (relación usuarios-roles)
CREATE TABLE ux_usuario_rol (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    rol_id BIGINT NOT NULL REFERENCES ux_rol(id) ON DELETE CASCADE,
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(usuario_id, rol_id)
);

-- Crear índices para optimización de consultas
CREATE INDEX idx_sucursal_empresa_id ON sucursal(empresa_id);
CREATE INDEX idx_caja_sucursal_id ON caja(sucursal_id);
CREATE INDEX idx_venta_caja_id ON venta(caja_id);
CREATE INDEX idx_venta_sucursal_id ON venta(sucursal_id);
CREATE INDEX idx_venta_fecha ON venta(fecha_venta DESC);
CREATE INDEX idx_payments_payment_id ON payments(payment_id);
CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_created_at ON payments(created_at DESC);
CREATE INDEX idx_payments_venta_id ON payments(venta_id);
CREATE INDEX idx_payments_sync_cursor ON payments(last_sync_cursor);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_empresa_id ON users(empresa_id);
CREATE INDEX idx_usuario_rol_usuario_id ON ux_usuario_rol(usuario_id);
CREATE INDEX idx_usuario_rol_rol_id ON ux_usuario_rol(rol_id);

-- Crear función para actualizar timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Crear triggers para actualizar timestamps automáticamente
CREATE TRIGGER update_empresa_updated_at BEFORE UPDATE ON empresa
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_sucursal_updated_at BEFORE UPDATE ON sucursal
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_caja_updated_at BEFORE UPDATE ON caja
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_customers_updated_at BEFORE UPDATE ON customers
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_venta_updated_at BEFORE UPDATE ON venta
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_payments_updated_at BEFORE UPDATE ON payments
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_ux_rol_updated_at BEFORE UPDATE ON ux_rol
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_ux_usuario_rol_updated_at BEFORE UPDATE ON ux_usuario_rol
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();