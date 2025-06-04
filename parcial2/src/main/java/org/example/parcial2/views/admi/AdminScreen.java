// AdminScreen.java
package org.example.parcial2.views.admi;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.controlller.UserController;
import org.example.parcial2.models.User;
import org.example.parcial2.views.LoginScreen;

public class AdminScreen {

    private final Stage stage;
    private final int userId;
    private final UserController userController;
    private String adminName;

    /** Constructor que recibe solo el Stage (userId se ignora o queda -1). */
    public AdminScreen(Stage stage) {
        this(stage, -1);
    }

    /** Constructor que recibe Stage + userId. */
    public AdminScreen(Stage stage, int userId) {
        this.stage = stage;
        this.userId = userId;
        this.userController = new UserController();
        User currentUser = userController.buscarUsuarioPorId(userId);
        this.adminName = (currentUser != null) ? currentUser.getNombre() : "Usuario #" + userId;

    }

    public void show() {
        Label welcomeLabel = new Label("Bienvenido, Administrador"+ " "+ adminName );
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        // Crear botones para las diferentes secciones administrativas
        Button btnUsuarios = new Button("Administrar Usuarios");
        btnUsuarios.setPrefSize(200, 40);
        btnUsuarios.setOnAction(e -> new AdminUsuariosScreen(stage, null).show());

        Button btnArtistas = new Button("Administrar Artistas");
        btnArtistas.setPrefSize(200, 40);
        btnArtistas.setOnAction(e -> new AdminArtistasScreen(stage, userId).show());

        Button btnAlbumes = new Button("Administrar Álbumes");
        btnAlbumes.setPrefSize(200, 40);
        btnAlbumes.setOnAction(e -> new AdminAlbumesScreen(stage, userId).show());

        Button btnCanciones = new Button("Administrar Canciones");
        btnCanciones.setPrefSize(200, 40);
        btnCanciones.setOnAction(e -> new AdminCancionesScreen(stage, userId).show());

        Button btnGeneros = new Button("Administrar Géneros");
        btnGeneros.setPrefSize(200, 40);
        btnGeneros.setOnAction(e -> new AdminGenerosScreen(stage, userId).show());

        Button btnVentas = new Button("Total Canciones Vendidas");
        btnVentas.setPrefSize(200, 40);
        btnVentas.setOnAction(e -> new AdminTotalCancionesVendidas(stage, userId).show());

        Button logoutButton = new Button("Cerrar Sesión");
        logoutButton.setPrefSize(200, 40);
        logoutButton.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> {
            stage.setScene(new LoginScreen(stage).getLoginScene());
            stage.setTitle("Login");
        });

        // Organizar botones en un GridPane para mejor distribución
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(20);
        buttonGrid.setVgap(15);
        buttonGrid.setAlignment(Pos.CENTER);

        // Primera columna
        buttonGrid.add(btnUsuarios, 0, 0);
        buttonGrid.add(btnArtistas, 0, 1);
        buttonGrid.add(btnAlbumes, 0, 2);

        // Segunda columna
        buttonGrid.add(btnCanciones, 1, 0);
        buttonGrid.add(btnGeneros, 1, 1);
        buttonGrid.add(btnVentas, 1, 2);

        VBox root = new VBox(25, welcomeLabel, buttonGrid, logoutButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #121212;");

        Scene scene = new Scene(root, 520, 450);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Pantalla de Administrador");
        stage.show();
    }
}