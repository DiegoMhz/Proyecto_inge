package view;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    public Login() {
        initUI();
    }

    private void initUI() {
        // Configuración de la ventana principal
        
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel con cuadrícula (4 filas, 2 columnas, espacio de 10px)
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Creación e inserción de componentes
        panel.add(new JLabel(""));
        txtUsuario = new JTextField();
        panel.add(txtUsuario);

        panel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        JButton btnLogin = new JButton("Ingresar");
        JButton btnRegistro = new JButton("Registrarse");

        panel.add(btnLogin);
        panel.add(btnRegistro);

        // Agregar panel a la ventana
        add(panel);

        // Registro de eventos mediante lambdas
        btnLogin.addActionListener(e -> ejecutarLogin());
        btnRegistro.addActionListener(e -> JOptionPane.showMessageDialog(this, "Navegando a Registro..."));
    }

    private void ejecutarLogin() {
        String usuario = txtUsuario.getText();
        String password = new String(txtPassword.getPassword());

        if (usuario.isBlank() || password.isBlank()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos", "Atención", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Datos capturados correctamente para: " + usuario, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}