package model;

public class Usuario {
    private RolUsuario rol;
    private String name;
    private String password;
    private String lastName;
    private String cedula;
    private String correo;
    
    // Constructor vacío necesario para que Jackson pueda deserializar el JSON
    public Usuario() {}

    public Usuario  (RolUsuario rol, String name, String lastName, String cedula, String correo, String password) {
        this.name = name;
        this.password = password;
        this.lastName= lastName;
        this.cedula= cedula;
        this.correo= correo;
        this.rol= rol;
    }

    // Getters y Setters
    public String getCedula() { return cedula; }
    public void setCedula(String cedula){this.cedula=cedula;}
    public String getLastName() { return lastName; }
    public void setLastName(String lastName){this.lastName=lastName;}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCorreo(){return correo;}
    public void setCorreo(String correo){this.correo=correo;}
    public RolUsuario getRol(){return rol;}
    public void setRol(RolUsuario rol){this.rol=rol;}
}