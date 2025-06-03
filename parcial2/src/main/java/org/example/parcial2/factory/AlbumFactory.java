package org.example.parcial2.factory;

import org.example.parcial2.models.Album;

import java.sql.Date;

public class AlbumFactory {
    public static Album crearAlbum(
            int idArtista,
            String nombreAlbum,
            Date fechaLanzamiento,
            byte[] albumImage
    ) {
        // idAlbum lo genera la BD, así que va como 0
        return new Album(0, idArtista, nombreAlbum, fechaLanzamiento, albumImage);
    }
}
