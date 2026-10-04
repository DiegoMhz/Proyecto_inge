package view;

import static view.components.Title.createTitlePanel;

import view.components.CustomIconButton;
import view.components.RadialGradient;
import javax.swing.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.None;

import java.awt.*;

public class AdminHome extends JFrame {
    private CustomIconButton btnGestionUsuarios;
    private CustomIconButton btnGestionFlota;
    private CustomIconButton btnGestionRutas;
    private CustomIconButton btnGestionReportes;

    public AdminHome() {
        initUI();
    }

    private void initUI() {
        // Configuración de la ventana principal
        setTitle("Panel de Administración");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // MAXIMIZAR LA VENTANA AL INICIAR
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        panel.setBackground(new Color(255, 255, 255, 40));

        // PONERLE EL FONDO DEGRADADO CON COLOR
        RadialGradient background = new RadialGradient();
        background.setLayout(new GridBagLayout());
        background.setBorder(BorderFactory.createEmptyBorder(40, 200, 40, 200));
        this.setContentPane(background);
        background.add(panel);

        // TITULO
        JLabel title = createTitlePanel("Panel de Administración");
        panel.add(title);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(2, 2, 20, 20));
        btnGestionUsuarios = new CustomIconButton(
                "<html><center>Registro de<br>Unidades</center></html>",
                "icons/bus.png");
        btnGestionRutas = new CustomIconButton(
                "<html><center>Registro de<br>Conductores</center></html>",
                "icons/cedula.png");
        btnGestionReportes = new CustomIconButton(
                "<html><center>Optimización y<br>Mantenimiento</center></html>",
                "icons/laptop.png");
        btnGestionFlota = new CustomIconButton(
                "<html><center>Gestion de<br>Unidades</center></html>",
                "icons/circulo.png");

        panelBotones.add(btnGestionUsuarios);
        panelBotones.add(btnGestionRutas);
        panelBotones.add(btnGestionReportes);
        panelBotones.add(btnGestionFlota);
        panelBotones.setOpaque(false);
        panel.add(panelBotones);
       


    }
}