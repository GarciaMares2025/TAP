package org.example.parcial2.views.users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.utils.Database;

import java.util.ArrayList;
import java.util.List;

public class UserCancionScreen {

    private final Stage stage;
    private final int userId;
    private final Database db;
    private final TableView<String[]> songTable;
    private final List<String[]> carrito;

    public UserCancionScreen(Stage stage, int userId) {
        this.stage = stage;
        this.userId = userId; // Asignar el userId
        this.db = Database.getInstance();
        this.songTable = new TableView<>();
        this.carrito = new ArrayList<>();
    }

    public void show() {
        Label label = new Label("Lista de Canciones Disponibles (Precio por canción: $10)");

        // Configuración de la tabla de canciones
        TableColumn<String[], String> idCol = new TableColumn<>("ID Canción");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));

        TableColumn<String[], String> tituloCol = new TableColumn<>("Título");
        tituloCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));

        TableColumn<String[], String> duracionCol = new TableColumn<>("Duración");
        duracionCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[2]));

        TableColumn<String[], String> albumCol = new TableColumn<>("Álbum");
        albumCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[3]));

        TableColumn<String[], String> artistaCol = new TableColumn<>("Artista");
        artistaCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[4]));

        songTable.getColumns().addAll(idCol, tituloCol, duracionCol, albumCol, artistaCol);
        loadAvailableSongs();

        // Botones para añadir al carrito y finalizar compra
        Button addToCartButton = new Button("Agregar al Carrito");
        addToCartButton.setOnAction(e -> addToCart());

        Button checkoutButton = new Button("Finalizar Compra");
        checkoutButton.setOnAction(e -> finalizePurchase());

        Button backButton = new Button("Regresar");
        backButton.setOnAction(e -> new UserScreen(stage, userId).show()); // Regresar a UserScreen

        // Layout
        VBox vbox = new VBox(10, label, songTable, addToCartButton, checkoutButton, backButton);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(vbox, 700, 500);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Compra de Canciones");
        stage.show();
    }

    private void loadAvailableSongs() {
        ObservableList<String[]> songs = FXCollections.observableArrayList(db.getAvailableSongsWithAlbumAndArtist());
        songTable.setItems(songs);
    }

    private void addToCart() {
        String[] selectedSong = songTable.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            carrito.add(selectedSong);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Canción añadida al carrito.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Seleccione una canción para agregar.");
            alert.showAndWait();
        }
    }

    private void finalizePurchase() {
        if (carrito.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "El carrito está vacío. Añada canciones antes de finalizar la compra.");
            alert.showAndWait();
        } else {
            boolean success = db.registerPurchase(carrito, userId); // Pasar el userId
            Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                    success ? "Compra finalizada con éxito, consulte la sección de historial de compras." : "Error al realizar la compra.");
            alert.showAndWait();
            carrito.clear();
        }
    }

}

