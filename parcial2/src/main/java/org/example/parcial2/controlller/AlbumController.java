package org.example.parcial2.controlller;

import org.example.parcial2.models.Album;
import org.example.parcial2.repository.AlbumRepository;
import java.util.List;

public class AlbumController {
    private final AlbumRepository albumRepository = new AlbumRepository();

    public List<Album> listarAlbums() {
        return albumRepository.buscarTodos();
    }

    public Album buscarAlbumPorId(int id) {
        return albumRepository.buscarPorId(id);
    }

    public void crearAlbum(Album album) {
        albumRepository.guardar(album);
    }

    public void actualizarAlbum(Album album) {
        albumRepository.actualizar(album);
    }

    public void eliminarAlbum(int id) {
        albumRepository.eliminar(id);
    }
}
