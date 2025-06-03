package org.example.parcial2.controlller;

import org.example.parcial2.models.User;
import org.example.parcial2.repository.UserRepository;
import java.util.List;

public class UserController {
    private final UserRepository userRepository = new UserRepository();

    public List<User> listarUsuarios() {
        return userRepository.buscarTodos();
    }

    public User buscarUsuarioPorId(int idUser) {
        return userRepository.buscarPorId(idUser);
    }

    public void crearUsuario(User user) {
        userRepository.guardar(user);
    }

    public void actualizarUsuario(User user) {
        userRepository.actualizar(user);
    }

    public void eliminarUsuario(int idUser) {
        userRepository.eliminar(idUser);
    }

    // Métodos útiles extra:
    public boolean existeUsuario(String username) {
        return userRepository.buscarPorUsername(username) != null;
    }

    public User buscarPorUsername(String username) {
        return userRepository.buscarPorUsername(username);
    }
}
