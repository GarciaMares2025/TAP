package org.example.parcial2.repository;

import org.example.parcial2.models.Album;
import org.example.parcial2.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumRepository implements Repository<Album> {

    @Override
    public Album buscarPorId(int idAlbum) {
        String sql = "SELECT * FROM Album WHERE idAlbum = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlbum);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToAlbum(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Album> buscarTodos() {
        List<Album> lista = new ArrayList<>();
        String sql = "SELECT * FROM Album";
        try (Connection conn = Database.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRowToAlbum(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void guardar(Album album) {
        String sql = "INSERT INTO Album (idArtista, nombreAlbum, fechaLanzamiento, albumImage) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, album.getIdArtista());
            stmt.setString(2, album.getNombreAlbum());
            stmt.setDate(3, album.getFechaLanzamiento());
            stmt.setBytes(4, album.getAlbumImage());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Album album) {
        String sql = "UPDATE Album SET idArtista = ?, nombreAlbum = ?, fechaLanzamiento = ?, albumImage = ? WHERE idAlbum = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, album.getIdArtista());
            stmt.setString(2, album.getNombreAlbum());
            stmt.setDate(3, album.getFechaLanzamiento());
            stmt.setBytes(4, album.getAlbumImage());
            stmt.setInt(5, album.getIdAlbum());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int idAlbum) {
        String sql = "DELETE FROM Album WHERE idAlbum = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlbum);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------
    // Método utilitario para mapear un ResultSet a Album
    // ---------------------------------------------------
    private Album mapRowToAlbum(ResultSet rs) throws SQLException {
        return new Album(
                rs.getInt("idAlbum"),
                rs.getInt("idArtista"),
                rs.getString("nombreAlbum"),
                rs.getDate("fechaLanzamiento"),
                rs.getBytes("albumImage")
        );
    }
}
