package org.example.parcial2.models;

import java.sql.Date;

public class Album {
    private int idAlbum;
    private int idArtista;
    private String nombreAlbum;
    private Date fechaLanzamiento;
    private byte[] albumImage; // Para imagen tipo BLOB

    public Album() {}

    public Album(int idAlbum, int idArtista, String nombreAlbum, Date fechaLanzamiento, byte[] albumImage) {
        this.idAlbum = idAlbum;
        this.idArtista = idArtista;
        this.nombreAlbum = nombreAlbum;
        this.fechaLanzamiento = fechaLanzamiento;
        this.albumImage = albumImage;
    }

    public int getIdAlbum() { return idAlbum; }
    public void setIdAlbum(int idAlbum) { this.idAlbum = idAlbum; }

    public int getIdArtista() { return idArtista; }
    public void setIdArtista(int idArtista) { this.idArtista = idArtista; }

    public String getNombreAlbum() { return nombreAlbum; }
    public void setNombreAlbum(String nombreAlbum) { this.nombreAlbum = nombreAlbum; }

    public Date getFechaLanzamiento() { return fechaLanzamiento; }
    public void setFechaLanzamiento(Date fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }

    public byte[] getAlbumImage() { return albumImage; }
    public void setAlbumImage(byte[] albumImage) { this.albumImage = albumImage; }
}
