package org.example.parcial2.controlller;

import org.example.parcial2.models.DetalleVenta;
import org.example.parcial2.repository.DetalleVentaRepository;
import java.util.List;

public class DetalleVentaController {
    private final DetalleVentaRepository detalleVentaRepository = new DetalleVentaRepository();

    public List<DetalleVenta> listarDetallesVenta() {
        return detalleVentaRepository.buscarTodos();
    }

    public DetalleVenta buscarDetallePorId(int id) {
        return detalleVentaRepository.buscarPorId(id);
    }

    public void crearDetalleVenta(DetalleVenta detalle) {
        detalleVentaRepository.guardar(detalle);
    }

    public void actualizarDetalleVenta(DetalleVenta detalle) {
        detalleVentaRepository.actualizar(detalle);
    }

    public void eliminarDetalleVenta(int id) {
        detalleVentaRepository.eliminar(id);
    }
}
