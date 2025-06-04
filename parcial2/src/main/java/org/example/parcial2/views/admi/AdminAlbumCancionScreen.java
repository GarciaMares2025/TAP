package org.example.parcial2.views.admi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.utils.Database;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Pantalla para mostrar las canciones de un álbum específico.
 * Permite recibir id y nombre de álbum directamente mediante showWithAlbum().
 */
public class AdminAlbumCancionScreen {

    private final Stage stage;
    private final int userId;
    private final TableView<String[]> songTable;
    private final ImageView albumCoverView;

    /** Constructor con Stage + userId */
    public AdminAlbumCancionScreen(Stage stage, int userId) {
        this.stage = stage;
        this.userId = userId;
        this.songTable = new TableView<>();
        this.albumCoverView = new ImageView();
        albumCoverView.setFitWidth(120);
        albumCoverView.setFitHeight(120);
        albumCoverView.setPreserveRatio(true);
    }

    /** Muestra las canciones del álbum dado */
    public void showWithAlbum(int albumId, String albumName) {
        Label titleLabel = new Label("Álbum: " + albumName);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        // Tabla de canciones: {idCancion, titulo, duracion}
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

        songTable.getColumns().setAll(idCol, tituloCol, duracionCol);
        songTable.setPrefHeight(200);

        loadSongsForAlbum(albumId);
        loadAlbumCover(albumId);

        Button backButton = new Button("← Regresar");
        backButton.setOnAction(e -> new AdminAlbumesScreen(stage, userId).show());

        VBox root = new VBox(12,
                titleLabel,
                albumCoverView,
                songTable,
                backButton
        );
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #121212;");

        Scene scene = new Scene(root, 500, 500);

        // Manejo seguro del CSS
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("No se pudo cargar el archivo CSS: " + e.getMessage());
        }

        stage.setScene(scene);
        stage.setTitle("Canciones de \"" + albumName + "\"");
        stage.show();
    }

    private void loadSongsForAlbum(int albumId) {
        try {
            List<String[]> listaCanciones = Database.getInstance().getSongsByAlbumId(albumId);
            ObservableList<String[]> canciones = FXCollections.observableArrayList(listaCanciones);
            songTable.setItems(canciones);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al cargar las canciones: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    private void loadAlbumCover(int albumId) {
        try {
            byte[] imageBytes = Database.getInstance().getAlbumImageById(albumId);
            if (imageBytes != null) {
                Image albumImg = new Image(new ByteArrayInputStream(imageBytes));
                albumCoverView.setImage(albumImg);
            } else {
                albumCoverView.setImage(null);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen del álbum: " + e.getMessage());
            albumCoverView.setImage(null);
        }
    }
}