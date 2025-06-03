package org.example.parcial2.factory;

import org.example.parcial2.models.Cancion;

import java.sql.Time;

public class CancionFactory {
    public static Cancion crearCancion(
            String titulo,
            Time duracion,
            int idGenero
    ) {
        // idCancion lo genera la BD, así que lo inicializamos en 0.
        // Para rutaAudio, ponemos cadena vacía por defecto.
        return new Cancion(0, titulo, duracion, idGenero, "");
    }
}
