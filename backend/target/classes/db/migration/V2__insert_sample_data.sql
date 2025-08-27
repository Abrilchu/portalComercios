-- Insert sample commerce data (Commerce ID: 1)

-- Insert users with hashed passwords (password: "password123")
INSERT INTO users (username, email, password_hash, role, commerce_id) VALUES
('owner1', 'owner@uxdual.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'owner', 1),
('manager1', 'manager1@uxdual.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'manager', 1),
('cashier1', 'cashier1@uxdual.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'cashier', 1),
('cashier2', 'cashier2@uxdual.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'cashier', 1);

-- Insert branches
INSERT INTO branches (name, address, commerce_id, manager_id) VALUES
('Sucursal Centro', 'Av. Principal 123, Centro', 1, 2),
('Sucursal Norte', 'Calle Norte 456, Zona Norte', 1, 2);

-- Insert cashiers
INSERT INTO cashiers (name, code, branch_id, user_id) VALUES
('Caja 1 - Centro', 'CAJA001', 1, 3),
('Caja 2 - Centro', 'CAJA002', 1, NULL),
('Caja 1 - Norte', 'CAJA003', 2, 4);

-- Insert sample customers
INSERT INTO customers (name, phone, email, customer_id, commerce_id) VALUES
('Juan Pérez', '+54911123456', 'juan@email.com', 'CUST001', 1),
('María García', '+54911234567', 'maria@email.com', 'CUST002', 1),
('Carlos López', '+54911345678', 'carlos@email.com', 'CUST003', 1);

-- Insert sample payments with different statuses and timestamps
INSERT INTO payments (payment_id, order_id, customer_id, branch_id, cashier_id, amount, method, status, settlement_eta, status_timeline) VALUES
('PAY001', 'ORD001', 1, 1, 1, 15000.00, 'set_amount', 'APROBADA', CURRENT_TIMESTAMP + INTERVAL '2 days', 
 '{"created": "' || CURRENT_TIMESTAMP || '", "approved": "' || CURRENT_TIMESTAMP + INTERVAL '30 seconds' || '"}'),
('PAY002', 'ORD002', 2, 1, 1, 25000.00, 'open_amount', 'PEND_LIQ', CURRENT_TIMESTAMP + INTERVAL '1 day',
 '{"created": "' || CURRENT_TIMESTAMP - INTERVAL '1 hour' || '", "approved": "' || CURRENT_TIMESTAMP - INTERVAL '30 minutes' || '"}'),
('PAY003', 'ORD003', 3, 2, 3, 8500.00, 'set_amount', 'LIQUIDADA', NULL,
 '{"created": "' || CURRENT_TIMESTAMP - INTERVAL '2 days' || '", "approved": "' || CURRENT_TIMESTAMP - INTERVAL '2 days' || '", "settled": "' || CURRENT_TIMESTAMP - INTERVAL '1 day' || '"}'),
('PAY004', 'ORD004', 1, 1, 2, 12000.00, 'set_amount', 'RECHAZADA', NULL,
 '{"created": "' || CURRENT_TIMESTAMP - INTERVAL '3 hours' || '", "rejected": "' || CURRENT_TIMESTAMP - INTERVAL '2 hours' || '"}'),
('PAY005', 'ORD005', 2, 2, 3, 18500.00, 'open_amount', 'APROBADA', CURRENT_TIMESTAMP + INTERVAL '3 days',
 '{"created": "' || CURRENT_TIMESTAMP - INTERVAL '30 minutes' || '", "approved": "' || CURRENT_TIMESTAMP - INTERVAL '15 minutes' || '"}');

-- Update last_sync_cursor for real-time updates simulation
UPDATE payments SET last_sync_cursor = CURRENT_TIMESTAMP WHERE id IN (1, 5);
UPDATE payments SET last_sync_cursor = CURRENT_TIMESTAMP - INTERVAL '1 hour' WHERE id IN (2, 3, 4);
