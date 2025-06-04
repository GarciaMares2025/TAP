package org.example.parcial2.views.users;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.controlller.UserController;
import org.example.parcial2.models.User;
import org.example.parcial2.views.LoginScreen;

/**
 * Pantalla principal del usuario (rol = “user”).
 */
public class UserScreen {

    private final Stage stage;
    private final int userId; // ID del usuario autentica

    private final UserController userController;
    private String userName;



    public UserScreen(Stage stage, int userId) {
        this.stage = stage;
        this.userId = userId;

        this.userController = new UserController();
        User currentUser = userController.buscarUsuarioPorId(userId);
        this.userName = (currentUser != null) ? currentUser.getNombre() : "Usuario #" + userId;



    }

    public void show() {
        Label welcomeLabel = new Label("Bienvenido a la sección de Usuario ID: " + userId +" "+ userName+  "");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

        // Botón: Compra de Canciones
        Button compraCancionesButton = new Button("Compra de Canciones");
        compraCancionesButton.setOnAction(e -> {
            UserCancionScreen ucScreen = new UserCancionScreen(stage, userId);
            ucScreen.show();
        });

        // Botón: Compra de Álbumes
        Button compraAlbumesButton = new Button("Compra de Álbumes");
        compraAlbumesButton.setOnAction(e -> {
            UserAlbumScreen uaScreen = new UserAlbumScreen(stage, userId);
            uaScreen.show();
        });

        // Botón: Historial de Compras
        Button historialComprasButton = new Button("Historial de Compras");
        historialComprasButton.setOnAction(e -> {
            UserHistorialScreen uhScreen = new UserHistorialScreen(stage, userId);
            uhScreen.show();
        });

        // Botón: Datos Personales
        Button datosPersonalesButton = new Button("Datos Personales");
        datosPersonalesButton.setOnAction(e -> {
            UserDatosScreen udScreen = new UserDatosScreen(stage, userId);
            udScreen.show();
        });

        // Botón: Reproducir Música
        Button reproducirButton = new Button("Reproducir Música");
        reproducirButton.setOnAction(e -> {
            UserReproducirScreen urScreen = new UserReproducirScreen(stage, userId);
            urScreen.show();
        });

        // Botón: Cerrar Sesión
        Button logoutButton = new Button("Cerrar Sesión");
        logoutButton.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen(stage);
            loginScreen.show();
        });

        VBox vbox = new VBox(12,
                welcomeLabel,
                compraCancionesButton,
                compraAlbumesButton,
                historialComprasButton,
                datosPersonalesButton,
                reproducirButton,
                logoutButton
        );
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #121212;");

        Scene scene = new Scene(vbox, 500, 450);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Usuario - Menú Principal");
        stage.show();
    }
}
