package org.example.parcial2.models;

public class User {
    private int idUser;
    private String nombre;
    private String telUser;
    private String emailUser;
    private String user;
    private String password;
    private String role;

    public User(int idUser, String nombre, String telUser, String emailUser, String user, String password, String role) {
        this.idUser = idUser;
        this.nombre = nombre;
        this.telUser = telUser;
        this.emailUser = emailUser;
        this.user = user;
        this.password = password;
        this.role = role;
    }

    // Getters para cada propiedad
    public int getIdUser() { return idUser; }
    public String getNombre() { return nombre; }
    public String getTelUser() { return telUser; }
    public String getEmailUser() { return emailUser; }
    public String getUser() { return user; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}
