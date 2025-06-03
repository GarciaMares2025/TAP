package org.example.parcial2.repository;

import org.example.parcial2.models.Genero;
import org.example.parcial2.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GeneroRepository implements Repository<Genero> {

    @Override
    public Genero buscarPorId(int idGenero) {
        String sql = "SELECT * FROM Genero WHERE idGenero = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idGenero);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToGenero(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Genero> buscarTodos() {
        List<Genero> lista = new ArrayList<>();
        String sql = "SELECT * FROM Genero";
        try (Connection conn = Database.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRowToGenero(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void guardar(Genero genero) {
        String sql = "INSERT INTO Genero (nombreGenero) VALUES (?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, genero.getNombreGenero());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Genero genero) {
        String sql = "UPDATE Genero SET nombreGenero = ? WHERE idGenero = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, genero.getNombreGenero());
            stmt.setInt(2, genero.getIdGenero());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int idGenero) {
        String sql = "DELETE FROM Genero WHERE idGenero = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idGenero);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------
    // Método utilitario para mapear un ResultSet a Genero
    // ---------------------------------------------------
    private Genero mapRowToGenero(ResultSet rs) throws SQLException {
        return new Genero(
                rs.getInt("idGenero"),
                rs.getString("nombreGenero")
        );
    }
}
