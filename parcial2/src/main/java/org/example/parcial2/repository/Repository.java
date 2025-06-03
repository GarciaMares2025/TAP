package org.example.parcial2.repository;

import java.util.List;

/**
 * Interfaz genérica para CRUD sobre cualquier entidad T.
 */
public interface Repository<T> {
    /**
     * Devuelve la lista completa de objetos T (todas las filas de la tabla).
     */
    List<T> buscarTodos();

    /**
     * Devuelve un objeto T dado su id. Retorna null si no existe.
     */
    T buscarPorId(int id);

    /**
     * Inserta un nuevo objeto T en la base de datos.
     * Puede devolver el id generado (o asignarlo al propio objeto, si lo manejas así).
     */
    void guardar(T t);

    /**
     * Actualiza un registro existente en la base de datos según el id que tenga el objeto T.
     */
    void actualizar(T t);

    /**
     * Elimina el registro cuyo id coincida con el pasado como parámetro.
     */
    void eliminar(int id);
}
