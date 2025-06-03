package org.example.parcial2;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.parcial2.views.LoginScreen;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        // En lugar de primaryStage.setScene(LoginScreen.getLoginScene());
        // Simplemente:
        LoginScreen loginScreen = new LoginScreen(primaryStage);
        loginScreen.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
