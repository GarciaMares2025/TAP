package org.example.parcial2.views.users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.views.ReproductorScreen;
import org.example.parcial2.utils.Database;

import java.util.List;

/**
 * Pantalla que muestra todas las canciones que el usuario ha comprado,
 * y permite reproducirlas.
 */
public class UserReproducirScreen {

    private final Stage stage;
    private final int userId;
    private final Database db;
    private final TableView<String[]> tableView;

    public UserReproducirScreen(Stage stage, int userId) {
        this.stage  = stage;
        this.userId = userId;
        this.db     = Database.getInstance();
        this.tableView = new TableView<>();
    }

    public void show() {
        Label label = new Label("Canciones que has comprado:");
        label.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        // Columna: ID Canción
        TableColumn<String[], String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(cd.getValue()[0])
        );
        idCol.setPrefWidth(50);

        // Columna: Título
        TableColumn<String[], String> tituloCol = new TableColumn<>("Título");
        tituloCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(cd.getValue()[1])
        );
        tituloCol.setPrefWidth(200);

        // Columna: Duración
        TableColumn<String[], String> duracionCol = new TableColumn<>("Duración");
        duracionCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(cd.getValue()[2])
        );
        duracionCol.setPrefWidth(100);

        // Columna “Reproducir”
        TableColumn<String[], Void> playCol = new TableColumn<>("Reproducir");
        playCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("▶");

            {
                btn.setOnAction(e -> {
                    String[] fila = getTableView().getItems().get(getIndex());
                    if (fila != null) {
                        int idCancion;
                        try {
                            idCancion = Integer.parseInt(fila[0]);
                        } catch (NumberFormatException ex) {
                            new Alert(Alert.AlertType.ERROR, "ID inválido: " + fila[0]).showAndWait();
                            return;
                        }
                        // <<< Aquí: ahora pasamos también userId >>>
                        ReproductorScreen rep = new ReproductorScreen(stage, idCancion, userId);
                        rep.show();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
        playCol.setPrefWidth(80);
        tableView.getColumns().addAll(idCol, tituloCol, duracionCol, playCol);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Cargar datos desde la BD
        List<String[]> compradas = db.getPurchasedSongsByUser(userId);
        ObservableList<String[]> data = FXCollections.observableArrayList(compradas);
        tableView.setItems(data);

        // Botón “Regresar” al menú de usuario
        Button backButton = new Button("Regresar");
        backButton.setOnAction(e -> new UserScreen(stage, userId).show());

        VBox vbox = new VBox(10, label, tableView, backButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #121212;");

        Scene scene = new Scene(vbox, 600, 450);
        scene.getStylesheets().add(getClass().getResource("/Logina.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Tus Canciones Compradas");
        stage.show();
    }
}
