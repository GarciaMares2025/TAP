package org.example.parcial2.views.admi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.utils.Database;

import java.util.List;

/**
 * Pantalla que muestra todos los álbumes y permite navegar a la gestión de canciones.
 */
public class AdminAlbumesScreen {

    private final Stage stage;
    private final int userId;
    private final TableView<String[]> albumTable;
    private Label totalLabel; // Mantener referencia directa al Label

    /** Constructor principal que recibe Stage + userId */
    public AdminAlbumesScreen(Stage stage, int userId) {
        this.stage = stage;
        this.userId = userId;
        this.albumTable = new TableView<>();
    }

    /** Sobrecarga: constructor que solo recibe Stage */
    public AdminAlbumesScreen(Stage stage) {
        this(stage, -1);
    }

    /** Muestra la pantalla de listado de álbumes */
    public void show() {
        Label titleLabel = new Label("Listado de Álbumes");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Columnas de la tabla: {idAlbum, nombreAlbum}
        TableColumn<String[], String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));
        idCol.setPrefWidth(80);

        TableColumn<String[], String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));
        nombreCol.setPrefWidth(250);

        albumTable.getColumns().clear();
        albumTable.getColumns().addAll(idCol, nombreCol);
        albumTable.setPrefHeight(300);

        // Crear el label del total y mantener referencia
        totalLabel = new Label();
        totalLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Cargar álbumes después de crear todos los componentes
        loadAlbums();

        // Botón para ver canciones del álbum seleccionado
        Button btnVerCanciones = new Button("Ver Canciones");
        btnVerCanciones.setPrefSize(120, 35);
        btnVerCanciones.setOnAction(e -> {
            String[] seleccionado = albumTable.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                try {
                    int albumId = Integer.parseInt(seleccionado[0]);
                    new AdminAlbumCancionScreen(stage, userId).showWithAlbum(albumId, seleccionado[1]);
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Error al procesar el ID del álbum.");
                    alert.showAndWait();
                }
            } else {
                Alert alerta = new Alert(Alert.AlertType.WARNING,
                        "Por favor, selecciona un álbum de la lista.", ButtonType.OK);
                alerta.setTitle("Álbum no seleccionado");
                alerta.setHeaderText(null);
                alerta.showAndWait();
            }
        });

        // Botón de regreso al menú principal de Admin
        Button backButton = new Button("← Regresar");
        backButton.setPrefSize(120, 35);
        backButton.setOnAction(e -> new AdminScreen(stage, userId).show());

        HBox buttonBox = new HBox(15, btnVerCanciones, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(15,
                titleLabel,
                albumTable,
                totalLabel,
                buttonBox
        );
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #121212;");

        Scene scene = new Scene(root, 480, 550);

        // Manejo seguro del CSS
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("No se pudo cargar el archivo CSS: " + e.getMessage());
        }

        stage.setScene(scene);
        stage.setTitle("Administrar Álbumes");
        stage.show();
    }

    /** Carga todos los álbumes desde la base de datos */
    private void loadAlbums() {
        try {
            List<String[]> lista = Database.getInstance().getAllAlbums();
            ObservableList<String[]> data = FXCollections.observableArrayList(lista);
            albumTable.setItems(data);

            // Actualizar el total usando la referencia directa
            updateTotalLabel();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al cargar los álbumes: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    /** Actualiza el label con el total de álbumes */
    private void updateTotalLabel() {
        try {
            if (totalLabel != null) {
                int total = albumTable.getItems().size();
                totalLabel.setText("Total de álbumes: " + total);
            }
        } catch (Exception e) {
            if (totalLabel != null) {
                totalLabel.setText("Error al obtener el total de álbumes.");
            }
        }
    }
}