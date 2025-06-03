package org.example.parcial2.controlller;

import org.example.parcial2.models.Cancion;
import org.example.parcial2.repository.CancionRepository;

import java.util.List;

public class CancionController {
    private final CancionRepository cancionRepository = new CancionRepository();

    public List<Cancion> listarCanciones() {
        return cancionRepository.buscarTodos();
    }

    public Cancion buscarCancionPorId(int id) {
        return cancionRepository.buscarPorId(id);
    }

    public void crearCancion(Cancion cancion) {
        cancionRepository.guardar(cancion);
    }

    public void actualizarCancion(Cancion cancion) {
        cancionRepository.actualizar(cancion);
    }

    public void eliminarCancion(int id) {
        cancionRepository.eliminar(id);
    }
}
