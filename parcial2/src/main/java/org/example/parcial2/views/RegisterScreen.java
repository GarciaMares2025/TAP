package org.example.parcial2.views;

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
import org.example.parcial2.utils.PasswordUtils; // Importar PasswordUtils

public class RegisterScreen {

    private final Stage stage;

    public RegisterScreen(Stage stage) {
        this.stage = stage;
    }

    /**
     * Construye la Scene de registro de usuario.
     */
    public Scene getRegisterScene() {
        // Cargar el logo de Spotify
        Image logoImage = new Image(getClass().getResourceAsStream("/Spotify.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(150);
        logoView.setPreserveRatio(true);

        // Título con estilo de Spotify
        Label title = new Label("Crea tu cuenta en Spotify");
        title.getStyleClass().add("title-label");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");

        // Labels y campos con estilo
        Label nameLabel = new Label("Nombre:");
        nameLabel.getStyleClass().add("form-label");
        nameLabel.setStyle("-fx-text-fill: white;");
        TextField nameField = new TextField();
        nameField.setPromptText("Ingresa tu nombre completo");
        nameField.getStyleClass().add("text-field");

        Label phoneLabel = new Label("Teléfono:");
        phoneLabel.getStyleClass().add("form-label");
        phoneLabel.setStyle("-fx-text-fill: white;");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Ingresa tu teléfono");
        phoneField.getStyleClass().add("text-field");

        Label emailLabel = new Label("Email:");
        emailLabel.getStyleClass().add("form-label");
        emailLabel.setStyle("-fx-text-fill: white;");
        TextField emailField = new TextField();
        emailField.setPromptText("Ingresa tu correo electrónico");
        emailField.getStyleClass().add("text-field");

        Label userLabel = new Label("Usuario:");
        userLabel.getStyleClass().add("form-label");
        userLabel.setStyle("-fx-text-fill: white;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Elige un nombre de usuario");
        usernameField.getStyleClass().add("text-field");

        Label passwordLabel = new Label("Contraseña:");
        passwordLabel.getStyleClass().add("form-label");
        passwordLabel.setStyle("-fx-text-fill: white;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Crea una contraseña (mín. 6 caracteres)");
        passwordField.getStyleClass().add("password-field");

        // Campo para confirmar contraseña
        Label confirmPasswordLabel = new Label("Confirmar Contraseña:");
        confirmPasswordLabel.getStyleClass().add("form-label");
        confirmPasswordLabel.setStyle("-fx-text-fill: white;");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirma tu contraseña");
        confirmPasswordField.getStyleClass().add("password-field");

        Label roleLabel = new Label("Tipo de cuenta:");
        roleLabel.getStyleClass().add("form-label");
        roleLabel.setStyle("-fx-text-fill: white;");

        // ComboBox para seleccionar rol: user / artist / admin
        ObservableList<String> roles = FXCollections.observableArrayList("user", "artist");
        ComboBox<String> roleCombo = new ComboBox<>(roles);
        roleCombo.setValue("user");
        roleCombo.setStyle("-fx-background-color: #333; -fx-text-fill: white;");

        Button registerButton = new Button("Registrar");
        registerButton.getStyleClass().add("login-button");
        registerButton.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 25px; -fx-background-radius: 25px;");

        registerButton.setOnAction(e -> {
            String nombre = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String confirmPassword = confirmPasswordField.getText().trim();
            String role = roleCombo.getValue();

            // Validaciones mejoradas
            if (nombre.isEmpty() || phone.isEmpty() || email.isEmpty() ||
                    username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showAlert("Todos los campos son obligatorios", Alert.AlertType.WARNING);
                return;
            }

            // Validar longitud de contraseña
            if (password.length() < 6) {
                showAlert("La contraseña debe tener al menos 6 caracteres", Alert.AlertType.WARNING);
                return;
            }

            // Validar que las contraseñas coincidan
            if (!password.equals(confirmPassword)) {
                showAlert("Las contraseñas no coinciden", Alert.AlertType.WARNING);
                return;
            }

            // Validar formato de email básico
            if (!email.contains("@") || !email.contains(".")) {
                showAlert("Por favor ingresa un email válido", Alert.AlertType.WARNING);
                return;
            }

            try {
                // La contraseña se encriptará automáticamente en Database.createUser()
                Database.getInstance().createUser(nombre, phone, email, username, password, role);

                showAlert("¡Usuario registrado exitosamente!",
                        Alert.AlertType.INFORMATION);

                // Volvemos al login
                LoginScreen loginScreen = new LoginScreen(stage);
                loginScreen.show();

            } catch (Exception ex) {
                showAlert("Error al registrar usuario.\nPosibles causas:\n• El usuario ya existe\n• El email ya está registrado\n• Error de conexión",
                        Alert.AlertType.ERROR);
                ex.printStackTrace(); // Para debugging
            }
        });

        Label backToLoginLabel = new Label("¿Ya tienes cuenta? Inicia sesión");
        backToLoginLabel.getStyleClass().add("create-account-label");
        backToLoginLabel.setStyle("-fx-text-fill: #1DB954; -fx-underline: true; -fx-cursor: hand;");
        backToLoginLabel.setOnMouseClicked(e -> {
            LoginScreen loginScreen = new LoginScreen(stage);
            loginScreen.show();
        });

        // Contenedor principal con logo incluido
        VBox vbox = new VBox(15,
                logoView,
                title,
                nameLabel, nameField,
                phoneLabel, phoneField,
                emailLabel, emailField,
                userLabel, usernameField,
                passwordLabel, passwordField,
                confirmPasswordLabel, confirmPasswordField,
                roleLabel, roleCombo,
                registerButton,
                backToLoginLabel
        );
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.getStyleClass().add("login-screen");
        vbox.setStyle("-fx-background-color: #121212;");

        return new Scene(vbox, 500, 900);
    }

    /**
     * Método helper para mostrar alertas
     */
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle("Registro de Usuario");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Muestra la ventana de registro (asigna la Scene al Stage).
     */
    public void show() {
        Scene scene = getRegisterScene();
        scene.getStylesheets().add(getClass().getResource("/Logina.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Registro - Spotify");
        stage.show();
    }
}