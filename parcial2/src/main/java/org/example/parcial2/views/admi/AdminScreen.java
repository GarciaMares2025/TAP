package org.example.parcial2.views.admi;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.views.LoginScreen;

public class AdminScreen {

    private final Stage stage;
    private final int adminId;

    /**
     * Constructor principal que recibe STAGE + ID del administrador.
     * Úsalo cuando quieras conocer el `adminId` (quizá para filtrar datos, etc.).
     */
    public AdminScreen(Stage stage, int adminId) {
        this.stage   = stage;
        this.adminId = adminId;
    }

    /**
     * Constructor adicional para compatibilidad: solo recibe el STAGE.
     * Internamente delega en el de dos parámetros, asignando adminId = 0.
     */
    public AdminScreen(Stage stage) {
        this(stage, 0);
    }

    /**
     * Construye y muestra la pantalla principal del administrador.
     */
    public void show() {
        // Etiqueta de bienvenida (usa adminId solo a modo de ejemplo; puedes omitirlo si no lo necesitas)
        Label welcomeLabel = new Label("Bienvenido administrador" +
                (adminId > 0 ? " (ID: " + adminId + ")" : ""));
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

        // Botones de ejemplo
        Button usuariosButton = new Button("Gestionar Usuarios");
        usuariosButton.setOnAction(e -> {
            // new AdminUsuariosScreen(stage).show();  (ejemplo)
        });

        Button artistasButton = new Button("Gestionar Artistas");
        artistasButton.setOnAction(e -> {
            // new AdminArtistasScreen(stage).show();
        });

        Button generosButton = new Button("Gestionar Géneros");
        generosButton.setOnAction(e -> {
            // new AdminGenerosScreen(stage).show();
        });

        Button totalVentasButton = new Button("Ver Total de Ventas");
        totalVentasButton.setOnAction(e -> {
            // new AdminTotalCancionesVendidas(stage).show();
        });

        // Botón de cerrar sesión: volvemos a login
        Button logoutButton = new Button("Cerrar Sesión");
        logoutButton.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen(stage);
            loginScreen.show();
        });

        // Layout vertical con todos los controles
        VBox vbox = new VBox(10,
                welcomeLabel,
                usuariosButton,
                artistasButton,
                generosButton,
                totalVentasButton,
                logoutButton
        );
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #121212;");

        Scene scene = new Scene(vbox, 400, 400);
        scene.getStylesheets().add(getClass().getResource("/Logina.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Ventana de Administrador");
        stage.show();
    }
}
