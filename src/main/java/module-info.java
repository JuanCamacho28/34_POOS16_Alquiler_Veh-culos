module vallegrande.edu.pe.alquilervehiculos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires jasperreports;

    opens vallegrande.edu.pe.alquilervehiculos to javafx.fxml;
    opens vallegrande.edu.pe.alquilervehiculos.modelo to javafx.base, jasperreports;
    exports vallegrande.edu.pe.alquilervehiculos;
    exports vallegrande.edu.pe.alquilervehiculos.modelo;
    exports vallegrande.edu.pe.alquilervehiculos.dao;
    exports vallegrande.edu.pe.alquilervehiculos.util;
}