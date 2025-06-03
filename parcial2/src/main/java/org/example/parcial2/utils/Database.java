package org.example.parcial2.utils;

import org.example.parcial2.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;




public class Database {
    private static Database instancia;// <-- Singleton instance
    private final String url      = "jdbc:mysql://localhost:3306/spotify";
    private final String user     = "root";
    private final String password = "Hola123";

    private Database() {
        try {
            // (Opcionalmente) cargar el driver explícitamente; en MySQL moderno no hace falta
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró el driver MySQL JDBC", e);
        }
    }

    // ESTE ES EL METODO QUE TE FALTA:
    public static synchronized Database getInstance() {
        if (instancia == null) {
            instancia = new Database();
        }
        return instancia;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }


    // Métodos para manejo de usuarios
    public boolean authenticateUser(String username, String pass) {
        String sql = "SELECT * FROM Usuarios WHERE User = ? AND password = ?";
        try (Connection conn = getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, pass);
            var rs = stmt.executeQuery();
            return rs.next(); // si hay fila, las credenciales son correctas
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String[]> getPurchasedSongsByUser(int userId) {
        List<String[]> lista = new ArrayList<>();
        String sql = """
            SELECT c.idCancion,
                   c.titulo,
                   c.duracion,
                   c.rutaAudio
            FROM Cancion c
            JOIN Detalle_Venta dv ON c.idCancion = dv.idCancion
            JOIN Venta v ON dv.idVenta = v.idVenta
            WHERE v.idUser = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String idCancion = String.valueOf(rs.getInt("idCancion"));
                    String titulo    = rs.getString("titulo");
                    Time duracion    = rs.getTime("duracion");
                    String durStr    = (duracion != null ? duracion.toString() : "00:00");
                    String rutaAudio = rs.getString("rutaAudio");

                    lista.add(new String[]{ idCancion, titulo, durStr, rutaAudio });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public String getUserRole(String username) {
        String sql = "SELECT role FROM Usuarios WHERE `User` = ?";
        try (Connection conn = getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }


    public boolean isUserExists(String username) {
        String query = "SELECT * FROM Usuarios WHERE User = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createUser(String name, String phone, String email, String username, String password, String role) {
        if (isUserExists(username)) {
            System.out.println("El usuario ya existe.");
            return;
        }

        String query = "INSERT INTO Usuarios (nombre, telUser, emailUser, User, password, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.setString(4, username);
            stmt.setString(5, password);
            stmt.setString(6, role);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM Usuarios";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User(
                        rs.getInt("idUser"),
                        rs.getString("nombre"),
                        rs.getString("telUser"),
                        rs.getString("emailUser"),
                        rs.getString("User"),
                        rs.getString("password"),
                        rs.getString("role")
                );
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean deleteUserById(int id) {
        String query = "DELETE FROM Usuarios WHERE idUser = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Métodos para manejar géneros
    public List<String[]> getAllGenres() {
        List<String[]> genres = new ArrayList<>();
        String query = "SELECT idGenero, nombreGenero FROM Genero";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                genres.add(new String[]{String.valueOf(rs.getInt("idGenero")), rs.getString("nombreGenero")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genres;
    }

    public boolean addGenre(String nombreGenero) {
        String query = "INSERT INTO Genero (nombreGenero) VALUES (?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombreGenero);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteGenreById(int id) {
        String query = "DELETE FROM Genero WHERE idGenero = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Métodos para manejar Albumes
   /* public boolean addAlbum(String nombreAlbum, String fechaLanzamiento) {
        String query = "INSERT INTO Album (nombreAlbum, fechaLanzamiento) VALUES (?, ?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombreAlbum);
            stmt.setString(2, fechaLanzamiento);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }*/
    public boolean addAlbum(String nombreAlbum, String fechaLanzamiento, File imageFile) {
        String query = "INSERT INTO Album (nombreAlbum, fechaLanzamiento, albumImage) VALUES (?, ?, ?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nombreAlbum);
            stmt.setString(2, fechaLanzamiento);

            if (imageFile != null) {
                byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                stmt.setBytes(3, imageBytes);
            } else {
                stmt.setNull(3, java.sql.Types.BLOB);
            }

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAlbumById(int id) {
        String query = "DELETE FROM Album WHERE idAlbum = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] getAlbumImageById(int albumId) {
        String query = "SELECT albumImage FROM Album WHERE idAlbum = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, albumId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("albumImage");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<String[]> getAllAlbumsWithDetails() {
        List<String[]> albums = new ArrayList<>();
        String query = "SELECT idAlbum, nombreAlbum, fechaLanzamiento FROM Album";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                albums.add(new String[]{
                        String.valueOf(rs.getInt("idAlbum")),
                        rs.getString("nombreAlbum"),
                        rs.getString("fechaLanzamiento")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return albums;
    }

    public List<String[]> getSongsByAlbumId(int albumId) {
        List<String[]> songs = new ArrayList<>();
        String query = """
                    SELECT c.idCancion, c.titulo, c.duracion 
                    FROM Cancion c
                    JOIN Album_Cancion ac ON c.idCancion = ac.idCancion
                    WHERE ac.idAlbum = ?
                """;
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, albumId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                songs.add(new String[]{
                        String.valueOf(rs.getInt("idCancion")),
                        rs.getString("titulo"),
                        rs.getString("duracion")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

    // Métodos para manejar artistas
    public List<String[]> getAllArtists() {
        List<String[]> artists = new ArrayList<>();
        String query = "SELECT idArtista, nombreArtista FROM Artista";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                artists.add(new String[]{String.valueOf(rs.getInt("idArtista")), rs.getString("nombreArtista")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return artists;
    }

    public List<String[]> getAllArtistsWithDetails() {
        List<String[]> artists = new ArrayList<>();
        String query = "SELECT idArtista, nombreArtista, nacionalidad FROM Artista";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                artists.add(new String[]{
                        String.valueOf(rs.getInt("idArtista")),
                        rs.getString("nombreArtista"),
                        rs.getString("nacionalidad")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return artists;
    }


    public boolean addArtist(String nombreArtista, String nacionalidad) {
        String query = "INSERT INTO Artista (nombreArtista, nacionalidad) VALUES (?, ?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombreArtista);
            stmt.setString(2, nacionalidad);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteArtistById(int id) {
        String query = "DELETE FROM Artista WHERE idArtista = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Métodos para manejar álbumes
    public List<String[]> getAllAlbums() {
        List<String[]> albums = new ArrayList<>();
        String query = "SELECT idAlbum, nombreAlbum FROM Album";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                albums.add(new String[]{String.valueOf(rs.getInt("idAlbum")), rs.getString("nombreAlbum")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return albums;
    }

    // Métodos para manejar canciones y relaciones usando Interpretacion y Album_Cancion
    public boolean addSong(String titulo, String duracion, int generoId, int artistaId, int albumId) {
        try (Connection conn = Database.getInstance().getConnection()) {
            String songQuery = "INSERT INTO Cancion (titulo, duracion, idGenero) VALUES (?, ?, ?)";
            try (PreparedStatement songStmt = conn.prepareStatement(songQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                songStmt.setString(1, titulo);
                songStmt.setString(2, duracion);  // duracion como String en formato "HH:MM:SS"
                songStmt.setInt(3, generoId);
                songStmt.executeUpdate();

                ResultSet generatedKeys = songStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int cancionId = generatedKeys.getInt(1);

                    // Relacionar canción con artista usando Interpretacion
                    String artistRelationQuery = "INSERT INTO Interpretacion (idCancion, idArtista) VALUES (?, ?)";
                    try (PreparedStatement artistStmt = conn.prepareStatement(artistRelationQuery)) {
                        artistStmt.setInt(1, cancionId);
                        artistStmt.setInt(2, artistaId);
                        artistStmt.executeUpdate();
                    }

                    // Relacionar canción con álbum usando Album_Cancion
                    String albumRelationQuery = "INSERT INTO Album_Cancion (idCancion, idAlbum) VALUES (?, ?)";
                    try (PreparedStatement albumStmt = conn.prepareStatement(albumRelationQuery)) {
                        albumStmt.setInt(1, cancionId);
                        albumStmt.setInt(2, albumId);
                        albumStmt.executeUpdate();
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }




    public List<String[]> getAllSongsWithDetails() {
        List<String[]> songs = new ArrayList<>();
        String query = """
                SELECT c.idCancion, c.titulo, c.duracion, g.nombreGenero, a.nombreArtista, al.nombreAlbum
                FROM Cancion c
                JOIN Genero g ON c.idGenero = g.idGenero
                JOIN Interpretacion i ON c.idCancion = i.idCancion
                JOIN Artista a ON i.idArtista = a.idArtista
                JOIN Album_Cancion ac ON c.idCancion = ac.idCancion
                JOIN Album al ON ac.idAlbum = al.idAlbum
                """;
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                songs.add(new String[]{
                        String.valueOf(rs.getInt("idCancion")),
                        rs.getString("titulo"),
                        rs.getString("duracion"),
                        rs.getString("nombreGenero"),
                        rs.getString("nombreArtista"),
                        rs.getString("nombreAlbum")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }


    //***** seccion para Usuarios  *****

    //Comprar Canciones
    public List<String[]> getAvailableSongs() {
        List<String[]> songs = new ArrayList<>();
        String query = "SELECT c.idCancion, c.titulo, c.duracion, g.nombreGenero FROM Cancion c JOIN Genero g ON c.idGenero = g.idGenero";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                songs.add(new String[]{
                        String.valueOf(rs.getInt("idCancion")),
                        rs.getString("titulo"),
                        rs.getString("duracion"),
                        rs.getString("nombreGenero")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

    public List<String[]> getAvailableSongsWithAlbum() {
        List<String[]> songs = new ArrayList<>();
        String query = """
                    SELECT c.idCancion, c.titulo, c.duracion, a.nombreAlbum
                    FROM Cancion c
                    JOIN Album_Cancion ac ON c.idCancion = ac.idCancion
                    JOIN Album a ON ac.idAlbum = a.idAlbum
                """;
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                songs.add(new String[]{
                        String.valueOf(rs.getInt("idCancion")),
                        rs.getString("titulo"),
                        rs.getString("duracion"),
                        rs.getString("nombreAlbum")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

    public List<String[]> getAvailableAlbumsWithImages() {
        List<String[]> albums = new ArrayList<>();
        String query = "SELECT idAlbum, nombreAlbum, fechaLanzamiento FROM Album";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                albums.add(new String[]{
                        String.valueOf(rs.getInt("idAlbum")),
                        rs.getString("nombreAlbum"),
                        rs.getString("fechaLanzamiento")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return albums;
    }

    public List<String[]> getAvailableSongsWithAlbumAndArtist() {
        List<String[]> songs = new ArrayList<>();
        String query = """
                    SELECT c.idCancion, c.titulo, c.duracion, a.nombreAlbum, ar.nombreArtista
                    FROM Cancion c
                    JOIN Album_Cancion ac ON c.idCancion = ac.idCancion
                    JOIN Album a ON ac.idAlbum = a.idAlbum
                    JOIN Interpretacion i ON c.idCancion = i.idCancion
                    JOIN Artista ar ON i.idArtista = ar.idArtista
                """;
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                songs.add(new String[]{
                        String.valueOf(rs.getInt("idCancion")),
                        rs.getString("titulo"),
                        rs.getString("duracion"),
                        rs.getString("nombreAlbum"),
                        rs.getString("nombreArtista")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

    // manejar Albumes
    public byte[] getAlbumImage(int albumId) {
        String query = "SELECT albumImage FROM Album WHERE idAlbum = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, albumId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("albumImage");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean registerPurchase(List<String[]> carrito) {
        String ventaQuery = "INSERT INTO Venta (fechaVenta, totalVenta, idUser) VALUES (NOW(), ?, ?)";
        String detalleQuery = "INSERT INTO Detalle_Venta (idVenta, idCancion, precio) VALUES (?, ?, ?)";
        try (Connection conn = Database.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            // Calcular total y registrar la venta
            double total = carrito.size() * 10.00; // Precio ficticio para cada canción
            PreparedStatement ventaStmt = conn.prepareStatement(ventaQuery, Statement.RETURN_GENERATED_KEYS);
            ventaStmt.setDouble(1, total);
            ventaStmt.setInt(2, 1); // ID de usuario temporal
            ventaStmt.executeUpdate();
            ResultSet ventaRs = ventaStmt.getGeneratedKeys();
            ventaRs.next();
            int idVenta = ventaRs.getInt(1);

            // Registrar detalles de la venta
            PreparedStatement detalleStmt = conn.prepareStatement(detalleQuery);
            for (String[] song : carrito) {
                detalleStmt.setInt(1, idVenta);
                detalleStmt.setInt(2, Integer.parseInt(song[0]));
                detalleStmt.setDouble(3, 10.00); // Precio ficticio
                detalleStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<String[]> getAvailableAlbums() {
        List<String[]> albums = new ArrayList<>();
        String query = "SELECT idAlbum, nombreAlbum, fechaLanzamiento FROM Album";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                albums.add(new String[]{
                        String.valueOf(rs.getInt("idAlbum")),
                        rs.getString("nombreAlbum"),
                        rs.getString("fechaLanzamiento")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return albums;
    }





    //se puede eliminar este:
    public List<String[]> getAlbumSongsWithArtist(int albumId) {
        List<String[]> songs = new ArrayList<>();
        String query = """
                    SELECT c.idCancion, c.titulo, ar.nombreArtista
                    FROM Cancion c
                    JOIN Album_Cancion ac ON c.idCancion = ac.idCancion
                    JOIN Interpretacion i ON c.idCancion = i.idCancion
                    JOIN Artista ar ON i.idArtista = ar.idArtista
                    WHERE ac.idAlbum = ?
                """;
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, albumId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                songs.add(new String[]{
                        String.valueOf(rs.getInt("idCancion")),
                        rs.getString("titulo"),
                        rs.getString("nombreArtista")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

    public List<String[]> getAlbumSongsWithDetails(int albumId) {
        List<String[]> songs = new ArrayList<>();
        String query = """
                    SELECT c.idCancion, c.titulo, c.duracion, ar.nombreArtista
                    FROM Cancion c
                    JOIN Album_Cancion ac ON c.idCancion = ac.idCancion
                    JOIN Interpretacion i ON c.idCancion = i.idCancion
                    JOIN Artista ar ON i.idArtista = ar.idArtista
                    WHERE ac.idAlbum = ?
                """;
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, albumId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                songs.add(new String[]{
                        String.valueOf(rs.getInt("idCancion")),
                        rs.getString("titulo"),
                        rs.getString("duracion"),
                        rs.getString("nombreArtista")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

    // manejar el historial y generar un reporte PDF

    public List<String[]> getHistorialCompras() {
        List<String[]> historial = new ArrayList<>();
        String query = """
                    SELECT v.fechaVenta, v.totalVenta, 'Compra' AS tipo, v.idVenta 
                    FROM Venta v 
                    WHERE v.idUser = ? 
                    ORDER BY v.fechaVenta DESC
                """;
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, 1); // ID de usuario temporal
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                historial.add(new String[]{
                        rs.getString("fechaVenta"),
                        rs.getString("totalVenta"),
                        rs.getString("tipo"),
                        String.valueOf(rs.getInt("idVenta"))
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return historial;
    }

    // manejar los detalles de compra
    public List<String[]> getDetalleCompra(int idCompra) {
        List<String[]> detalles = new ArrayList<>();
        String query = """
        SELECT c.titulo, dv.precio 
        FROM Detalle_Venta dv 
        JOIN Cancion c ON dv.idCancion = c.idCancion 
        WHERE dv.idVenta = ?
    """;
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idCompra);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                detalles.add(new String[]{
                        rs.getString("titulo"),
                        String.valueOf(rs.getDouble("precio"))
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detalles;
    }


    public boolean generateHistorialPDF(int userId) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 700);

            // Obtener datos del usuario
            String[] userData = getUserDataById(userId);
            contentStream.showText("Historial de Compras");
            contentStream.newLine();
            contentStream.showText("ID del Usuario: " + userId);
            contentStream.newLine();
            contentStream.showText("Nombre completo: " + userData[0]);
            contentStream.newLine();
            contentStream.showText("Correo: " + userData[2]);
            contentStream.newLine();
            contentStream.showText("Teléfono: " + userData[1]);
            contentStream.newLine();
            contentStream.newLine();

            // Obtener historial completo
            List<String[]> historial = getHistorialCompras(userId);
            double montoTotal = 0;

            // Iterar sobre todas las compras en el historial
            for (String[] compra : historial) {
                double totalVenta = Double.parseDouble(compra[1]);
                montoTotal += totalVenta;

                // Agregar datos de la compra al PDF
                contentStream.showText("Fecha: " + compra[0] + ", Total: $" + compra[1] + ", Tipo: " + compra[2]);
                contentStream.newLine();

                // Obtener y agregar detalles de la compra
                int idCompra = Integer.parseInt(compra[3]);
                List<String[]> detalles = getDetalleCompra(idCompra);
                for (String[] detalle : detalles) {
                    contentStream.showText("  - " + detalle[0] + ": $" + detalle[1]);
                    contentStream.newLine();
                }
                contentStream.newLine();
            }

            // Mostrar el monto total de todas las compras
            contentStream.showText("Monto total de las compras: $" + String.format("%.2f", montoTotal));
            contentStream.endText();
            contentStream.close();

            // Guardar el PDF
            document.save("Historial_Compras_User_" + userId + ".pdf");
            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public void listarTodasLasCompras(int userId) {
        Database db = new Database();
        List<String[]> historial = db.getHistorialCompras(userId);

        if (historial.isEmpty()) {
            System.out.println("No se encontraron compras para el usuario con ID: " + userId);
        } else {
            System.out.println("Historial de Compras para el Usuario ID: " + userId);
            for (String[] compra : historial) {
                System.out.println("Fecha: " + compra[0] + ", Total: $" + compra[1] + ", Tipo: " + compra[2] + ", ID de Compra: " + compra[3]);

                // Obtener los detalles de la compra
                int idCompra = Integer.parseInt(compra[3]);
                List<String[]> detalles = db.getDetalleCompra(idCompra);
                for (String[] detalle : detalles) {
                    System.out.println("    - Canción/Álbum: " + detalle[0] + ", Precio: $" + detalle[1]);
                }
            }
        }
    }


    public boolean registerPurchase(List<String[]> carrito, int userId) {
        String ventaQuery = "INSERT INTO Venta (fechaVenta, totalVenta, idUser) VALUES (NOW(), ?, ?)";
        String detalleQuery = "INSERT INTO Detalle_Venta (idVenta, idCancion, precio) VALUES (?, ?, ?)";
        try (Connection conn = Database.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            // Calcular total y registrar la venta
            double total = carrito.size() * 10.00; // Precio ficticio para cada canción
            PreparedStatement ventaStmt = conn.prepareStatement(ventaQuery, Statement.RETURN_GENERATED_KEYS);
            ventaStmt.setDouble(1, total);
            ventaStmt.setInt(2, userId); // Usar el ID del usuario autenticado
            ventaStmt.executeUpdate();
            ResultSet ventaRs = ventaStmt.getGeneratedKeys();
            ventaRs.next();
            int idVenta = ventaRs.getInt(1);

            // Registrar detalles de la venta
            PreparedStatement detalleStmt = conn.prepareStatement(detalleQuery);
            for (String[] song : carrito) {
                detalleStmt.setInt(1, idVenta);
                detalleStmt.setInt(2, Integer.parseInt(song[0]));
                detalleStmt.setDouble(3, 10.00); // Precio ficticio
                detalleStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean registerAlbumPurchase(List<String[]> carrito, int userId) {
        String ventaQuery = "INSERT INTO Venta (fechaVenta, totalVenta, idUser) VALUES (NOW(), ?, ?)";
        String detalleQuery = "INSERT INTO Detalle_Venta (idVenta, idCancion, precio) VALUES (?, ?, ?)";
        try (Connection conn = Database.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            double total = carrito.size() * 50.00; // Precio ficticio por álbum
            PreparedStatement ventaStmt = conn.prepareStatement(ventaQuery, Statement.RETURN_GENERATED_KEYS);
            ventaStmt.setDouble(1, total);
            ventaStmt.setInt(2, userId); // Uso correcto del userId dinámico
            ventaStmt.executeUpdate();

            ResultSet ventaRs = ventaStmt.getGeneratedKeys();
            ventaRs.next();
            int idVenta = ventaRs.getInt(1);

            // Registrar los detalles de las canciones dentro de los álbumes
            PreparedStatement detalleStmt = conn.prepareStatement(detalleQuery);
            for (String[] album : carrito) {
                List<String[]> albumSongs = getSongsByAlbumId(Integer.parseInt(album[0]));
                for (String[] song : albumSongs) {
                    detalleStmt.setInt(1, idVenta);
                    detalleStmt.setInt(2, Integer.parseInt(song[0]));
                    detalleStmt.setDouble(3, 10.00); // Precio ficticio por canción
                    detalleStmt.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }




















    public List<String[]> getHistorialCompras(int userId) {
        List<String[]> historial = new ArrayList<>();
        String query = """
            SELECT v.fechaVenta, v.totalVenta, 'Compra' AS tipo, v.idVenta 
            FROM Venta v 
            WHERE v.idUser = ? 
            ORDER BY v.fechaVenta DESC
        """;
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                historial.add(new String[]{
                        rs.getString("fechaVenta"),
                        rs.getString("totalVenta"),
                        rs.getString("tipo"),
                        String.valueOf(rs.getInt("idVenta"))
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return historial;
    }



    //Datos de usuario
    public String[] getUserDataById(int userId) {
        String[] userData = new String[3];
        String query = "SELECT nombre, telUser, emailUser FROM Usuarios WHERE idUser = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userData[0] = rs.getString("nombre");
                userData[1] = rs.getString("telUser");
                userData[2] = rs.getString("emailUser");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userData;
    }

    public boolean updateUserData(int userId, String telUser, String emailUser) {
        String query = "UPDATE Usuarios SET telUser = ?, emailUser = ? WHERE idUser = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, telUser);
            stmt.setString(2, emailUser);
            stmt.setInt(3, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<String[]> getSongsCountByArtist() {
        List<String[]> results = new ArrayList<>();
        String query = "SELECT a.nombreArtista, COUNT(i.idCancion) AS total_canciones " +
                "FROM Artista a " +
                "JOIN Interpretacion i ON a.idArtista = i.idArtista " +
                "GROUP BY a.nombreArtista";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String[] row = new String[2];
                row[0] = rs.getString("nombreArtista"); // Alias definido en la consulta
                row[1] = String.valueOf(rs.getInt("total_canciones")); // Alias definido en la consulta
                results.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }


    public String[] getMostRecentAlbum() {
        String query = "SELECT nombreAlbum, MAX(fechaLanzamiento) AS fecha_mas_reciente FROM Album";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new String[]{
                        rs.getString("nombreAlbum"),
                        rs.getString("fecha_mas_reciente")
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public double getTotalSales() {
        String query = "SELECT SUM(precio) AS total_ventas FROM Detalle_Venta";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("total_ventas");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }



    public List<String[]> getAlbumsWithSongCount() {
        List<String[]> results = new ArrayList<>();
        String query = """
        SELECT 
            A.nombreAlbum AS Album,
            COUNT(AC.idCancion) AS CantidadCanciones
        FROM 
            Album A
        JOIN 
            Album_Cancion AC ON A.idAlbum = AC.idAlbum
        GROUP BY 
            A.idAlbum, A.nombreAlbum
        ORDER BY 
            CantidadCanciones DESC
    """;
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(new String[]{
                        rs.getString("Album"),
                        String.valueOf(rs.getInt("CantidadCanciones"))
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }



    public String[] getTopSellingGenre() {
        String query = """
        SELECT 
            G.nombreGenero AS Genero,
            SUM(DV.precio) AS IngresoTotal
        FROM 
            Genero G
        JOIN 
            Cancion C ON G.idGenero = C.idGenero
        JOIN 
            Detalle_Venta DV ON C.idCancion = DV.idCancion
        GROUP BY 
            G.idGenero, G.nombreGenero
        ORDER BY 
            IngresoTotal DESC
        LIMIT 1;
    """;
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return new String[]{rs.getString("Genero"), String.valueOf(rs.getDouble("IngresoTotal"))};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    public int getUserId(String username) {
        String sql = "SELECT idUser FROM Usuarios WHERE `User` = ?";
        try (Connection conn = getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idUser");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean deleteSongById(int id) {
        try (Connection conn = Database.getInstance().getConnection()) {
            conn.setAutoCommit(false); // Iniciar una transacción

            // 1. Eliminar referencias en la tabla 'album_cancion'
            String deleteAlbumRelationQuery = "DELETE FROM album_cancion WHERE idCancion = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteAlbumRelationQuery)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            // 2. Eliminar referencias en la tabla 'interpretacion' (relación con artistas)
            String deleteArtistRelationQuery = "DELETE FROM interpretacion WHERE idCancion = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteArtistRelationQuery)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            // 3. Eliminar la canción de la tabla 'cancion'
            String deleteSongQuery = "DELETE FROM cancion WHERE idCancion = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteSongQuery)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit(); // Confirmar la transacción si todo fue exitoso
                    return true;
                } else {
                    conn.rollback(); // Revertir los cambios si no se eliminó ninguna canción
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Manejo de errores
        }
    }


    // Método para verificar si una canción existe por su ID
    public boolean doesSongExist(int id) {
        String query = "SELECT 1 FROM Cancion WHERE idCancion = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Retorna true si la canción existe
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public List<String[]> getTopSellingSongs() {
        List<String[]> songs = new ArrayList<>();
        String query = """
        SELECT c.titulo AS Cancion, COUNT(dv.idCancion) AS Ventas
        FROM Cancion c
        JOIN Detalle_Venta dv ON c.idCancion = dv.idCancion
        GROUP BY c.idCancion, c.titulo
        ORDER BY Ventas DESC
        LIMIT 10;
    """;
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                songs.add(new String[]{
                        rs.getString("Cancion"),
                        String.valueOf(rs.getInt("Ventas"))
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }








    public int getTotalUsuarios() {
        String query = "SELECT COUNT(*) AS total_usuarios FROM Usuarios";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total_usuarios");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }




}
