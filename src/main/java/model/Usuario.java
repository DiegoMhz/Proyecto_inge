package model;

public class Usuario {
    private String name;
    private String password;
    private String lastName;
    private String cedula;

    // Constructor vacío necesario para que Jackson pueda deserializar el JSON
    public Usuario() {}

    public Usuario  (String name, String password) {
        this.name = name;
        this.password = password;
    }

    // Getters y Setters
    public String getCedula() { return cedula; }
    public String getLastName() { return lastName; }
    public String getname() { return name; }
    public void setname(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}