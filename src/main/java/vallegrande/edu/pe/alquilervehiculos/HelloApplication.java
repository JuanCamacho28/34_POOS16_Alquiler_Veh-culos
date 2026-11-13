package vallegrande.edu.pe.alquilervehiculos;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import vallegrande.edu.pe.alquilervehiculos.dao.VehiculoDAO;
import vallegrande.edu.pe.alquilervehiculos.modelo.Vehiculo;
import vallegrande.edu.pe.alquilervehiculos.util.Conexion;

import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;

public class HelloApplication extends Application {
    private final VehiculoDAO dao = new VehiculoDAO();
    private final TableView<Vehiculo> tabla = new TableView<>();
    private final TextField txtPlaca = new TextField(), txtMarca = new TextField(),
            txtModelo = new TextField(), txtAnio = new TextField(),
            txtTipo = new TextField(), txtPrecio = new TextField();
    private final ComboBox<String> cboEstado = new ComboBox<>(
            FXCollections.observableArrayList("Disponible", "Alquilado", "Mantenimiento"));
    private Vehiculo seleccionado;

    @Override
    public void start(Stage stage) {
        cboEstado.setValue("Disponible");

        txtPlaca.setTextFormatter(new TextFormatter<>(change -> {
            String texto = change.getControlNewText().toUpperCase().replaceAll("[^A-Z0-9]", "");
            if (texto.length() > 6) return null;
            if (texto.length() > 3) texto = texto.substring(0, 3) + "-" + texto.substring(3);
            change.setText(texto);
            change.setRange(0, change.getControlText().length());
            change.setCaretPosition(texto.length());
            change.setAnchor(texto.length());
            return change;
        }));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(15));
        form.addRow(0, new Label("Placa:"), txtPlaca, new Label("Marca:"), txtMarca);
        form.addRow(1, new Label("Modelo:"), txtModelo, new Label("Año:"), txtAnio);
        form.addRow(2, new Label("Tipo:"), txtTipo, new Label("Precio diario:"), txtPrecio);
        form.addRow(3, new Label("Estado:"), cboEstado);

        Button btnRegistrar = new Button("Registrar");
        Button btnActualizar = new Button("Actualizar");
        Button btnEliminar = new Button("Eliminar");
        Button btnLimpiar = new Button("Limpiar");
        Button btnReporte = new Button("Reporte PDF");
        HBox botones = new HBox(10, btnRegistrar, btnActualizar, btnEliminar, btnLimpiar, btnReporte);
        botones.setPadding(new Insets(0, 15, 10, 15));

        configurarTabla();

        btnRegistrar.setOnAction(e -> registrar());
        btnActualizar.setOnAction(e -> actualizar());
        btnEliminar.setOnAction(e -> eliminar());
        btnLimpiar.setOnAction(e -> limpiar());
        btnReporte.setOnAction(e -> generarReportePDF());

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, old, v) -> {
            if (v != null) {
                seleccionado = v;
                txtPlaca.setText(v.getPlaca());
                txtMarca.setText(v.getMarca());
                txtModelo.setText(v.getModelo());
                txtAnio.setText(String.valueOf(v.getAnio()));
                txtTipo.setText(v.getTipo());
                txtPrecio.setText(String.valueOf(v.getPrecioDiario()));
                cboEstado.setValue(v.getEstado());
            }
        });

        VBox root = new VBox(10, new Label("  SISTEMA DE ALQUILER DE VEHÍCULOS"), form, botones, tabla);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        root.setPadding(new Insets(10));

        stage.setTitle("Alquiler de Vehículos");
        stage.setScene(new Scene(root, 850, 600));
        stage.show();
        cargar();
    }

    private void configurarTabla() {
        String[][] cols = {{"ID", "idVehiculo"}, {"Placa", "placa"}, {"Marca", "marca"},
                {"Modelo", "modelo"}, {"Año", "anio"}, {"Tipo", "tipo"},
                {"Precio diario", "precioDiario"}, {"Estado", "estado"}};
        for (String[] c : cols) {
            TableColumn<Vehiculo, Object> col = new TableColumn<>(c[0]);
            col.setCellValueFactory(new PropertyValueFactory<>(c[1]));
            tabla.getColumns().add(col);
        }
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void cargar() {
        try {
            tabla.setItems(FXCollections.observableArrayList(dao.listar()));
        } catch (Exception ex) {
            mensaje(Alert.AlertType.ERROR, "Error al consultar: " + ex.getMessage());
        }
    }

    private Vehiculo leerFormulario(int id) {
        if (txtPlaca.getText().isBlank() || txtMarca.getText().isBlank() || txtModelo.getText().isBlank()
                || txtAnio.getText().isBlank() || txtTipo.getText().isBlank() || txtPrecio.getText().isBlank()) {
            mensaje(Alert.AlertType.WARNING, "Complete todos los campos.");
            return null;
        }
        try {
            return new Vehiculo(id, txtPlaca.getText().trim(), txtMarca.getText().trim(),
                    txtModelo.getText().trim(), Integer.parseInt(txtAnio.getText().trim()),
                    txtTipo.getText().trim(), Double.parseDouble(txtPrecio.getText().trim()),
                    cboEstado.getValue());
        } catch (NumberFormatException ex) {
            mensaje(Alert.AlertType.WARNING, "Año y precio deben ser números.");
            return null;
        }
    }

    private void registrar() {
        Vehiculo v = leerFormulario(0);
        if (v == null) return;
        try {
            dao.insertar(v);
            cargar();
            limpiar();
            mensaje(Alert.AlertType.INFORMATION, "Vehículo registrado.");
        } catch (Exception ex) {
            mensaje(Alert.AlertType.ERROR, "Error al registrar: " + ex.getMessage());
        }
    }

    private void actualizar() {
        if (seleccionado == null) {
            mensaje(Alert.AlertType.WARNING, "Seleccione un vehículo de la tabla.");
            return;
        }
        Vehiculo v = leerFormulario(seleccionado.getIdVehiculo());
        if (v == null) return;
        try {
            dao.actualizar(v);
            cargar();
            limpiar();
            mensaje(Alert.AlertType.INFORMATION, "Vehículo actualizado.");
        } catch (Exception ex) {
            mensaje(Alert.AlertType.ERROR, "Error al actualizar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionado == null) {
            mensaje(Alert.AlertType.WARNING, "Seleccione un vehículo de la tabla.");
            return;
        }
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar el vehículo " + seleccionado.getPlaca() + "?");
        if (conf.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                dao.eliminar(seleccionado.getIdVehiculo());
                cargar();
                limpiar();
            } catch (Exception ex) {
                mensaje(Alert.AlertType.ERROR, "Error al eliminar: " + ex.getMessage());
            }
        }
    }

    private void limpiar() {
        seleccionado = null;
        txtPlaca.clear();
        txtMarca.clear();
        txtModelo.clear();
        txtAnio.clear();
        txtTipo.clear();
        txtPrecio.clear();
        cboEstado.setValue("Disponible");
        tabla.getSelectionModel().clearSelection();
    }

    private void generarReportePDF() {
        try (Connection cn = Conexion.getConnection();
             InputStream jrxml = getClass().getResourceAsStream("/reporte_vehiculos.jrxml")) {
            JasperReport reporte = JasperCompileManager.compileReport(jrxml);
            JasperPrint print = JasperFillManager.fillReport(reporte, new HashMap<>(), cn);
            File pdf = new File("ReporteVehiculos.pdf");
            JasperExportManager.exportReportToPdfFile(print, pdf.getAbsolutePath());
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdf);
            }
            mensaje(Alert.AlertType.INFORMATION, "Reporte generado en: " + pdf.getAbsolutePath());
        } catch (Exception ex) {
            mensaje(Alert.AlertType.ERROR, "Error al generar PDF: " + ex.getMessage());
        }
    }

    private void mensaje(Alert.AlertType tipo, String texto) {
        new Alert(tipo, texto).showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}