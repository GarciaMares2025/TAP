package org.example.parcial2.views.admi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.parcial2.utils.Database;

public class AdminTotalCancionesVendidas {

    private final Stage stage;
    private final Database db;

    public AdminTotalCancionesVendidas(Stage stage) {
        this.stage = stage;
        this.db = Database.getInstance();
    }

    public void show() {
        // Etiqueta de título
        Label label = new Label("Total de Dinero Ganado por Ventas de Canciones");
        label.getStyleClass().add("title-label");

        // Etiqueta para el total
        Label totalLabel = new Label();
        totalLabel.getStyleClass().add("label");

        // Tabla para mostrar canciones más vendidas
        TableView<String[]> topSongsTable = new TableView<>();
        topSongsTable.getStyleClass().add("table-view");

        // Configurar columnas de la tabla
        TableColumn<String[], String> songTitleCol = new TableColumn<>("Canción");
        songTitleCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));

        TableColumn<String[], String> salesCol = new TableColumn<>("Ventas");
        salesCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));

        topSongsTable.getColumns().addAll(songTitleCol, salesCol);

        // Actualizar datos
        updateTotalLabelAndTable(totalLabel, topSongsTable);

        // Botón de regresar
        Button backButton = new Button("Regresar");
        backButton.getStyleClass().add("button");
        backButton.setOnAction(e -> new AdminScreen(stage).show());

        // Layout principal
        VBox vbox = new VBox(20, label, totalLabel, topSongsTable, backButton);
        vbox.getStyleClass().add("vbox");

        // Configurar escena
        Scene scene = new Scene(vbox, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm()); // Cargar estilos
        stage.setScene(scene);
        stage.setTitle("Total Ventas de Canciones");
        stage.show();
    }

    private void updateTotalLabelAndTable(Label label, TableView<String[]> table) {
        try {
            // Actualizar el total de ventas
            double total = db.getTotalSales();
            label.setText("Total Ganado: $" + String.format("%.2f", total));

            // Actualizar la tabla con las canciones más vendidas
            ObservableList<String[]> topSongs = FXCollections.observableArrayList(db.getTopSellingSongs());
            table.setItems(topSongs);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al obtener datos.");
            alert.showAndWait();
        }
    }
}
