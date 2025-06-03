package org.example.parcial2.utils;

// Guarda este archivo como ConnectionTest.java en cualquier carpeta fuera de tu proyecto.
// Solo depende de la librería del conector MySQL (mysql-connector-j).

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionTest {
    public static void main(String[] args) {
        // 1) Ajusta estos valores según tu configuración real de MySQL:
        String url      = "jdbc:mysql://localhost:3306/spotify";  // Base de datos: spotify
        String user     = "root";                                 // Usuario de MySQL
        String password = "Hola123";                              // Contraseña real de MySQL

        Connection conn = null;
        try {
            // 2) Intenta abrir la conexión:
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión exitosa a MySQL.");
            } else {
                System.out.println("❌ No se abrió la conexión.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con MySQL:");
            e.printStackTrace();
        } finally {
            // 3) Cierra la conexión (si fue abierta):
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) { }
            }
        }
    }
}
