package org.example.parcial2.factory;

import org.example.parcial2.models.Artista;

public class ArtistaFactory {
    public static Artista crearArtista(
            String nombreArtista,
            String nacionalidad
    ) {
        // idArtista lo genera la BD, así que va como 0
        return new Artista(0, nombreArtista, nacionalidad);
    }
}
