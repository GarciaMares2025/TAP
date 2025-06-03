package org.example.parcial2.controlller;

import org.example.parcial2.models.Venta;
import org.example.parcial2.repository.VentaRepository;

import java.util.List;

public class VentaController {
    private final VentaRepository ventaRepository = new VentaRepository();

    public List<Venta> listarVentas() {
        return ventaRepository.buscarTodos();
    }

    public Venta buscarVentaPorId(int id) {
        return ventaRepository.buscarPorId(id);
    }

    public void crearVenta(Venta venta) {
        ventaRepository.guardar(venta);
    }

    public void actualizarVenta(Venta venta) {
        ventaRepository.actualizar(venta);
    }

    public void eliminarVenta(int id) {
        ventaRepository.eliminar(id);
    }
}
