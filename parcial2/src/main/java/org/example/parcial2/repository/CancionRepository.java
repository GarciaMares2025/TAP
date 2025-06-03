package org.example.parcial2.repository;

import org.example.parcial2.models.Cancion;
import org.example.parcial2.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CancionRepository implements Repository<Cancion> {
    @Override
    public List<Cancion> buscarTodos() {
        List<Cancion> lista = new ArrayList<>();
        String sql = "SELECT idCancion, titulo, duracion, idGenero, rutaAudio FROM Cancion";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next()) {
                Cancion c = new Cancion(
                        rs.getInt("idCancion"),
                        rs.getString("titulo"),
                        rs.getTime("duracion"),
                        rs.getInt("idGenero"),
                        rs.getString("rutaAudio")
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Cancion buscarPorId(int idCancion) {
        String sql = "SELECT idCancion, titulo, duracion, idGenero, rutaAudio FROM Cancion WHERE idCancion = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, idCancion);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cancion(
                            rs.getInt("idCancion"),
                            rs.getString("titulo"),
                            rs.getTime("duracion"),
                            rs.getInt("idGenero"),
                            rs.getString("rutaAudio")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void guardar(Cancion c) {
        String sql = "INSERT INTO Cancion (titulo, duracion, idGenero, rutaAudio) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, c.getTitulo());
            stmt.setTime(2, c.getDuracion());
            stmt.setInt(3, c.getIdGenero());
            stmt.setString(4, c.getRutaAudio());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setIdCancion(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Cancion c) {
        String sql = "UPDATE Cancion SET titulo=?, duracion=?, idGenero=?, rutaAudio=? WHERE idCancion=?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, c.getTitulo());
            stmt.setTime(2, c.getDuracion());
            stmt.setInt(3, c.getIdGenero());
            stmt.setString(4, c.getRutaAudio());
            stmt.setInt(5, c.getIdCancion());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int idCancion) {
        String sql = "DELETE FROM Cancion WHERE idCancion = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, idCancion);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
