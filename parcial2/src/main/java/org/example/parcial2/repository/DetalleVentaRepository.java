package org.example.parcial2.repository;

import org.example.parcial2.models.DetalleVenta;
import org.example.parcial2.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaRepository implements Repository<DetalleVenta> {

    @Override
    public DetalleVenta buscarPorId(int idDetalle) {
        String sql = "SELECT * FROM Detalle_Venta WHERE idDetalle = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDetalle);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToDetalleVenta(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DetalleVenta> buscarTodos() {
        List<DetalleVenta> lista = new ArrayList<>();
        String sql = "SELECT * FROM Detalle_Venta";
        try (Connection conn = Database.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRowToDetalleVenta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void guardar(DetalleVenta d) {
        String sql = "INSERT INTO Detalle_Venta (idVenta, idCancion, precio) VALUES (?, ?, ?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, d.getIdVenta());
            stmt.setInt(2, d.getIdCancion());
            stmt.setBigDecimal(3, d.getPrecio());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(DetalleVenta d) {
        String sql = "UPDATE Detalle_Venta SET idVenta = ?, idCancion = ?, precio = ? WHERE idDetalle = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, d.getIdVenta());
            stmt.setInt(2, d.getIdCancion());
            stmt.setBigDecimal(3, d.getPrecio());
            stmt.setInt(4, d.getIdDetalle());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int idDetalle) {
        String sql = "DELETE FROM Detalle_Venta WHERE idDetalle = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDetalle);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------
    // Método utilitario para mapear un ResultSet a DetalleVenta
    // ---------------------------------------------------
    private DetalleVenta mapRowToDetalleVenta(ResultSet rs) throws SQLException {
        return new DetalleVenta(
                rs.getInt("idDetalle"),
                rs.getInt("idVenta"),
                rs.getInt("idCancion"),
                rs.getBigDecimal("precio")
        );
    }
}
