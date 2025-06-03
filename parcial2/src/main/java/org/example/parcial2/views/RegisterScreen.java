package org.example.parcial2.views;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.utils.Database;

import java.io.InputStream;

public class RegisterScreen {
    private final Stage stage;

    public RegisterScreen(Stage stage) {
        this.stage = stage;
    }

    /**
     * Construye y muestra la escena de Registro de Usuario.
     */
    public void show() {
        // 1) Logo
        InputStream logoStream = getClass().getResourceAsStream("/Spotify.png");
        ImageView logoView = new ImageView(new Image(logoStream));
        logoView.setFitWidth(150);
        logoView.setPreserveRatio(true);
        logoView.getStyleClass().add("logo-image");

        // 2) Título
        Label titleLabel = new Label("Registro de Usuario");
        titleLabel.getStyleClass().add("title-label");

        // 3) Campos del formulario
        TextField nameField = new TextField();
        nameField.setPromptText("Nombre completo");
        nameField.getStyleClass().add("text-field");
        nameField.setMaxWidth(250);

        TextField phoneField = new TextField();
        phoneField.setPromptText("Teléfono");
        phoneField.getStyleClass().add("text-field");
        phoneField.setMaxWidth(250);

        TextField emailField = new TextField();
        emailField.setPromptText("Correo electrónico");
        emailField.getStyleClass().add("text-field");
        emailField.setMaxWidth(250);

        TextField userField = new TextField();
        userField.setPromptText("Usuario (username)");
        userField.getStyleClass().add("text-field");
        userField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");
        passwordField.getStyleClass().add("password-field");
        passwordField.setMaxWidth(250);

        ChoiceBox<String> roleChoice = new ChoiceBox<>();
        roleChoice.getItems().addAll("user", "artist");
        roleChoice.setValue("user");
        roleChoice.getStyleClass().add("choice-box");
        roleChoice.setMaxWidth(250);

        // 4) Botón “Registrar”
        Button registerButton = new Button("Registrar");
        registerButton.getStyleClass().add("login-button");

        // 5) Enlace “¿Ya tienes cuenta? Inicia sesión”
        Label backToLoginLabel = new Label("¿Ya tienes cuenta? Inicia sesión");
        backToLoginLabel.getStyleClass().add("create-account-label");
        backToLoginLabel.setOnMouseClicked(e -> {
            LoginScreen loginScreen = new LoginScreen(stage);
            loginScreen.show();
        });

        // 6) Layout principal
        VBox vbox = new VBox(15,
                logoView,
                titleLabel,
                nameField,
                phoneField,
                emailField,
                userField,
                passwordField,
                roleChoice,
                registerButton,
                backToLoginLabel
        );
        vbox.getStyleClass().add("login-screen");

        Scene scene = new Scene(vbox, 450, 700);
        scene.getStylesheets().add(getClass().getResource("/Logina.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Registro de Usuario");
        stage.show();

        // 7) Acción del botón “Registrar”
        registerButton.setOnAction(e -> {
            String nombre   = nameField.getText().trim();
            String telUser  = phoneField.getText().trim();
            String email    = emailField.getText().trim();
            String username = userField.getText().trim();
            String pass     = passwordField.getText().trim();
            String role     = roleChoice.getValue();

            if (nombre.isEmpty() || telUser.isEmpty() || email.isEmpty() ||
                    username.isEmpty() || pass.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Por favor completa todos los campos.");
                return;
            }

            if (Database.getInstance().isUserExists(username)) {
                showAlert(Alert.AlertType.WARNING, "El nombre de usuario ya existe. Elige otro.");
                return;
            }

            Database.getInstance().createUser(nombre, telUser, email, username, pass, role);
            showAlert(Alert.AlertType.INFORMATION, "Usuario registrado exitosamente.");

            // Después de registrar, volvemos al login
            LoginScreen loginScreen = new LoginScreen(stage);
            loginScreen.show();
        });
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
