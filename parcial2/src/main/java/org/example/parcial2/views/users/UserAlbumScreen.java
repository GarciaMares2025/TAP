package org.example.parcial2.views.users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.utils.Database;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class UserAlbumScreen {

    private final Stage stage;
    private final Database db;
    private final TableView<String[]> albumTable;
    private final TableView<String[]> songTable;
    private final List<String[]> carrito;
    private final ImageView albumCoverView;
    private final int userId; // Agregar userId como atributo


    public UserAlbumScreen(Stage stage,int userId) {
        this.stage = stage;
        this.db = Database.getInstance();
        this.albumTable = new TableView<>();
        this.songTable = new TableView<>();
        this.carrito = new ArrayList<>();
        this.albumCoverView = new ImageView();
        this.userId = userId; // Asignar el userId
    }

    public void show() {
        Label label = new Label("Lista de Álbumes Disponibles (Precio por álbum: $50)");

        // Configuración de tabla de álbumes
        TableColumn<String[], String> idCol = new TableColumn<>("ID Álbum");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));

        TableColumn<String[], String> nombreCol = new TableColumn<>("Nombre del Álbum");
        nombreCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));

        TableColumn<String[], String> fechaCol = new TableColumn<>("Fecha de Lanzamiento");
        fechaCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[2]));

        albumTable.getColumns().addAll(idCol, nombreCol, fechaCol);
        loadAvailableAlbums();

        // Configuración de tabla de canciones por álbum
        TableColumn<String[], String> songIdCol = new TableColumn<>("ID Canción");
        songIdCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));

        TableColumn<String[], String> tituloCol = new TableColumn<>("Título");
        tituloCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));

        TableColumn<String[], String> duracionCol = new TableColumn<>("Duración");
        duracionCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[2]));

        TableColumn<String[], String> artistaCol = new TableColumn<>("Artista");
        artistaCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[3]));

        songTable.getColumns().addAll(songIdCol, tituloCol, duracionCol, artistaCol);

        // Cargar canciones y portada al seleccionar un álbum
        albumTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int albumId = Integer.parseInt(newSelection[0]);
                loadSongsForAlbum(albumId);
                loadAlbumCover(albumId);
            }
        });

        // Botones
        Button addToCartButton = new Button("Agregar Álbum al Carrito");
        addToCartButton.setOnAction(e -> addToCart());

        Button checkoutButton = new Button("Finalizar Compra");
        checkoutButton.setOnAction(e -> finalizePurchase());

        Button backButton = new Button("Regresar");
        backButton.setOnAction(e -> new UserScreen(stage,userId).show());

        Button showAlbumSongCountButton = new Button("Ver Álbumes y Cantidad de Canciones");
        showAlbumSongCountButton.setOnAction(e -> showAlbumsWithSongCount());

        // Configuración de imagen de portada
        albumCoverView.setFitWidth(100);
        albumCoverView.setPreserveRatio(true);

        // Layout
        VBox vbox = new VBox(10, label, albumTable, albumCoverView, new Label("Canciones del Álbum"), songTable,
                showAlbumSongCountButton, addToCartButton, checkoutButton, backButton);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(vbox, 700, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Compra de Álbumes");
        stage.show();
    }

    private void loadAvailableAlbums() {
        ObservableList<String[]> albums = FXCollections.observableArrayList(db.getAvailableAlbumsWithImages());
        albumTable.setItems(albums);
    }

    private void loadSongsForAlbum(int albumId) {
        ObservableList<String[]> songs = FXCollections.observableArrayList(db.getAlbumSongsWithDetails(albumId));
        songTable.setItems(songs);
    }

    private void loadAlbumCover(int albumId) {
        byte[] imageData = db.getAlbumImage(albumId);
        if (imageData != null) {
            albumCoverView.setImage(new Image(new ByteArrayInputStream(imageData)));
        } else {
            albumCoverView.setImage(null);
        }
    }

    private void addToCart() {
        String[] selectedAlbum = albumTable.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            carrito.add(selectedAlbum);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Álbum añadido al carrito.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Seleccione un álbum para agregar.");
            alert.showAndWait();
        }
    }

    private void finalizePurchase() {
        if (carrito.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "El carrito está vacío. Añada álbumes antes de finalizar la compra.");
            alert.showAndWait();
        } else {
            boolean success = db.registerAlbumPurchase(carrito,userId);
            Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                    success ? "Compra finalizada con éxito, consulte la sección de historial de compras." : "Error al realizar la compra.");
            alert.showAndWait();
            carrito.clear();
        }
    }

    private void showAlbumsWithSongCount() {
        TableView<String[]> albumCountTable = new TableView<>();

        TableColumn<String[], String> albumCol = new TableColumn<>("Álbum");
        albumCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));

        TableColumn<String[], String> countCol = new TableColumn<>("Cantidad de Canciones");
        countCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));

        albumCountTable.getColumns().addAll(albumCol, countCol);

        ObservableList<String[]> albums = FXCollections.observableArrayList(db.getAlbumsWithSongCount());
        albumCountTable.setItems(albums);

        Button backButton = new Button("Regresar");
        backButton.setOnAction(e -> show()); // Vuelve a la vista principal de UserAlbumScreen

        VBox layout = new VBox(10, new Label("Álbumes y su Cantidad de Canciones"), albumCountTable, backButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Álbumes y Cantidad de Canciones");
        stage.show();
    }
}
