package org.example.parcial2.controlller;

import org.example.parcial2.models.Genero;
import org.example.parcial2.repository.GeneroRepository;
import java.util.List;

public class GeneroController {
    private final GeneroRepository generoRepository = new GeneroRepository();

    public List<Genero> listarGeneros() {
        return generoRepository.buscarTodos();
    }

    public Genero buscarGeneroPorId(int id) {
        return generoRepository.buscarPorId(id);
    }

    public void crearGenero(Genero genero) {
        generoRepository.guardar(genero);
    }

    public void actualizarGenero(Genero genero) {
        generoRepository.actualizar(genero);
    }

    public void eliminarGenero(int id) {
        generoRepository.eliminar(id);
    }
}
