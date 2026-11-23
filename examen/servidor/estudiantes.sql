-- Ejecutar en phpMyAdmin
SET NAMES utf8mb4;
CREATE DATABASE IF NOT EXISTS bd_examen CHARACTER SET utf8mb4;
USE bd_examen;

CREATE TABLE IF NOT EXISTS estudiantes (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    codigo   VARCHAR(20)  NOT NULL UNIQUE,
    nombre   VARCHAR(50)  NOT NULL,
    apellido VARCHAR(50)  NOT NULL,
    edad     INT          NOT NULL,
    carrera  VARCHAR(80)  NOT NULL
);

-- Datos de prueba (INSERT IGNORE: se puede ejecutar varias veces sin duplicar)
INSERT IGNORE INTO estudiantes (codigo, nombre, apellido, edad, carrera) VALUES
('E001', 'Ana',     'García',   21, 'Ingeniería de Sistemas'),
('E002', 'Carlos',  'Mendoza',  23, 'Medicina'),
('E003', 'Lucía',   'Pérez',    20, 'Derecho'),
('E004', 'Andrés',  'Torres',   22, 'Arquitectura'),
('E005', 'Valeria', 'Rojas',    24, 'Psicología'),
('E006', 'Mateo',   'Castillo', 19, 'Administración de Empresas');
