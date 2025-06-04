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
 * Pantalla que muestra todas las canciones disponibles (incluso sin filtrar por álbum).
 */
public class AdminCancionesScreen {

    private final Stage stage;
    private final int userId;
    private final TableView<String[]> songTable;

    /** Constructor principal con Stage + userId */
    public AdminCancionesScreen(Stage stage, int userId) {
        this.stage = stage;
        this.userId = userId;
        this.songTable = new TableView<>();
    }

    /** Sobrecarga: constructor que sólo recibe Stage */
    public AdminCancionesScreen(Stage stage) {
        this(stage, -1);
    }

    /** Muestra la pantalla de listado de canciones */
    public void show() {
        Label titleLabel = new Label("Listado de Canciones");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Columnas de la tabla: {idCancion, titulo, duracion, nombreAlbum}
        TableColumn<String[], String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));
        idCol.setPrefWidth(60);

        TableColumn<String[], String> tituloCol = new TableColumn<>("Título");
        tituloCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));
        tituloCol.setPrefWidth(180);

        TableColumn<String[], String> duracionCol = new TableColumn<>("Duración");
        duracionCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue()[2]));
        duracionCol.setPrefWidth(100);

        TableColumn<String[], String> albumCol = new TableColumn<>("Álbum");
        albumCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().length > 3 ? cellData.getValue()[3] : "N/A"));
        albumCol.setPrefWidth(140);

        songTable.getColumns().clear();
        songTable.getColumns().addAll(idCol, tituloCol, duracionCol, albumCol);
        songTable.setPrefHeight(300);

        loadAllSongs();

        // Label para mostrar el total de canciones
        Label totalLabel = new Label();
        totalLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        updateTotalLabel(totalLabel);

        Button backButton = new Button("← Regresar");
        backButton.setPrefSize(120, 35);
        backButton.setOnAction(e -> new AdminScreen(stage, userId).show());

        VBox root = new VBox(15,
                titleLabel,
                songTable,
                totalLabel,
                backButton
        );
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #121212;");

        Scene scene = new Scene(root, 520, 580);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Administrar Canciones");
        stage.show();
    }

    /** Carga todas las canciones desde la base de datos */
    private void loadAllSongs() {
        try {
            List<String[]> lista = Database.getInstance().getAllSongs();
            ObservableList<String[]> data = FXCollections.observableArrayList(lista);
            songTable.setItems(data);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al cargar las canciones: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    /** Actualiza el label con el total de canciones */
    private void updateTotalLabel(Label label) {
        try {
            int total = songTable.getItems().size();
            label.setText("Total de canciones: " + total);
        } catch (Exception e) {
            label.setText("Error al obtener el total de canciones.");
        }
    }
}