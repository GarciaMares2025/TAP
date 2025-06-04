package org.example.parcial2.views.admi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.models.User;
import org.example.parcial2.utils.Database;
import org.example.parcial2.views.users.UserHistorialScreen;

/**
 * Pantalla que muestra todos los usuarios registrados y permite gestionarlos.
 * Las columnas "ID" y "Email" solo aparecen si el usuario logueado tiene rol "admin".
 */
public class AdminUsuariosScreen {

    private final Stage stage;
    private final Database db;
    private final TableView<User> userTable;
    private final User loggedUser;
    private final int userId;

    /** Constructor principal con Stage + usuario logueado */
    public AdminUsuariosScreen(Stage stage, User loggedUser) {
        this.stage = stage;
        this.db = Database.getInstance();
        this.userTable = new TableView<>();
        this.loggedUser = loggedUser;
        this.userId = loggedUser != null ? loggedUser.getIdUser() : -1;
    }

    /** Constructor alternativo con Stage + userId */
    public AdminUsuariosScreen(Stage stage, int userId) {
        this.stage = stage;
        this.db = Database.getInstance();
        this.userTable = new TableView<>();
        this.loggedUser = null;
        this.userId = userId;
    }

    /** Muestra la pantalla de listado de usuarios */
    public void show() {
        Label label = new Label("Administración de Usuarios");
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        boolean isAdmin = loggedUser != null && "admin".equalsIgnoreCase(loggedUser.getRole());

        // Columnas de la tabla
        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        idCol.setPrefWidth(60);

        TableColumn<User, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nombreCol.setPrefWidth(150);

        TableColumn<User, String> telCol = new TableColumn<>("Teléfono");
        telCol.setCellValueFactory(new PropertyValueFactory<>("telUser"));
        telCol.setPrefWidth(120);

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("emailUser"));
        emailCol.setPrefWidth(200);

        TableColumn<User, String> userCol = new TableColumn<>("Usuario");
        userCol.setCellValueFactory(new PropertyValueFactory<>("user"));
        userCol.setPrefWidth(120);

        TableColumn<User, String> passwordCol = new TableColumn<>("Contraseña");
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordCol.setPrefWidth(120);

        TableColumn<User, String> roleCol = new TableColumn<>("Rol");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(100);

        if (isAdmin || loggedUser == null) {
            userTable.getColumns().addAll(idCol, nombreCol, telCol, emailCol, userCol, passwordCol, roleCol);
        } else {
            userTable.getColumns().addAll(nombreCol, telCol, userCol, passwordCol, roleCol);
        }

        loadUsers();

        Label totalUsuariosLabel = new Label();
        totalUsuariosLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        updateTotalUsuariosLabel(totalUsuariosLabel);

        // TextField y botón para eliminar usuario (solo visible para admin)
        TextField idField = new TextField();
        idField.setPromptText("ID de usuario a eliminar");
        idField.setMaxWidth(120);

        Button deleteButton = new Button("Eliminar usuario");
        deleteButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                if (id <= 0) {
                    new Alert(Alert.AlertType.ERROR, "Por favor, ingrese un ID válido.").showAndWait();
                    return;
                }

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        "¿Está seguro de que desea eliminar el usuario con ID " + id + "?",
                        ButtonType.YES, ButtonType.NO);
                confirmAlert.setTitle("Confirmación de eliminación");
                confirmAlert.setHeaderText(null);
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        if (db.deleteUserById(id)) {
                            loadUsers();
                            updateTotalUsuariosLabel(totalUsuariosLabel);
                            idField.clear();
                            new Alert(Alert.AlertType.INFORMATION, "Usuario eliminado exitosamente.").showAndWait();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "El usuario con el ID especificado no existe.").showAndWait();
                        }
                    }
                });
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Por favor, ingrese un ID numérico válido.").showAndWait();
            }
        });

        HBox deleteBox = new HBox(10, idField, deleteButton);
        deleteBox.setAlignment(Pos.CENTER);
        if (!isAdmin && loggedUser != null) {
            deleteBox.setManaged(false);
            deleteBox.setVisible(false);
        }

        // Botón para ver historial de compras
        Button viewHistoryButton = new Button("Ver historial de compras");
        viewHistoryButton.setOnAction(e -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                Stage historyStage = new Stage();
                int selectedUserId = selectedUser.getIdUser();
                new UserHistorialScreen(historyStage, selectedUserId).show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Por favor, seleccione un usuario.").showAndWait();
            }
        });

        // Botón para regresar al menú principal
        Button backButton = new Button("← Regresar");
        backButton.setOnAction(e -> new AdminScreen(stage, userId).show());

        HBox buttonBox = new HBox(10, viewHistoryButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(10, label, userTable, deleteBox, buttonBox, totalUsuariosLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #121212;");

        Scene scene = new Scene(vbox, 950, 550);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Admin - Usuarios");
        stage.show();
    }

    private void updateTotalUsuariosLabel(Label label) {
        try {
            int totalUsuarios = db.getTotalUsuarios();
            label.setText("Total de usuarios: " + totalUsuarios);
        } catch (Exception e) {
            label.setText("Error al obtener el total de usuarios.");
            e.printStackTrace(); // Para debugging
        }
    }

    private void loadUsers() {
        try {
            ObservableList<User> users = FXCollections.observableArrayList(db.getAllUsers());
            userTable.setItems(users);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar los usuarios: " + e.getMessage()).showAndWait();
            e.printStackTrace(); // Para debugging
        }
    }
}