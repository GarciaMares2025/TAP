package org.example.parcial2.views;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.utils.Database;
import org.example.parcial2.views.admi.AdminScreen;
import org.example.parcial2.views.users.UserScreen;

import java.io.InputStream;

public class LoginScreen {
    private final Stage stage;

    public LoginScreen(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // 1) Logo de Spotify (opcional)
        InputStream logoStream = getClass().getResourceAsStream("/Spotify.png");
        ImageView logoView = new ImageView(new Image(logoStream));
        logoView.setFitWidth(150);
        logoView.setPreserveRatio(true);
        logoView.getStyleClass().add("logo-image");

        // 2) Título “Login”
        Label titleLabel = new Label("Iniciar Sesion");
        titleLabel.getStyleClass().add("title-label");

        // 3) Campo de usuario
        TextField usernameField = new TextField();
        usernameField.setPromptText("Usuario");
        usernameField.getStyleClass().add("text-field");
        usernameField.setMaxWidth(250);

        // 4) Campo de contraseña
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");
        passwordField.getStyleClass().add("password-field");
        passwordField.setMaxWidth(250);

        // 5) Botón “Iniciar sesión”
        Button loginButton = new Button("Iniciar sesión");
        loginButton.getStyleClass().add("login-button");
        loginButton.setDefaultButton(true);

        // 6) Enlace “¿No tienes cuenta? Regístrate en Spotify”
        Label registerLabel = new Label("¿No tienes cuenta? Regístrate en Spotify");
        registerLabel.getStyleClass().add("create-account-label");
        registerLabel.setOnMouseClicked(e -> {
            RegisterScreen registerScreen = new RegisterScreen(stage);
            registerScreen.show();
        });

        // 7) Layout principal
        VBox vbox = new VBox(20, logoView, titleLabel, usernameField, passwordField, loginButton, registerLabel);
        vbox.getStyleClass().add("login-screen");

        // 8) Crear escena y aplicar CSS
        Scene scene = new Scene(vbox, 400, 500);
        scene.getStylesheets().add(getClass().getResource("/Logina.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();

        // 9) Lógica del botón “Iniciar sesión”
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Por favor ingresa usuario y contraseña.");
                return;
            }

            boolean ok = Database.getInstance().authenticateUser(username, password);
            if (ok) {
                int idUser = Database.getInstance().getUserId(username);
                String role = Database.getInstance().getUserRole(username);

                if ("admin".equalsIgnoreCase(role)) {
                    // Ahora coincide con el nuevo constructor AdminScreen(Stage, int)
                    AdminScreen adminScreen = new AdminScreen(stage, idUser);
                    adminScreen.show();
                } else {
                    UserScreen userScreen = new UserScreen(stage, idUser);
                    userScreen.show();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Usuario o contraseña incorrectos");
            }
        });
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
