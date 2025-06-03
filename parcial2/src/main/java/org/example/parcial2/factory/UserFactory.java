package org.example.parcial2.factory;

import org.example.parcial2.models.User;

public class UserFactory {
    public static User crearUsuario(
            String nombre,
            String telUser,
            String emailUser,
            String user,
            String password,
            String role
    ) {
        // idUser lo genera la BD, por eso es 0 (placeholder)
        return new User(0, nombre, telUser, emailUser, user, password, role);
    }
}
