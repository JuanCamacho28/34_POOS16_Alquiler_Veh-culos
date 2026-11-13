# Sistema de Gestión de Alquiler de Vehículos

Aplicación de escritorio desarrollada en **Java 17** con **JavaFX**, **Maven** y **MySQL** (JDBC).

## Funcionalidades Implementadas
- **CRUD Completo:** Registro, listado, actualización y eliminación de vehículos con validación de formato de placa (`XXX-XXX`).
- **Persistencia en MySQL:** Conexión mediante patrón DAO (`VehiculoDAO` y `Conexion`).
- **Reto 1 - Reportes PDF:** Exportación de vehículos registrados a PDF utilizando **JasperReports**.
- **Reto 2 - Empaquetado Ejecutable:** Generación de JAR autocontenido (`AlquilerVehiculos-1.0-SNAPSHOT.jar`) mediante `maven-shade-plugin`.

## Estructura del Proyecto
- `src/main/java/.../modelo/Vehiculo.java`: Entidad del dominio.
- `src/main/java/.../dao/VehiculoDAO.java`: Operaciones SQL CRUD.
- `src/main/java/.../util/Conexion.java`: Gestión de conexión JDBC.
- `src/main/java/.../HelloApplication.java`: Interfaz gráfica JavaFX y generación de reportes.
- `src/main/resources/reporte_vehiculos.jrxml`: Plantilla del reporte JasperReports.
- `database.sql`: Script de creación de la base de datos y tabla `vehiculos`.