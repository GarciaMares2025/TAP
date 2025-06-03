package org.example.parcial2.views.admi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.utils.Database;

public class AdminGenerosScreen {

    private final Stage stage;
    private final Database db;
    private final TableView<String[]> genreTable;

    public AdminGenerosScreen(Stage stage) {
        this.stage = stage;
        this.db = Database.getInstance();
        this.genreTable = new TableView<>();
    }

    public void show() {
        Label label = new Label("Administración de Géneros");

        // Configurar la tabla de géneros
        TableColumn<String[], Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(Integer.parseInt(cellData.getValue()[0])));

        TableColumn<String[], String> nombreCol = new TableColumn<>("Nombre del Género");
        nombreCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));

        genreTable.getColumns().addAll(idCol, nombreCol);
        loadGenres();

        // Cuadro de texto y botón para añadir un nuevo género
        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre del nuevo género");

        Button addButton = new Button("Añadir género");
        addButton.setOnAction(e -> {
            if (db.addGenre(nombreField.getText())) {
                loadGenres();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Género añadido exitosamente.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo añadir el género.");
                alert.showAndWait();
            }
            nombreField.clear();
        });

        // Cuadro de texto y botón para eliminar un género por ID
        TextField idField = new TextField();
        idField.setPromptText("ID del género a eliminar");

        Button deleteButton = new Button("Eliminar género");
        deleteButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());

                // Confirmación de eliminación
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        "¿Está seguro de que desea eliminar el género con ID " + id + "?",
                        ButtonType.YES, ButtonType.NO);
                confirmAlert.setTitle("Confirmación de eliminación");
                confirmAlert.setHeaderText(null);

                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        if (db.deleteGenreById(id)) {
                            loadGenres();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Género eliminado exitosamente.");
                            alert.showAndWait();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "El género con el ID especificado no existe.");
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

        // Botón para mostrar el género más vendido
        Button topGenreButton = new Button("Género Más Vendido");
        topGenreButton.setOnAction(e -> showTopSellingGenre());

        // Botón de regresar
        Button backButton = new Button("Regresar");
        backButton.setOnAction(e -> new AdminScreen(stage).show());

        VBox vbox = new VBox(10, label, genreTable, nombreField, addButton, idField, deleteButton, topGenreButton, backButton);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(vbox, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Admin - Géneros");
        stage.show();
    }

    private void loadGenres() {
        ObservableList<String[]> genres = FXCollections.observableArrayList(db.getAllGenres());
        genreTable.setItems(genres);
    }

    private void showTopSellingGenre() {
        String[] topGenre = db.getTopSellingGenre();
        if (topGenre != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Género Más Vendido");
            alert.setHeaderText("El género más vendido por ingresos:");
            alert.setContentText("Género: " + topGenre[0] + "\nIngresos Totales: $" + topGenre[1]);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No se encontraron datos.");
            alert.showAndWait();
        }
    }
}
