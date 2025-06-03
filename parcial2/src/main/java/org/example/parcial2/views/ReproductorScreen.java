package org.example.parcial2.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.example.parcial2.models.Cancion;
import org.example.parcial2.repository.CancionRepository;
import org.example.parcial2.views.users.UserReproducirScreen;

public class ReproductorScreen {

    private final Stage stage;
    private final int idCancion;
    private final int userId;        // ← Nuevo campo para saber a qué usuario regresar
    private MediaPlayer mediaPlayer;

    /**
     * Constructor: recibe Stage, el id de la canción a reproducir, y el userId para regresar.
     */
    public ReproductorScreen(Stage stage, int idCancion, int userId) {
        this.stage     = stage;
        this.idCancion = idCancion;
        this.userId    = userId;
    }

    /**
     * Construye y muestra la interfaz de reproducción de la canción.
     */
    public void show() {
        // 1) Obtener datos de la canción desde repositorio
        CancionRepository repo = new CancionRepository();
        Cancion c = repo.buscarPorId(idCancion);
        if (c == null) {
            new Alert(Alert.AlertType.ERROR, "No se encontró la canción con ID " + idCancion, ButtonType.OK)
                    .showAndWait();
            return;
        }

        String rutaAudio = c.getRutaAudio();
        if (rutaAudio == null || rutaAudio.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Esta canción no tiene ruta de audio asignada.", ButtonType.OK)
                    .showAndWait();
            return;
        }

        // 2) Construir Media y MediaPlayer
        Media media;
        try {
            media = new Media(rutaAudio);
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al cargar audio:\n" + ex.getMessage(), ButtonType.OK)
                    .showAndWait();
            return;
        }

        mediaPlayer = new MediaPlayer(media);

        // 3) Etiqueta con el título de la canción
        Label titleLabel = new Label("Reproduciendo: " + c.getTitulo());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

        // 4) Botones de control: Play / Pause / Stop
        Button playBtn  = new Button("Play");
        Button pauseBtn = new Button("Pause");
        Button stopBtn  = new Button("Stop");

        playBtn.setOnAction(e -> mediaPlayer.play());
        pauseBtn.setOnAction(e -> mediaPlayer.pause());
        stopBtn.setOnAction(e -> mediaPlayer.stop());

        // 5) Slider de progreso
        Slider progressSlider = new Slider();
        progressSlider.setMin(0);
        progressSlider.setMax(100);
        progressSlider.setPrefWidth(400);

        Label currentTimeLabel = new Label("00:00");
        currentTimeLabel.setStyle("-fx-text-fill: white;");
        Label totalTimeLabel   = new Label("00:00");
        totalTimeLabel.setStyle("-fx-text-fill: white;");

        // Cuando el media está listo, ajusta el máximo del Slider
        mediaPlayer.setOnReady(() -> {
            double totalSec = media.getDuration().toSeconds();
            progressSlider.setMax(totalSec);
            totalTimeLabel.setText(formatTime((long) totalSec));
        });

        // Mientras se reproduce, actualizamos Slider y etiqueta de tiempo
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            double segAct = newTime.toSeconds();
            if (!progressSlider.isValueChanging()) {
                progressSlider.setValue(segAct);
                currentTimeLabel.setText(formatTime((long) segAct));
            }
        });

        // Si el usuario arrastra el Slider, hacemos seek
        progressSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) {
                mediaPlayer.seek(javafx.util.Duration.seconds(progressSlider.getValue()));
            }
        });
        progressSlider.setOnMouseReleased(ev ->
                mediaPlayer.seek(javafx.util.Duration.seconds(progressSlider.getValue()))
        );

        // Cuando la canción termina, detenemos y reseteamos
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.stop());

        // 6) Botón “Regresar” para volver a la lista de canciones compradas
        Button backButton = new Button("Regresar");
        backButton.setOnAction(e -> {
            // Detenemos la reproducción antes de cambiar de pantalla
            mediaPlayer.stop();
            new UserReproducirScreen(stage, userId).show();
        });

        // 7) Agrupar controles en HBox
        HBox controlsBox = new HBox(10,
                playBtn, pauseBtn, stopBtn,
                currentTimeLabel, progressSlider, totalTimeLabel
        );
        controlsBox.setAlignment(Pos.CENTER);

        // 8) Construir el VBox principal (incluye backButton abajo)
        VBox root = new VBox(15,
                titleLabel,
                controlsBox,
                backButton
        );
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #121212;");

        Scene scene = new Scene(root, 700, 180);
        scene.getStylesheets().add(getClass().getResource("/Logina.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Reproductor - " + c.getTitulo());
        stage.show();

        // 9) Iniciar reproducción automáticamente
        mediaPlayer.play();
    }

    /**
     * Formatea segundos en "mm:ss" o "hh:mm:ss" según corresponda.
     */
    private String formatTime(long totalSec) {
        long horas   = totalSec / 3600;
        long minutos = (totalSec % 3600) / 60;
        long segundos= totalSec % 60;
        if (horas > 0) {
            return String.format("%02d:%02d:%02d", horas, minutos, segundos);
        } else {
            return String.format("%02d:%02d", minutos, segundos);
        }
    }
}
