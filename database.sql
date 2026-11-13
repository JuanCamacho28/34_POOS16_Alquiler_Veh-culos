CREATE DATABASE IF NOT EXISTS alquiler_vehiculos_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE alquiler_vehiculos_db;

CREATE TABLE IF NOT EXISTS vehiculos (
    id_vehiculo INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(15) NOT NULL UNIQUE,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    `año` INT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    precio_diario DECIMAL(10, 2) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'Disponible'
);

SELECT * FROM vehiculos;
