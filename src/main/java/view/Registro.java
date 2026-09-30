package view;

import model.AutenticacionService;

import javax.swing.*;
import java.awt.*;

public class Registro extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmarPassword;
    private AutenticacionService authService;

    public Registro() {
        this.authService = new AutenticacionService();
        initUI();
    }

    private void initUI() {
        setTitle("Registro de Usuario");
        setSize(420, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Solo cierra esta ventana
        setLocationRelativeTo(null);
        setResizable(false);

        // Contenedor principal con GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // --- Título (H1) ---
        JLabel h1 = new JLabel("Crear Cuenta");
        h1.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(h1, gbc);

        gbc.gridwidth = 1; // Resetear ancho a 1 columna

        // --- Campo Usuario ---
        JLabel lblUsuario = new JLabel("Usuario:");
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(lblUsuario, gbc);

        txtUsuario = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtUsuario, gbc);

        // --- Campo Contraseña ---
        JLabel lblPassword = new JLabel("Contraseña:");
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(lblPassword, gbc);

        txtPassword = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtPassword, gbc);

        // --- Campo Confirmar Contraseña ---
        JLabel lblConfirmar = new JLabel("Confirmar:");
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panel.add(lblConfirmar, gbc);

        txtConfirmarPassword = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtConfirmarPassword, gbc);

        // --- Botón Registrar ---
        JButton btnRegistrar = new JButton("Guardar Registro");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 8, 8, 8);
        panel.add(btnRegistrar, gbc);

        add(panel);

        // Evento del botón al hacer clic
        btnRegistrar.addActionListener(e -> ejecutarRegistro());
    }

    private void ejecutarRegistro() {
        String usuario = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword());
        String confirmPass = new String(txtConfirmarPassword.getPassword());

        if (usuario.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!pass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean exito = authService.registrarUsuario(usuario, pass);

        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Usuario registrado correctamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); // Cierra la ventana de registro
        } else {
            JOptionPane.showMessageDialog(this, "El nombre de usuario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}