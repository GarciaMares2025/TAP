package org.example.parcial2.views.users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.utils.Database;

public class UserHistorialScreen {

    private final Stage stage;
    private final Database db;
    private final TableView<String[]> historialTable;
    private final TableView<String[]> detalleTable; // Tabla para detalles de compra específica
    private final int userId; // Agregar userId como atributo


    public UserHistorialScreen(Stage stage,int userId) {
        this.stage = stage;
        this.db = Database.getInstance();
        this.historialTable = new TableView<>();
        this.detalleTable = new TableView<>();
        this.userId = userId; // Asignar el userId
    }

    public void show() {
        Label label = new Label("Historial de Compras");

        // Configuración de la tabla de historial de compras
        TableColumn<String[], String> fechaCol = new TableColumn<>("Fecha de Compra");
        fechaCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));

        TableColumn<String[], String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));

        TableColumn<String[], String> tipoCol = new TableColumn<>("Tipo de Compra");
        tipoCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[2]));

        historialTable.getColumns().addAll(fechaCol, totalCol, tipoCol);
        loadHistorial();

        // Configuración de la tabla de detalles de compra
        TableColumn<String[], String> itemCol = new TableColumn<>("Canción/Álbum");
        itemCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));

        TableColumn<String[], String> precioCol = new TableColumn<>("Precio");
        precioCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));

        detalleTable.getColumns().addAll(itemCol, precioCol);

        // Cargar detalles de compra al seleccionar una entrada en el historial
        historialTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int idCompra = Integer.parseInt(newSelection[3]); // Suponiendo que el ID de compra esté en la posición 3
                loadDetalleCompra(idCompra);
            }
        });

        // Botón para generar reporte en PDF
        Button generarPDFButton = new Button("Generar Reporte en PDF");
        generarPDFButton.setOnAction(e -> generatePDFReport());

        // Botón para regresar a la pantalla anterior
        Button backButton = new Button("Regresar");
        backButton.setOnAction(e -> new UserScreen(stage,userId).show());

        // Layout
        VBox vbox = new VBox(10, label, historialTable, new Label("Detalles de la Compra Seleccionada"), detalleTable, generarPDFButton, backButton);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(vbox, 700, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Historial de Compras");
        stage.show();
    }

    private void loadHistorial() {
        ObservableList<String[]> historial = FXCollections.observableArrayList(db.getHistorialCompras(userId));
        historialTable.setItems(historial);
    }


    private void loadDetalleCompra(int idCompra) {
        ObservableList<String[]> detalle = FXCollections.observableArrayList(db.getDetalleCompra(idCompra));
        detalleTable.setItems(detalle);
    }


    private void generatePDFReport() {
        boolean success = db.generateHistorialPDF(userId);
        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                success ? "Reporte generado exitosamente." : "Error al generar el reporte.");
        alert.showAndWait();
    }
}
