
CREATE DATABASE spotify;
USE spotify;
CREATE TABLE Usuarios (
    idUser INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telUser VARCHAR(20) NOT NULL,
    emailUser VARCHAR(100) NOT NULL UNIQUE,
    User VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'user', 'artist') NOT NULL
);

Select * from Usuarios;
CREATE TABLE Artista (
    idArtista INT AUTO_INCREMENT PRIMARY KEY,
    nombreArtista VARCHAR(100) NOT NULL,
    nacionalidad VARCHAR(50) NOT NULL
);
CREATE TABLE Album (
    idAlbum INT AUTO_INCREMENT PRIMARY KEY,
    idArtista INT NOT NULL,
    nombreAlbum VARCHAR(100) NOT NULL,
    fechaLanzamiento DATE NOT NULL,
    albumImage BLOB,
    FOREIGN KEY (idArtista) REFERENCES Artista(idArtista) ON DELETE CASCADE
);
CREATE TABLE Genero (
    idGenero INT AUTO_INCREMENT PRIMARY KEY,
    nombreGenero VARCHAR(50) NOT NULL
);
CREATE TABLE Cancion (
    idCancion   INT AUTO_INCREMENT PRIMARY KEY,
    titulo      VARCHAR(100) NOT NULL,
    duracion    TIME NOT NULL,
    idGenero    INT NOT NULL,
    rutaAudio   VARCHAR(255) NOT NULL,
    FOREIGN KEY (idGenero) REFERENCES Genero(idGenero) ON DELETE CASCADE
);
CREATE TABLE Album_Cancion (
    idAlbum INT NOT NULL,
    idCancion INT NOT NULL,
    PRIMARY KEY (idAlbum, idCancion),
    FOREIGN KEY (idAlbum) REFERENCES Album(idAlbum) ON DELETE CASCADE,
    FOREIGN KEY (idCancion) REFERENCES Cancion(idCancion) ON DELETE CASCADE
);
CREATE TABLE Interpretacion (
    idCancion INT NOT NULL,
    idArtista INT NOT NULL,
    PRIMARY KEY (idCancion, idArtista),
    FOREIGN KEY (idCancion) REFERENCES Cancion(idCancion) ON DELETE CASCADE,
    FOREIGN KEY (idArtista) REFERENCES Artista(idArtista) ON DELETE CASCADE
);
CREATE TABLE Venta (
    idVenta INT AUTO_INCREMENT PRIMARY KEY,
    fechaVenta DATETIME NOT NULL,
    totalVenta DECIMAL(10, 2) NOT NULL,
    idUser INT NOT NULL,
    FOREIGN KEY (idUser) REFERENCES Usuarios(idUser) ON DELETE CASCADE
);
CREATE TABLE Detalle_Venta (
    idDetalle INT AUTO_INCREMENT PRIMARY KEY,
    idVenta INT NOT NULL,
    idCancion INT NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (idVenta) REFERENCES Venta(idVenta) ON DELETE CASCADE,
    FOREIGN KEY (idCancion) REFERENCES Cancion(idCancion) ON DELETE CASCADE
);
SELECT SUM(precio) AS total_ventas FROM Detalle_Venta;
