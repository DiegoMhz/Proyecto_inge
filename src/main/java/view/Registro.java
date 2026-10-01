package view;

import model.AutenticacionService;
import javax.swing.*;
import java.awt.*;

public class Registro extends JFrame {
    private AutenticacionService authService;
    JTextField inputName;
    JTextField inputLastName;
    JTextField inputCI;
    JTextField inputEmail;
    JPasswordField inputPassword;
    JPasswordField inputConfirmPassword;

    public Registro() {
        initUI();
    }

    private JPanel crearCampo(String textoEtiqueta, JComponent componente) {
        JPanel div = new JPanel(new GridLayout(2, 1));
        JLabel text = new JLabel(textoEtiqueta);
        componente.putClientProperty("FlatLaf.style", "arc: 10");
        div.add(text);
        div.add(componente);
        return div;
    }

    private void initUI() {

        setTitle("Registro de Usuario");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Contenedor principal con GridLayout (4 filas, 2 columnas, espacios de 10px)
        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. Titulo
        JLabel lblTitulo = new JLabel("REGISTRO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblTitulo);
        // Input Nombre
        inputName = new JTextField();
        panel.add(crearCampo("Nombre", inputName));

        // Input Apellido
        inputLastName = new JTextField();
        panel.add(crearCampo("Apellido", inputLastName));

        // Input NumeroCedula
        inputCI = new JTextField();
        panel.add(crearCampo("Cedula", inputCI));

        // Input Email
        inputEmail = new JTextField();
        panel.add(crearCampo("Correo", inputEmail));

        // Input Contraseña
        inputPassword = new JPasswordField();
        panel.add(crearCampo("Contraseña", inputPassword));

        // Input Confirmar Contraseña
        inputConfirmPassword = new JPasswordField();
        panel.add(crearCampo("Confirmar contraseña", inputConfirmPassword));

        JButton btnRegistrar = new JButton("Registrarme");
        panel.add((btnRegistrar));

        btnRegistrar.putClientProperty("FlatLaf.style", "arc: 10");

        // 2. IMPORTANTE: Agregar el panel al JFrame
        this.add(panel);

        btnRegistrar.addActionListener(e -> ejecutarRegistro());
    }

    private void ejecutarRegistro() {
        String name = inputName.getText().trim();
        String lastName = inputLastName.getText().trim();
        String cedula = inputCI.getText().trim();
        String pass = new String(inputPassword.getPassword());
        String confirmPass = new String(inputConfirmPassword.getPassword());

        if (name.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || lastName.isEmpty() || cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!pass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean exito = authService.registrarUsuario(name, lastName, cedula, pass);

        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Usuario registrado correctamente!",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); // Cierra la ventana de registro
        } else {
            JOptionPane.showMessageDialog(this, "El nombre de usuario ya existe.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}