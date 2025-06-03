package org.example.parcial2.repository;

import org.example.parcial2.models.Venta;
import org.example.parcial2.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaRepository implements Repository<Venta> {

    @Override
    public Venta buscarPorId(int idVenta) {
        String sql = "SELECT * FROM Venta WHERE idVenta = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToVenta(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Venta> buscarTodos() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM Venta";
        try (Connection conn = Database.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRowToVenta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void guardar(Venta venta) {
        String sql = "INSERT INTO Venta (fechaVenta, totalVenta, idUser) VALUES (?, ?, ?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, venta.getFechaVenta());
            stmt.setBigDecimal(2, venta.getTotalVenta());
            stmt.setInt(3, venta.getIdUser());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Venta venta) {
        String sql = "UPDATE Venta SET fechaVenta = ?, totalVenta = ?, idUser = ? WHERE idVenta = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, venta.getFechaVenta());
            stmt.setBigDecimal(2, venta.getTotalVenta());
            stmt.setInt(3, venta.getIdUser());
            stmt.setInt(4, venta.getIdVenta());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int idVenta) {
        String sql = "DELETE FROM Venta WHERE idVenta = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------
    // Método utilitario para mapear un ResultSet a Venta
    // ---------------------------------------------------
    private Venta mapRowToVenta(ResultSet rs) throws SQLException {
        return new Venta(
                rs.getInt("idVenta"),
                rs.getTimestamp("fechaVenta"),
                rs.getBigDecimal("totalVenta"),
                rs.getInt("idUser")
        );
    }
}
