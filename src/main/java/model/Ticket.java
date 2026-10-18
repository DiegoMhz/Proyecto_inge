package model;

import java.time.LocalDateTime;

public class Ticket {
    private String idTicket;
    private String cedulaUsuario;
    private String nombreRuta;
    private String tipoRuta; // URBANA o EXTRAURBANA
    private String rolUsuario; // Ej: ESTUDIANTE, PROFESOR, EXTERNO
    private double montoCobrado;
    private String fechaHora;

    // Constructor vacío para Jackson / JSON
    public Ticket() {
    }

    public Ticket(String idTicket, String cedulaUsuario, String nombreRuta, String tipoRuta, String rolUsuario, double montoCobrado) {
        this.idTicket = idTicket;
        this.cedulaUsuario = cedulaUsuario;
        this.nombreRuta = nombreRuta;
        this.tipoRuta = tipoRuta;
        this.rolUsuario = rolUsuario;
        this.montoCobrado = montoCobrado;
        this.fechaHora = LocalDateTime.now().toString();
    }

    // Getters y Setters
    public String getIdTicket() { return idTicket; }
    public void setIdTicket(String idTicket) { this.idTicket = idTicket; }

    public String getCedulaUsuario() { return cedulaUsuario; }
    public void setCedulaUsuario(String cedulaUsuario) { this.cedulaUsuario = cedulaUsuario; }

    public String getNombreRuta() { return nombreRuta; }
    public void setNombreRuta(String nombreRuta) { this.nombreRuta = nombreRuta; }

    public String getTipoRuta() { return tipoRuta; }
    public void setTipoRuta(String tipoRuta) { this.tipoRuta = tipoRuta; }

    public String getRolUsuario() { return rolUsuario; }
    public void setRolUsuario(String rolUsuario) { this.rolUsuario = rolUsuario; }

    public double getMontoCobrado() { return montoCobrado; }
    public void setMontoCobrado(double montoCobrado) { this.montoCobrado = montoCobrado; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
}
