package org.example.parcial2.models;

public class Interpretacion {
    private int idCancion;
    private int idArtista;

    public Interpretacion() {}

    public Interpretacion(int idCancion, int idArtista) {
        this.idCancion = idCancion;
        this.idArtista = idArtista;
    }

    public int getIdCancion() { return idCancion; }
    public void setIdCancion(int idCancion) { this.idCancion = idCancion; }

    public int getIdArtista() { return idArtista; }
    public void setIdArtista(int idArtista) { this.idArtista = idArtista; }
}
