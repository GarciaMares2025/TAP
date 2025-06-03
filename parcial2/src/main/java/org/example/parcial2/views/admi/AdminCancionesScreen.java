package org.example.parcial2.views.admi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.utils.Database;

public class AdminCancionesScreen {

    private final Stage stage;
    private final Database db;
    private final TableView<String[]> songTable;

    public AdminCancionesScreen(Stage stage) {
        this.stage = stage;
        this.db = Database.getInstance();
        this.songTable = new TableView<>();
    }

    public void show() {
        Label label = new Label("Administración de Canciones");

        // Botón Regresar
        Button backButton = new Button("Regresar");
        backButton.setOnAction(e -> new AdminScreen(stage).show());

        // Contenedor para colocar el botón en la esquina superior derecha
        HBox topBar = new HBox();
        topBar.getChildren().add(backButton);
        topBar.setStyle("-fx-alignment: top-right; -fx-padding: 10;");

        // Campos para ingresar datos de la canción
        TextField tituloField = new TextField();
        tituloField.setPromptText("Título de la canción");

        TextField duracionField = new TextField();
        duracionField.setPromptText("Duración (HH:MM:SS)");

        // ComboBox para seleccionar género
        ComboBox<String[]> generoBox = new ComboBox<>(FXCollections.observableArrayList(db.getAllGenres()));
        generoBox.setPromptText("Seleccione el género");
        generoBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item[1]); // Mostrar el nombre del género
            }
        });
        generoBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item[1]); // Mostrar el nombre del género
            }
        });

        // ComboBox para seleccionar artista
        ComboBox<String[]> artistaBox = new ComboBox<>(FXCollections.observableArrayList(db.getAllArtists()));
        artistaBox.setPromptText("Seleccione el artista");
        artistaBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item[1]); // Mostrar el nombre del artista
            }
        });
        artistaBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item[1]); // Mostrar el nombre del artista
            }
        });

        // ComboBox para seleccionar álbum
        ComboBox<String[]> albumBox = new ComboBox<>(FXCollections.observableArrayList(db.getAllAlbums()));
        albumBox.setPromptText("Seleccione el álbum");
        albumBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item[1]); // Mostrar el nombre del álbum
            }
        });
        albumBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item[1]); // Mostrar el nombre del álbum
            }
        });


        // Gráfico de pastel para mostrar las canciones por artista
        PieChart pieChart = new PieChart();
        updatePieChart(pieChart);

        Button addButton = new Button("Añadir canción");
        addButton.setOnAction(e -> {
            try {
                String titulo = tituloField.getText();
                String duracion = duracionField.getText();
                int generoId = Integer.parseInt(generoBox.getValue()[0]);
                int artistaId = Integer.parseInt(artistaBox.getValue()[0]);
                int albumId = Integer.parseInt(albumBox.getValue()[0]);

                if (db.addSong(titulo, duracion, generoId, artistaId, albumId)) {
                    loadSongs();
                    updatePieChart(pieChart);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Canción añadida exitosamente.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo añadir la canción.");
                    alert.showAndWait();
                }

                tituloField.clear();
                duracionField.clear();
                generoBox.setValue(null);
                artistaBox.setValue(null);
                albumBox.setValue(null);

            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ingrese un valor válido para los campos.");
                alert.showAndWait();
            }
        });

        // Campo y botón para eliminar canciones por ID
        TextField eliminarIdField = new TextField();
        eliminarIdField.setPromptText("ID de la canción a eliminar");

        Button deleteButton = new Button("Eliminar canción");
        deleteButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(eliminarIdField.getText()); // Validar ID

                // Verificar si la canción existe
                if (!db.doesSongExist(id)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "El ID especificado no existe en la base de datos.");
                    alert.showAndWait();
                    return;
                }

                // Intentar eliminar la canción
                if (db.deleteSongById(id)) {
                    loadSongs(); // Actualizar tabla
                    updatePieChart(pieChart); // Actualizar gráfico
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Canción eliminada exitosamente.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo eliminar la canción.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ingrese un ID válido.");
                alert.showAndWait();
            }

            eliminarIdField.clear(); // Limpiar campo de texto
        });


        // Configuración de la tabla de canciones
        TableColumn<String[], String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));

        TableColumn<String[], String> tituloCol = new TableColumn<>("Título");
        tituloCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));

        TableColumn<String[], String> duracionCol = new TableColumn<>("Duración (HH:MM:SS)");
        duracionCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[2]));

        TableColumn<String[], String> generoCol = new TableColumn<>("Género");
        generoCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[3]));

        TableColumn<String[], String> artistaCol = new TableColumn<>("Artista");
        artistaCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[4]));

        TableColumn<String[], String> albumCol = new TableColumn<>("Álbum");
        albumCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[5]));

        songTable.getColumns().addAll(idCol, tituloCol, duracionCol, generoCol, artistaCol, albumCol);
        loadSongs();

        VBox leftPanel = new VBox(10, label, tituloField, duracionField, generoBox, artistaBox, albumBox, addButton, eliminarIdField, deleteButton, songTable);
        leftPanel.setStyle("-fx-alignment: center;"); // Centrar contenido horizontalmente
        VBox rightPanel = new VBox(10, new Label("Gráfico de Canciones por Artista"), pieChart);

        HBox mainLayout = new HBox(20, leftPanel, rightPanel);
        VBox finalLayout = new VBox(topBar, mainLayout);
        VBox.setVgrow(mainLayout, Priority.ALWAYS);

        Scene scene = new Scene(finalLayout, 1300, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Admin - Canciones");
        stage.show();
    }

    private void loadSongs() {
        ObservableList<String[]> songs = FXCollections.observableArrayList(db.getAllSongsWithDetails());
        songTable.setItems(songs);
    }

    private void updatePieChart(PieChart pieChart) {
        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();
        for (String[] entry : db.getSongsCountByArtist()) {
            int total = Integer.parseInt(entry[1]);
            chartData.add(new PieChart.Data(entry[0] + " (" + total + ")", total)); // Añadir texto con conteo
        }
        pieChart.setData(chartData);
        pieChart.setLabelsVisible(true); // Mostrar etiquetas
        pieChart.setLegendVisible(true); // Mostrar leyenda
    }

}
