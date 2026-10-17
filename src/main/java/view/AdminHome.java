package view;

import static view.components.Title.createTitlePanel;

import view.components.CustomIconButton;
import view.components.RadialGradient;

import javax.swing.*;


import java.awt.*;

public class AdminHome extends JFrame {
        private CustomIconButton btnGestionUnidades;
        private CustomIconButton btnGestionFlota;
        private CustomIconButton btnRegistro;
        // private CustomIconButton btnGestionRutas;
        // private CustomIconButton btnGestionReportes;

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
                background.setLayout(new BorderLayout(0, 30));
                this.setContentPane(background);

                JPanel panelRegistro = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                panelRegistro.setOpaque(false);
                panelRegistro.setBorder(BorderFactory.createEmptyBorder(60, 0, 0, 0));
                btnRegistro = new CustomIconButton(
                                "Registro de Admin/Conductor");
                btnRegistro.setFont(new Font("SansSerif", Font.BOLD, 16));
                btnRegistro.setPreferredSize(new Dimension(300, 70));
                btnRegistro.setMinimumSize(new Dimension(300, 70));
                btnRegistro.setMaximumSize(new Dimension(300, 70));

                panelRegistro.add(btnRegistro);
                background.add(panelRegistro, BorderLayout.NORTH);

                JPanel panelContenido = new JPanel(new GridBagLayout());
                panelContenido.setOpaque(false);
                panelContenido.setBorder(BorderFactory.createEmptyBorder(40, 200, 40, 200));
                background.add(panelContenido, BorderLayout.CENTER);
                panelContenido.add(panel);

                // TITULO
                JLabel title = createTitlePanel("Panel de Administración");
                panel.add(title);

                JPanel panelBotones = new JPanel();
                panelBotones.setLayout(new GridLayout(2, 2, 20, 20));
                btnGestionUnidades = new CustomIconButton(
                                "<html><center>Registro de<br>Unidades</center></html>",
                                "icons/bus.png");
                // btnGestionRutas = new CustomIconButton(
                //                 "<html><center>Registro de<br>Conductores</center></html>",
                //                 "icons/cedula.png");
                // btnGestionReportes = new CustomIconButton(
                //                 "<html><center>Optimización y<br>Mantenimiento</center></html>",
                //                 "icons/laptop.png");
                btnGestionFlota = new CustomIconButton(
                                "<html><center>Gestion de<br>Unidades</center></html>",
                                "icons/circulo.png");

                panelBotones.add(btnGestionUnidades);
                // panelBotones.add(btnGestionRutas);
                // panelBotones.add(btnGestionReportes);
                panelBotones.add(btnGestionFlota);
                panelBotones.setOpaque(false);
                panel.add(panelBotones);

                btnGestionFlota.addActionListener(e -> {
                        new ControlItineraios().setVisible(true);
                        this.dispose();
                });

                btnGestionUnidades.addActionListener(e -> {
                        new GestionDeFlota().setVisible(true);
                        this.dispose();
                });

                btnRegistro.addActionListener(e -> {
                        new Registro(true).setVisible(true);
                        this.dispose();
                });

        }
}