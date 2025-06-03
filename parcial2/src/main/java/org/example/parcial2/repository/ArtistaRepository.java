package org.example.parcial2.repository;

import org.example.parcial2.models.Artista;
import org.example.parcial2.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistaRepository implements Repository<Artista> {

    @Override
    public Artista buscarPorId(int idArtista) {
        String sql = "SELECT * FROM Artista WHERE idArtista = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idArtista);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToArtista(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Artista> buscarTodos() {
        List<Artista> lista = new ArrayList<>();
        String sql = "SELECT * FROM Artista";
        try (Connection conn = Database.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRowToArtista(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void guardar(Artista artista) {
        String sql = "INSERT INTO Artista (nombreArtista, nacionalidad) VALUES (?, ?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artista.getNombreArtista());
            stmt.setString(2, artista.getNacionalidad());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Artista artista) {
        String sql = "UPDATE Artista SET nombreArtista = ?, nacionalidad = ? WHERE idArtista = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artista.getNombreArtista());
            stmt.setString(2, artista.getNacionalidad());
            stmt.setInt(3, artista.getIdArtista());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int idArtista) {
        String sql = "DELETE FROM Artista WHERE idArtista = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idArtista);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------
    // Método utilitario para mapear un ResultSet a Artista
    // ---------------------------------------------------
    private Artista mapRowToArtista(ResultSet rs) throws SQLException {
        return new Artista(
                rs.getInt("idArtista"),
                rs.getString("nombreArtista"),
                rs.getString("nacionalidad")
        );
    }
}
