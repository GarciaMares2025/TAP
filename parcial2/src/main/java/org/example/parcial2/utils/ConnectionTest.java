package org.example.parcial2.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionTest {
    public static void main(String[] args) {
        // 1) Ajusta estos valores según tu configuración real de MySQL:
        String url      = "jdbc:mysql://localhos---------";  // Base de datos: spotify
        String user     = "-------";                                 // Usuario de MySQL
        String password = "-------";                              // Contraseña real de MySQL

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
