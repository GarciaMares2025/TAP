# Base de Datos Spotify

## Configuración
1. Ejecutar `BD Spotify.sql` para crear las tablas
2. Ejecutar `Spotify Inserts.sql` para insertar datos iniciales

## Estructura
- `BD Spotify.sql`: Definición de tablas y relaciones
- `Spotify Inserts.sql`: Datos de ejemplo y prueba

## Tablas Principales
- **Usuarios**: Gestión de usuarios del sistema (admin, user, artist)
- **Artista**: Información de artistas musicales
- **Album**: Álbumes musicales con portadas
- **Cancion**: Canciones con duración y ruta de audio
- **Genero**: Clasificación musical (Pop, Rock, Clásica, Jazz)
- **Venta**: Registro de compras de canciones
- **Detalle_Venta**: Detalles específicos de cada venta
- **Album_Cancion**: Relación entre álbumes y canciones
- **Interpretacion**: Relación entre canciones y artistas
