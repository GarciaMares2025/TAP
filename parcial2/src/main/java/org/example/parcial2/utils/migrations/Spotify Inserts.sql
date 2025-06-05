-- Spotify Inserts.sql (modificado para rutaAudio)

USE spotify;

-- Insert usuarios
INSERT INTO Usuarios (nombre, telUser, emailUser, User, password, role)
VALUES
('Juan Pérez', '5551234567', 'juan.perez@example.com', 'juan123',   'password123', 'admin'),
('María Gómez', '5559876543', 'maria.gomez@example.com', 'maria456','password456', 'user'),
('Carlos López','5557654321', 'carlos.lopez@example.com','carlos789','password789','user'),
('Ana Torres', '5551122334','ana.torres@example.com',  'ana321',   'password321','user');




-- Verificar que las contraseñas se actualizaron correctamente
SELECT User, password, LENGTH(password) as password_length FROM Usuarios;

-- Insert genero
INSERT INTO Genero (nombreGenero)
VALUES
('Pop'),
('Rock'),
('Clásica'),
('Jazz');

-- Insert artista
INSERT INTO Artista (nombreArtista, nacionalidad)
VALUES
('Artista 1', 'Mexicana'),
('Artista 2', 'Argentina');

-- Insert album
INSERT INTO Album (nombreAlbum, fechaLanzamiento, albumImage, idArtista)
VALUES
('Álbum 1', '2022-01-15', NULL, 1),
('Álbum 2', '2023-03-20', NULL, 2);

-- Insert cancion (ahora con rutaAudio)
INSERT INTO Cancion (titulo, duracion, idGenero, rutaAudio) VALUES
('Clasica', '00:05:28', (SELECT idGenero FROM Genero WHERE nombreGenero = 'Clasica'), 'file:///C:/Users/Kolker/Downloads/Clasica.mp3'),
('C1', '00:05:32', (SELECT idGenero FROM Genero WHERE nombreGenero = 'Clasica'), 'file:///C:/Users/Kolker/Downloads/C1.mp3'),
('C2', '00:05:08', (SELECT idGenero FROM Genero WHERE nombreGenero = 'Clasica'), 'file:///C:/Users/Kolker/Downloads/C2.mp3');


-- Insert album-cancion
INSERT INTO Album_Cancion (idAlbum, idCancion)
VALUES
(1, 1),
(2, 2);

-- Insert interpretacion
INSERT INTO Interpretacion (idCancion, idArtista)
VALUES
(1, 1),
(2, 2);

-- Insert venta
INSERT INTO Venta (fechaVenta, totalVenta, idUser)
VALUES
('2023-11-30 10:30:00', 20.00, 1);

-- Insert detalle-venta
INSERT INTO Detalle_Venta (idVenta, idCancion, precio)
VALUES
(1, 1, 10.00);

SELECT idCancion, titulo, duracion, idGenero, rutaAudio
FROM Cancion
WHERE titulo = 'Clasica';

SELECT c.idCancion, c.titulo, COUNT(v.idVenta) as cantidadVentas
