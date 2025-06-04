package org.example.parcial2.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    /**
     * Construye la Scene de login sin asignarla al Stage.
     */
    public Scene getLoginScene() {
        // Cargar el logo de Spotify
        InputStream logoStream = getClass().getResourceAsStream("/Spotify.png");
        Image logoImage = new Image(logoStream);
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(150); // Ajustar tamaño del logo
        logoView.setPreserveRatio(true);

        // Título con estilo de Spotify
        Label title = new Label("Inicia sesión en Spotify");
        title.getStyleClass().add("title-label");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Nombre de usuario");
        usernameField.getStyleClass().add("text-field");
        usernameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");
        passwordField.getStyleClass().add("password-field");
        passwordField.setMaxWidth(250);

        Button loginButton = new Button("Iniciar sesión");
        loginButton.getStyleClass().add("login-button");
        loginButton.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 25px; -fx-background-radius: 25px;");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Por favor ingresa usuario y contraseña.", ButtonType.OK)
                        .showAndWait();
                return;
            }

            boolean ok = Database.getInstance().authenticateUser(username, password);
            if (ok) {
                int idUser = Database.getInstance().getUserId(username);
                String role = Database.getInstance().getUserRole(username);

                if ("admin".equalsIgnoreCase(role)) {
                    // Ahora AdminScreen recibe Stage + userId
                    AdminScreen adminScreen = new AdminScreen(stage, idUser);
                    adminScreen.show();
                } else {
                    // UserScreen también deberá aceptar Stage + userId
                    UserScreen userScreen = new UserScreen(stage, idUser);
                    userScreen.show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Usuario o contraseña incorrectos", ButtonType.OK)
                        .showAndWait();
            }
        });

        Label registerLink = new Label("¿No tienes cuenta? Regístrate en Spotify");
        registerLink.getStyleClass().add("create-account-label");
        registerLink.setStyle("-fx-text-fill: #1DB954; -fx-underline: true; -fx-cursor: hand;");
        registerLink.setOnMouseClicked(e -> {
            RegisterScreen registerScreen = new RegisterScreen(stage);
            registerScreen.show();
        });

        // Layout principal con el logo incluido
        VBox vbox = new VBox(20, logoView, title, usernameField, passwordField, loginButton, registerLink);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.getStyleClass().add("login-screen"); // Clase CSS específica
        vbox.setStyle("-fx-background-color: #121212;");

        return new Scene(vbox, 600, 600);
    }

    /**
     * Muestra la ventana de login en el Stage (asigna la Scene).
     */
    public void show() {
        Scene scene = getLoginScene();
        scene.getStylesheets().add(getClass().getResource("/Logina.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Login - Spotify");
        stage.show();
    }
}