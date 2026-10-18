package model.entities;

public class SesionUsuario {

    private static Usuario usuarioLogueado;

    private SesionUsuario() {
    }

    public static void setUsuarioLogueado(Usuario usuario) {
        usuarioLogueado = usuario;
    }

    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public static void cerrarSesion() {
        usuarioLogueado = null;
    }

    public static boolean haySesionActiva() {
        return usuarioLogueado != null;
    }
}