package org.example.parcial2.views.users;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.views.LoginScreen;

public class UserScreen {

    private final Stage stage;
    private final int userId; // ID del usuario autenticado

    public UserScreen(Stage stage, int userId) {
        this.stage = stage;
        this.userId = userId;
    }

    public void show() {
        Label welcomeLabel = new Label("Bienvenido a la sección de Usuario");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        // Botón: Compra de Canciones
        Button compraCancionesButton = new Button("Compra de Canciones");
        compraCancionesButton.setOnAction(e -> new UserCancionScreen(stage, userId).show());

        // Botón: Compra de Álbumes
        Button compraAlbumesButton = new Button("Compra de Álbumes");
        compraAlbumesButton.setOnAction(e -> new UserAlbumScreen(stage, userId).show());

        // Botón: Historial de Compras
        Button historialComprasButton = new Button("Historial de Compras");
        historialComprasButton.setOnAction(e -> new UserHistorialScreen(stage, userId).show());

        // Botón: Datos Personales
        Button datosPersonalesButton = new Button("Datos Personales");
        datosPersonalesButton.setOnAction(e -> new UserDatosScreen(stage, userId).show());

        // ** Nuevo botón: Reproducir Música **
        Button reproducirButton = new Button("Reproducir Música");
        reproducirButton.setOnAction(e -> new UserReproducirScreen(stage, userId).show());

        // Botón: Cerrar Sesión
        Button logoutButton = new Button("Cerrar Sesión");
        logoutButton.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen(stage);
            loginScreen.show();  // <-- ahora este método existe en LoginScreen
        });

        // Layout vertical
        VBox vbox = new VBox(15,
                welcomeLabel,
                compraCancionesButton,
                compraAlbumesButton,
                historialComprasButton,
                datosPersonalesButton,
                reproducirButton,    // <-- aquí el nuevo botón
                logoutButton
        );
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #121212;");

        Scene scene = new Scene(vbox, 500, 450);
        scene.getStylesheets().add(getClass().getResource("/Logina.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Usuario - Menú Principal");
        stage.show();
    }
}
