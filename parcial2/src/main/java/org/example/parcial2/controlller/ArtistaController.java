package org.example.parcial2.controlller;

import org.example.parcial2.models.Artista;
import org.example.parcial2.repository.ArtistaRepository;
import java.util.List;

public class ArtistaController {
    private final ArtistaRepository artistaRepository = new ArtistaRepository();

    public List<Artista> listarArtistas() {
        return artistaRepository.buscarTodos();
    }

    public Artista buscarArtistaPorId(int id) {
        return artistaRepository.buscarPorId(id);
    }

    public void crearArtista(Artista artista) {
        artistaRepository.guardar(artista);
    }

    public void actualizarArtista(Artista artista) {
        artistaRepository.actualizar(artista);
    }

    public void eliminarArtista(int id) {
        artistaRepository.eliminar(id);
    }
}
