package org.example.parcial2.repository;

import org.example.parcial2.models.User;
import org.example.parcial2.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements Repository<User> {

    @Override
    public User buscarPorId(int idUser) {
        String sql = "SELECT * FROM Usuarios WHERE idUser = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Busca un usuario por su username ("User" en la tabla).
     * Útil para validar existencia en login/registro.
     */
    public User buscarPorUsername(String username) {
        String sql = "SELECT * FROM Usuarios WHERE User = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> buscarTodos() {
        List<User> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";
        try (Connection conn = Database.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void guardar(User u) {
        String sql = "INSERT INTO Usuarios (nombre, telUser, emailUser, User, password, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getNombre());
            stmt.setString(2, u.getTelUser());
            stmt.setString(3, u.getEmailUser());
            stmt.setString(4, u.getUser());
            stmt.setString(5, u.getPassword());
            stmt.setString(6, u.getRole());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(User u) {
        String sql = "UPDATE Usuarios SET nombre = ?, telUser = ?, emailUser = ?, User = ?, password = ?, role = ? WHERE idUser = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getNombre());
            stmt.setString(2, u.getTelUser());
            stmt.setString(3, u.getEmailUser());
            stmt.setString(4, u.getUser());
            stmt.setString(5, u.getPassword());
            stmt.setString(6, u.getRole());
            stmt.setInt(7, u.getIdUser());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int idUser) {
        String sql = "DELETE FROM Usuarios WHERE idUser = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------
    // Método utilitario para mapear un ResultSet a User
    // ---------------------------------------------------
    private User mapRowToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("idUser"),
                rs.getString("nombre"),
                rs.getString("telUser"),
                rs.getString("emailUser"),
                rs.getString("User"),
                rs.getString("password"),
                rs.getString("role")
        );
    }
}
