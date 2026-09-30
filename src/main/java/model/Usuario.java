package model;

public class Usuario {
    private String usuario;
    private String password;

    // Constructor vacío necesario para que Jackson pueda deserializar el JSON
    public Usuario() {}

    public Usuario(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }

    // Getters y Setters
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}