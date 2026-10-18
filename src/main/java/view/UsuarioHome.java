package view;

import static view.components.Title.createTitlePanel;

import view.components.CustomIconButton;
import view.components.RadialGradient;

import javax.swing.*;
import java.awt.*;

public class UsuarioHome extends JFrame {

    private CustomIconButton btnRutasUrbanas;
    private CustomIconButton btnRutasExtraurbanas;

    public UsuarioHome() {
        initUI();
    }

    private void initUI() {
        // Configuración básica de la ventana
        setTitle("Inicio Usuario - SGT");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Maximizar a pantalla completa
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Fondo Degradado Principal
        RadialGradient background = new RadialGradient();
        background.setLayout(new GridBagLayout()); // Centra el contenido en pantalla
        this.setContentPane(background);

        // Panel Tarjeta Central (Semi-transparente)
        JPanel panelTarjeta = new JPanel();
        panelTarjeta.setLayout(new BoxLayout(panelTarjeta, BoxLayout.Y_AXIS));
        panelTarjeta.setBorder(BorderFactory.createEmptyBorder(35, 40, 40, 40));
        panelTarjeta.setBackground(new Color(255, 255, 255, 30)); // Transparencia estilo Registro
        panelTarjeta.setPreferredSize(new Dimension(420, 520));

        // Título de Bienvenida
        JLabel title = createTitlePanel("<html><center>¡Hola! ¿Qué ruta deseas<br>consultar hoy?</center></html>");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTarjeta.add(title);

        panelTarjeta.add(Box.createVerticalStrut(35)); // Espaciado entre título y botones

        // Panel para colocar los dos botones verticalmente
        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 0, 20));
        panelBotones.setOpaque(false);
        panelBotones.setMaximumSize(new Dimension(280, 280));

        // Botón 1: Rutas Urbanas
        btnRutasUrbanas = new CustomIconButton(
                "<html><center>Rutas urbanas</center></html>",
                "icons/bus.png" // Ajusta a la ruta de tu ícono
        );

        // Botón 2: Rutas Extraurbanas
        btnRutasExtraurbanas = new CustomIconButton(
                "<html><center>Rutas<br>extraurbanas</center></html>",
                "icons/circulo.png" // Ajusta a la ruta de tu ícono
        );

        panelBotones.add(btnRutasUrbanas);
        panelBotones.add(btnRutasExtraurbanas);

        panelTarjeta.add(panelBotones);

        // Agregar la tarjeta centrada al fondo
        background.add(panelTarjeta);

        // --- EVENTOS ---
        btnRutasUrbanas.addActionListener(e -> {
            new RutasUrbanas().setVisible(true);
            this.dispose();
        });

        btnRutasExtraurbanas.addActionListener(e -> {
            new RutasExtraUrbanas().setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UsuarioHome().setVisible(true));
    }
}