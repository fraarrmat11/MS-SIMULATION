CREATE TABLE IF NOT EXISTS truck_position (
    truck_id VARCHAR(255) PRIMARY KEY,
    x_edge DOUBLE,
    y_edge DOUBLE
);

INSERT INTO truck_position (truck_id, x_edge, y_edge) VALUES (RANDOM_UUID(), 0.0, 0.0);

CREATE TABLE IF NOT EXISTS warehouse_position (
    warehouse_id UUID PRIMARY KEY,
    name VARCHAR(255),
    x_edge INT,
    y_edge INT,
    warehouse_type VARCHAR(50) -- Al ser EnumType.STRING, se guarda como texto
);

INSERT INTO warehouse_position (warehouse_id, name, x_edge, y_edge, warehouse_type)
VALUES (RANDOM_UUID(), 'Almacén Central', 100, 200, 'FACTORY');
