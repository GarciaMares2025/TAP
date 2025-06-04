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
 * Pantalla que muestra todos los artistas y permite gestionar información.
 */
public class AdminArtistasScreen {

    private final Stage stage;
    private final int userId;
    private final TableView<String[]> artistTable;

    /** Constructor principal con Stage + userId */
    public AdminArtistasScreen(Stage stage, int userId) {
        this.stage = stage;
        this.userId = userId;
        this.artistTable = new TableView<>();
    }

    /** Sobrecarga: constructor que sólo recibe Stage */
    public AdminArtistasScreen(Stage stage) {
        this(stage, -1);
    }

    /** Muestra la pantalla de listado de artistas */
    public void show() {
        Label titleLabel = new Label("Listado de Artistas");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Columnas de la tabla: {idArtista, nombreArtista, nacionalidad}
        TableColumn<String[], String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));
        idCol.setPrefWidth(60);

        TableColumn<String[], String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));
        nombreCol.setPrefWidth(180);

        TableColumn<String[], String> nacionalidadCol = new TableColumn<>("Nacionalidad");
        nacionalidadCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue()[2]));
        nacionalidadCol.setPrefWidth(120);

        artistTable.getColumns().clear();
        artistTable.getColumns().addAll(idCol, nombreCol, nacionalidadCol);
        artistTable.setPrefHeight(300);

        loadArtists();

        // Label para mostrar el total de artistas
        Label totalLabel = new Label();
        totalLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        updateTotalLabel(totalLabel);

        Button backButton = new Button("← Regresar");
        backButton.setPrefSize(120, 35);
        backButton.setOnAction(e -> new AdminScreen(stage, userId).show());

        VBox root = new VBox(15,
                titleLabel,
                artistTable,
                totalLabel,
                backButton
        );
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #121212;");

        Scene scene = new Scene(root, 480, 550);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Administrar Artistas");
        stage.show();
    }

    /** Carga todos los artistas desde la base de datos */
    private void loadArtists() {
        try {
            List<String[]> lista = Database.getInstance().getAllArtists();
            ObservableList<String[]> data = FXCollections.observableArrayList(lista);
            artistTable.setItems(data);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al cargar los artistas: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    /** Actualiza el label con el total de artistas */
    private void updateTotalLabel(Label label) {
        try {
            int total = artistTable.getItems().size();
            label.setText("Total de artistas: " + total);
        } catch (Exception e) {
            label.setText("Error al obtener el total de artistas.");
        }
    }
}