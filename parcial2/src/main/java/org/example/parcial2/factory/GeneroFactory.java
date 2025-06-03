package org.example.parcial2.factory;

import org.example.parcial2.models.Genero;

public class GeneroFactory {
    public static Genero crearGenero(
            String nombreGenero
    ) {
        // idGenero lo genera la BD, así que va como 0
        return new Genero(0, nombreGenero);
    }
}
