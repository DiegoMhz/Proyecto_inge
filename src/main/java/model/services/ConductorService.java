package model.services;

import model.entities.Usuario;
import model.entities.RolUsuario;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

public class ConductorService {

    private final AutenticacionService authService;

    public ConductorService() {
        this.authService = new AutenticacionService();
    }

    // OBTENER TODOS CONDUCTORES
    public List<Usuario> obtenerTodosLosConductores() {
        List<Usuario> todosLosUsuarios = authService.obtenerUsuarios();
        List<Usuario> conductores = new ArrayList<>();

        for (Usuario u : todosLosUsuarios) {
            if (u.getRol() == RolUsuario.CONDUCTOR) {
                conductores.add(u);
            }
        }
        return conductores;
    }

    public ComboBoxModel<Usuario> obtenerModeloConductores() {
        List<Usuario> conductores = obtenerTodosLosConductores();
        Usuario[] arreglo = conductores.toArray(new Usuario[0]);
        return new DefaultComboBoxModel<>(arreglo);
    }
}