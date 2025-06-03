package org.example.parcial2.models;

import java.sql.Time;

public class Cancion {
    private int idCancion;
    private String titulo;
    private Time duracion;
    private int idGenero;
    private String rutaAudio;  // ← Nuevo campo

    // Constructor completo (incluye rutaAudio)
    public Cancion(int idCancion, String titulo, Time duracion, int idGenero, String rutaAudio) {
        this.idCancion = idCancion;
        this.titulo    = titulo;
        this.duracion  = duracion;
        this.idGenero  = idGenero;
        this.rutaAudio = rutaAudio;
    }

    // Constructor sin id (para insertar nuevas filas)
    public Cancion(String titulo, Time duracion, int idGenero, String rutaAudio) {
        this.titulo    = titulo;
        this.duracion  = duracion;
        this.idGenero  = idGenero;
        this.rutaAudio = rutaAudio;
    }

    // Getters y setters

    public int getIdCancion() {
        return idCancion;
    }

    public void setIdCancion(int idCancion) {
        this.idCancion = idCancion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Time getDuracion() {
        return duracion;
    }

    public void setDuracion(Time duracion) {
        this.duracion = duracion;
    }

    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public String getRutaAudio() {
        return rutaAudio;
    }

    public void setRutaAudio(String rutaAudio) {
        this.rutaAudio = rutaAudio;
    }

    // (Opcional) toString, equals, hashCode...
}
