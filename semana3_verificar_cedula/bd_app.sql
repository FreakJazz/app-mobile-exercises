-- Base completa para correr el ejercicio desde cero (tabla usuarios del PDF + cedula)
CREATE DATABASE IF NOT EXISTS bd_app CHARACTER SET utf8mb4;
USE bd_app;

CREATE TABLE IF NOT EXISTS usuarios (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    edad   INT,
    activo TINYINT DEFAULT 1,
    cedula VARCHAR(10) UNIQUE
);

INSERT IGNORE INTO usuarios (nombre, correo, edad, cedula) VALUES
 ('Ana García',  'ana@mail.com',  23, '1234567890'),
 ('Luis Torres', 'luis@mail.com', 30, '1710034065');
