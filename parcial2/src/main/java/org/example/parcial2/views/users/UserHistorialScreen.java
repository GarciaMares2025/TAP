package org.example.parcial2.views.users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.parcial2.utils.Database;

/**
 * Pantalla que muestra el historial de compras del usuario.
 * Permite ver el historial completo, detalles de compras específicas y generar reportes PDF.
 */
public class UserHistorialScreen {

    private final Stage stage;
    private final Database db;
    private final TableView<String[]> historialTable;
    private final TableView<String[]> detalleTable; // Tabla para detalles de compra específica
    private final int userId; // ID del usuario para filtrar su historial

    /** Constructor que recibe el Stage y el ID del usuario */
    public UserHistorialScreen(Stage stage, int userId) {
        this.stage = stage;
        this.db = Database.getInstance();
        this.historialTable = new TableView<>();
        this.detalleTable = new TableView<>();
        this.userId = userId;
    }

    public void show() {
        Label label = new Label("Historial de Compras del Usuario #" + userId);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");

        // Configuración de la tabla de historial de compras
        TableColumn<String[], String> fechaCol = new TableColumn<>("Fecha de Compra");
        fechaCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));

        TableColumn<String[], String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));

        TableColumn<String[], String> tipoCol = new TableColumn<>("Tipo de Compra");
        tipoCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[2]));

        historialTable.getColumns().addAll(fechaCol, totalCol, tipoCol);
        historialTable.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white;");
        loadHistorial();

        // Configuración de la tabla de detalles de compra
        TableColumn<String[], String> itemCol = new TableColumn<>("Canción/Álbum");
        itemCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));

        TableColumn<String[], String> precioCol = new TableColumn<>("Precio");
        precioCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));

        detalleTable.getColumns().addAll(itemCol, precioCol);
        detalleTable.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white;");

        // Cargar detalles de compra al seleccionar una entrada en el historial
        historialTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    // Verificar que el array tenga al menos 4 elementos
                    if (newSelection.length >= 4 && newSelection[3] != null && !newSelection[3].trim().isEmpty()) {
                        int idCompra = Integer.parseInt(newSelection[3].trim());
                        loadDetalleCompra(idCompra);
                    } else {
                        System.err.println("ID de compra no disponible o inválido");
                        // Limpiar la tabla de detalles
                        detalleTable.setItems(FXCollections.observableArrayList());
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir ID de compra: " + newSelection[3] + " - " + e.getMessage());
                    // Mostrar mensaje de error al usuario
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("ID de compra inválido");
                    alert.setContentText("No se pueden cargar los detalles de esta compra.");
                    alert.showAndWait();
                }
            }
        });

        // Botón para generar reporte en PDF
        Button generarPDFButton = new Button("Generar Reporte en PDF");
        generarPDFButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        generarPDFButton.setOnAction(e -> generatePDFReport());

        // Botón para regresar a la pantalla anterior
        Button backButton = new Button("Regresar");
        backButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        backButton.setOnAction(e -> {
            // Asumiendo que UserScreen existe en el mismo paquete
            // Si está en otro paquete, ajusta el import
            new UserScreen(stage, userId).show();
        });

        // Layout principal
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                label,
                historialTable,
                new Label("Detalles de la Compra Seleccionada") {{
                    setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                }},
                detalleTable,
                generarPDFButton,
                backButton
        );
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #121212;");

        Scene scene = new Scene(vbox, 700, 600);

        // Descomenta la siguiente línea si tienes el archivo styles.css
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Historial de Compras");
        stage.show();
    }

    /**
     * Carga el historial de compras del usuario desde la base de datos
     */
    private void loadHistorial() {
        try {
            ObservableList<String[]> historial = FXCollections.observableArrayList(db.getHistorialCompras(userId));
            historialTable.setItems(historial);

            if (historial.isEmpty()) {
                // Mostrar mensaje si no hay historial
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sin Historial");
                alert.setHeaderText(null);
                alert.setContentText("No se encontraron compras para este usuario.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.err.println("Error al cargar historial: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar historial");
            alert.setContentText("No se pudo cargar el historial de compras.");
            alert.showAndWait();
        }
    }

    /**
     * Carga los detalles de una compra específica
     * @param idCompra ID de la compra seleccionada
     */
    private void loadDetalleCompra(int idCompra) {
        try {
            ObservableList<String[]> detalle = FXCollections.observableArrayList(db.getDetalleCompra(idCompra));
            detalleTable.setItems(detalle);

            if (detalle.isEmpty()) {
                System.out.println("No se encontraron detalles para la compra ID: " + idCompra);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar detalle de compra: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar detalles");
            alert.setContentText("No se pudieron cargar los detalles de la compra.");
            alert.showAndWait();
        }
    }

    /**
     * Genera un reporte PDF del historial de compras del usuario
     */
    private void generatePDFReport() {
        try {
            boolean success = db.generateHistorialPDF(userId);
            Alert alert = new Alert(
                    success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                    success ? "Reporte generado exitosamente." : "Error al generar el reporte."
            );
            alert.setTitle(success ? "Éxito" : "Error");
            alert.setHeaderText(null);
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Error al generar PDF: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al generar PDF");
            alert.setContentText("No se pudo generar el reporte PDF.");
            alert.showAndWait();
        }
    }
}