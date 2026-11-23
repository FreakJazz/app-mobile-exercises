-- Ejecutar en phpMyAdmin
CREATE DATABASE IF NOT EXISTS bd_contactos CHARACTER SET utf8mb4;
USE bd_contactos;

CREATE TABLE IF NOT EXISTS contactos (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    nombre   VARCHAR(80)  NOT NULL,
    telefono VARCHAR(20)  NOT NULL,
    foto     VARCHAR(255) NOT NULL
);

INSERT INTO contactos (nombre, telefono, foto) VALUES
('Ana García',      '3001234567', 'https://i.pravatar.cc/150?img=1'),
('Carlos Pérez',    '3109876543', 'https://i.pravatar.cc/150?img=12'),
('María Rodríguez', '3205551122', 'https://i.pravatar.cc/150?img=5'),
('Luis Torres',     '3157774433', 'https://i.pravatar.cc/150?img=15'),
('Sofía Ramírez',   '3012223344', 'https://i.pravatar.cc/150?img=9'),
('Andrés Morales',  '3184445566', 'https://i.pravatar.cc/150?img=33');
