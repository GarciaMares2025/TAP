package org.example.parcial2.views.admi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.utils.Database;

import java.util.List;

/**
 * Pantalla que muestra el total de canciones vendidas (estadísticas de ventas).
 * Permite al administrador ver qué canciones han sido más vendidas.
 */
public class AdminTotalCancionesVendidas {

    private final Stage stage;
    private final int userId;
    private final TableView<String[]> statsTable;

    /** Constructor principal con Stage + userId */
    public AdminTotalCancionesVendidas(Stage stage, int userId) {
        this.stage = stage;
        this.userId = userId;
        this.statsTable = new TableView<>();
    }

    /** Sobrecarga: constructor que sólo recibe Stage */
    public AdminTotalCancionesVendidas(Stage stage) {
        this(stage, -1);
    }

    /** Muestra la pantalla con estadísticas de ventas */
    public void show() {
        Label titleLabel = new Label("📊 Total de Canciones Vendidas");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        // Configuración de columnas mejorada
        setupTableColumns();

        // Label para mostrar el total de canciones vendidas
        Label totalCancionesLabel = new Label();
        totalCancionesLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

        // Cargar datos desde la base de datos
        loadStats();
        updateTotalCancionesLabel(totalCancionesLabel);

        // Botón de actualizar datos
        Button refreshButton = new Button("🔄 Actualizar");
        refreshButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        refreshButton.setOnAction(e -> {
            loadStats();
            updateTotalCancionesLabel(totalCancionesLabel);
            showAlert("Datos actualizados correctamente", Alert.AlertType.INFORMATION);
        });

        // Botón para regresar
        Button backButton = new Button("← Regresar al Panel");
        backButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        backButton.setOnAction(e -> {
            try {
                new AdminScreen(stage, userId).show();
            } catch (Exception ex) {
                showAlert("Error al regresar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        // Label con información adicional
        Label infoLabel = new Label("Mostrando canciones ordenadas por número de ventas (mayor a menor)");
        infoLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 12px;");

        VBox root = new VBox(15,
                titleLabel,
                infoLabel,
                statsTable,
                totalCancionesLabel,
                refreshButton,
                backButton
        );
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #121212;");

        Scene scene = new Scene(root, 500, 600);

        // Aplicar CSS si existe
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Advertencia: No se pudo cargar el archivo CSS");
        }

        stage.setScene(scene);
        stage.setTitle("Estadísticas de Ventas - Total Canciones");
        stage.show();
    }

    /**
     * Configura las columnas de la tabla con mejor formato
     */
    private void setupTableColumns() {
        // Columna ID Canción
        TableColumn<String[], String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));
        idCol.setPrefWidth(60);
        idCol.setStyle("-fx-alignment: CENTER;");

        // Columna Título
        TableColumn<String[], String> tituloCol = new TableColumn<>("Título de la Canción");
        tituloCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));
        tituloCol.setPrefWidth(250);

        // Columna Total Vendidas
        TableColumn<String[], String> totalCol = new TableColumn<>("Unidades Vendidas");
        totalCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue()[2]));
        totalCol.setPrefWidth(130);
        totalCol.setStyle("-fx-alignment: CENTER;");

        // Agregar todas las columnas
        statsTable.getColumns().clear();
        statsTable.getColumns().addAll(idCol, tituloCol, totalCol);
        statsTable.setPrefHeight(350);

        // Estilo de la tabla
        statsTable.setStyle("-fx-background-color: #1e1e1e;");
    }

    /**
     * Carga las estadísticas de ventas desde la base de datos
     */
    private void loadStats() {
        try {
            List<String[]> lista = Database.getInstance().getTotalSongsSold();

            if (lista.isEmpty()) {
                // Si no hay datos, mostrar mensaje
                statsTable.setPlaceholder(new Label("No hay datos de ventas disponibles"));
            } else {
                ObservableList<String[]> data = FXCollections.observableArrayList(lista);
                statsTable.setItems(data);
            }

        } catch (Exception e) {
            showAlert("Error al cargar estadísticas: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Actualiza el label que muestra el total de canciones vendidas
     */
    private void updateTotalCancionesLabel(Label label) {
        try {
            int totalCanciones = Database.getInstance().getTotalCancionesVendidas();
            label.setText("Total general de canciones vendidas: " + totalCanciones);
        } catch (Exception e) {
            label.setText("Error al obtener el total de canciones vendidas.");
            e.printStackTrace(); // Para debugging
        }
    }

    /**
     * Muestra una alerta al usuario
     */
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}