package org.example.parcial2.views.admi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.views.users.UserHistorialScreen;
import org.example.parcial2.utils.Database;
import org.example.parcial2.models.User;

public class AdminUsuariosScreen {

    private final Stage stage;
    private final Database db;
    private final TableView<User> userTable;

    public AdminUsuariosScreen(Stage stage) {
        this.stage = stage;
        this.db = Database.getInstance();
        this.userTable = new TableView<>();
    }

    public void show() {
        Label label = new Label("Administración de Usuarios");

        // Configurar la tabla de usuarios
        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idUser"));

        TableColumn<User, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<User, String> telCol = new TableColumn<>("Teléfono");
        telCol.setCellValueFactory(new PropertyValueFactory<>("telUser"));

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("emailUser"));

        TableColumn<User, String> userCol = new TableColumn<>("Usuario");
        userCol.setCellValueFactory(new PropertyValueFactory<>("user"));

        TableColumn<User, String> passwordCol = new TableColumn<>("Contraseña");
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<User, String> roleCol = new TableColumn<>("Rol");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        userTable.getColumns().addAll(idCol, nombreCol, telCol, emailCol, userCol, passwordCol, roleCol);
        loadUsers();

        // Label dinámico para el total de usuarios
        Label totalUsuariosLabel = new Label();
        updateTotalUsuariosLabel(totalUsuariosLabel);

        // Botón para eliminar usuario
        TextField idField = new TextField();
        idField.setPromptText("ID de usuario a eliminar");

        Button deleteButton = new Button("Eliminar usuario");
        deleteButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());

                // Confirmación de eliminación
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        "¿Está seguro de que desea eliminar el usuario con ID " + id + "?",
                        ButtonType.YES, ButtonType.NO);
                confirmAlert.setTitle("Confirmación de eliminación");
                confirmAlert.setHeaderText(null);

                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        if (db.deleteUserById(id)) {
                            loadUsers();
                            updateTotalUsuariosLabel(totalUsuariosLabel); // Actualizar el total de usuarios
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Usuario eliminado exitosamente.");
                            alert.showAndWait();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "El usuario con el ID especificado no existe.");
                            alert.showAndWait();
                        }
                    }
                });
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, ingrese un ID válido.");
                alert.showAndWait();
            }
        });

        // Botón para ver historial de compras en una nueva ventana
        Button viewHistoryButton = new Button("Ver historial de compras");
        viewHistoryButton.setOnAction(e -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                // Crear un nuevo Stage para la ventana del historial
                Stage historyStage = new Stage();
                new UserHistorialScreen(historyStage, selectedUser.getIdUser()).show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, seleccione un usuario.");
                alert.showAndWait();
            }
        });

        // Botón para regresar
        Button backButton = new Button("Regresar");
        backButton.setOnAction(e -> new AdminScreen(stage).show());

        // HBox para los botones
        HBox buttonBox = new HBox(10, viewHistoryButton, backButton);
        buttonBox.setStyle("-fx-padding: 10; -fx-alignment: center;");

        VBox vbox = new VBox(10, label, userTable, idField, deleteButton, buttonBox, totalUsuariosLabel);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(vbox, 900, 500);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Admin - Usuarios");
        stage.show();
    }

    private void updateTotalUsuariosLabel(Label label) {
        try {
            int totalUsuarios = db.getTotalUsuarios(); // Método en la clase Database
            label.setText("Total de usuarios: " + totalUsuarios);
        } catch (Exception e) {
            label.setText("Error al obtener el total de usuarios.");
        }
    }

    private void loadUsers() {
        ObservableList<User> users = FXCollections.observableArrayList(db.getAllUsers());
        userTable.setItems(users);
    }
}
