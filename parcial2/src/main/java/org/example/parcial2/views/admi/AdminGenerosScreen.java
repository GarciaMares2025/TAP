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
 * Pantalla que muestra todos los géneros musicales y permite gestionarlos.
 */
public class AdminGenerosScreen {

    private final Stage stage;
    private final int userId;
    private final TableView<String[]> genreTable;

    /** Constructor principal con Stage + userId */
    public AdminGenerosScreen(Stage stage, int userId) {
        this.stage = stage;
        this.userId = userId;
        this.genreTable = new TableView<>();
    }

    /** Sobrecarga: constructor que sólo recibe Stage */
    public AdminGenerosScreen(Stage stage) {
        this(stage, -1);
    }

    /** Muestra la pantalla de listado de géneros */
    public void show() {
        Label titleLabel = new Label("Listado de Géneros");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        // Columnas: {idGenero, nombreGenero}
        TableColumn<String[], String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));
        idCol.setPrefWidth(60);

        TableColumn<String[], String> nombreCol = new TableColumn<>("Género");
        nombreCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));
        nombreCol.setPrefWidth(200);

        genreTable.getColumns().addAll(idCol, nombreCol);
        genreTable.setPrefHeight(300);

        loadGenres();

        Button backButton = new Button("← Regresar");
        backButton.setOnAction(e -> new AdminScreen(stage, userId).show());

        VBox root = new VBox(12,
                titleLabel,
                genreTable,
                backButton
        );
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #121212;");

        Scene scene = new Scene(root, 400, 500);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Administrar Géneros");
        stage.show();
    }

    /** Carga todos los géneros desde la base de datos */
    private void loadGenres() {
        List<String[]> lista = Database.getInstance().getAllGenres();
        ObservableList<String[]> data = FXCollections.observableArrayList(lista);
        genreTable.setItems(data);
    }
}
