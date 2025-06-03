package org.example.parcial2.views.admi;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.utils.Database;

public class AdminArtistasScreen {

    private final Stage stage;
    private final Database db;
    private final TableView<String[]> artistTable;

    public AdminArtistasScreen(Stage stage) {
        this.stage = stage;
        this.db = Database.getInstance();
        this.artistTable = new TableView<>();
    }

    public void show() {
        Label label = new Label("Administración de Artistas");

        // Configurar la tabla de artistas
        TableColumn<String[], Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(Integer.parseInt(cellData.getValue()[0])));

        TableColumn<String[], String> nombreCol = new TableColumn<>("Nombre del Artista");
        nombreCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));

        TableColumn<String[], String> nacionalidadCol = new TableColumn<>("Nacionalidad");
        nacionalidadCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[2]));

        artistTable.getColumns().addAll(idCol, nombreCol, nacionalidadCol);
        loadArtists();

        // Cuadro de texto y botón para añadir un nuevo artista
        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre del nuevo artista");

        TextField nacionalidadField = new TextField();
        nacionalidadField.setPromptText("Nacionalidad del artista");

        Button addButton = new Button("Añadir artista");
        addButton.setOnAction(e -> {
            if (db.addArtist(nombreField.getText(), nacionalidadField.getText())) {
                loadArtists();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Artista añadido exitosamente.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo añadir el artista.");
                alert.showAndWait();
            }
            nombreField.clear();
            nacionalidadField.clear();
        });

        // Cuadro de texto y botón para eliminar un artista por ID
        TextField idField = new TextField();
        idField.setPromptText("ID del artista a eliminar");

        Button deleteButton = new Button("Eliminar artista");
        deleteButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());

                // Confirmación de eliminación
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        "¿Está seguro de que desea eliminar el artista con ID " + id + "?",
                        ButtonType.YES, ButtonType.NO);
                confirmAlert.setTitle("Confirmación de eliminación");
                confirmAlert.setHeaderText(null);

                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        if (db.deleteArtistById(id)) {
                            loadArtists();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Artista eliminado exitosamente.");
                            alert.showAndWait();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "El artista con el ID especificado no existe.");
                            alert.showAndWait();
                        }
                    }
                });

            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, ingrese un ID válido.");
                alert.showAndWait();
            }
            idField.clear();
        });

        Button backButton = new Button("Regresar");
        backButton.setOnAction(e -> new AdminScreen(stage).show());

        VBox vbox = new VBox(10, label, artistTable, nombreField, nacionalidadField, addButton, idField, deleteButton, backButton);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(vbox, 800, 600);
        //hoja de estilos CSS
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Admin - Artistas");
        stage.show();
    }

    private void loadArtists() {
        ObservableList<String[]> artists = FXCollections.observableArrayList(db.getAllArtistsWithDetails());
        artistTable.setItems(artists);
    }

}
