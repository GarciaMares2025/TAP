package org.example.parcial2;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.parcial2.views.LoginScreen;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Inicializa la pantalla de login
        LoginScreen loginScreen = new LoginScreen(primaryStage);
        loginScreen.show(); // Ahora cada Screen tiene su propio show()
    }

    public static void main(String[] args) {
        launch(args);
    }
}
