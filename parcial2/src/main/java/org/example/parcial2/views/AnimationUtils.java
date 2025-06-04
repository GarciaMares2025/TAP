package org.example.parcial2.views;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

/**
 * Clase utilitaria para animaciones en JavaFX.
 */
public class AnimationUtils {

    /**
     * Aplica una animación de escala a un botón para hacerlo "rebotar" visualmente.
     * @param button El botón a animar.
     */
    public static void animateButton(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setFromX(1.0);
        scaleTransition.setToX(1.2);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToY(1.2);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }
}
