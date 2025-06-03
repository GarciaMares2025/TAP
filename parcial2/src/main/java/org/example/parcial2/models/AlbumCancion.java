package org.example.parcial2.models;

public class AlbumCancion {
    private int idAlbum;
    private int idCancion;

    public AlbumCancion() {}

    public AlbumCancion(int idAlbum, int idCancion) {
        this.idAlbum = idAlbum;
        this.idCancion = idCancion;
    }

    public int getIdAlbum() { return idAlbum; }
    public void setIdAlbum(int idAlbum) { this.idAlbum = idAlbum; }

    public int getIdCancion() { return idCancion; }
    public void setIdCancion(int idCancion) { this.idCancion = idCancion; }
}
