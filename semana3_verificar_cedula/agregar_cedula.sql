-- Ejecutar en phpMyAdmin sobre la base bd_app (tabla usuarios del PDF)
USE bd_app;

ALTER TABLE usuarios ADD COLUMN cedula VARCHAR(10) UNIQUE;

-- Datos de prueba
UPDATE usuarios SET cedula = '1234567890' WHERE id = 1;
-- para insertar
-- INSERT INTO usuarios (nombre, correo, edad, cedula)
-- VALUES ('Ana García', 'ana@mail.com', 23, '1234567890');
