package view;

import static view.components.InputFactory.createInputPanel;
import model.AutenticacionService;
import javax.swing.*;
import java.awt.*;
import model.RolUsuario;
import view.components.RadialGradient;

public class Registro extends JFrame {
    private AutenticacionService authService;
    JTextField inputName;
    JTextField inputLastName;
    JTextField inputCI;
    JTextField inputEmail;
    JPasswordField inputPassword;
    JPasswordField inputConfirmPassword;

    public Registro() {
        this.authService = new AutenticacionService();
        initUI();
    }

    private void initUI() {

        setTitle("Registro de Usuario");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(255, 255, 255, 40));
        // 1. Titulo
        JLabel lblTitulo = new JLabel("REGISTRO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblTitulo);
        // Input Nombre
        inputName = new JTextField();
        panel.add(createInputPanel("Nombre", inputName));

        // Input Apellido
        inputLastName = new JTextField();
        panel.add(createInputPanel("Apellido", inputLastName));

        // Input NumeroCedula
        inputCI = new JTextField();
        panel.add(createInputPanel("Cedula", inputCI));

        // Input Email
        inputEmail = new JTextField();
        panel.add(createInputPanel("Correo", inputEmail));

        // Input Contraseña
        inputPassword = new JPasswordField();
        panel.add(createInputPanel("Contraseña", inputPassword));

        // Input Confirmar Contraseña
        inputConfirmPassword = new JPasswordField();
        panel.add(createInputPanel("Confirmar contraseña", inputConfirmPassword));

        JButton btnRegistrar = new JButton("Registrarme");
        panel.add((btnRegistrar));

        btnRegistrar.putClientProperty("FlatLaf.style", "arc: 10");

        // PONERLE EL FONDO DEGRADADO CON COLOR
        RadialGradient background = new RadialGradient();
        background.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(450, 600));
        this.setContentPane(background);
        add(panel);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        btnRegistrar.addActionListener(e -> ejecutarRegistro());
    }

    private void ejecutarRegistro() {
        String name = inputName.getText().trim();
        String lastName = inputLastName.getText().trim();
        String cedula = inputCI.getText().trim();
        String pass = new String(inputPassword.getPassword());
        String confirmPass = new String(inputConfirmPassword.getPassword());
        String email = inputEmail.getText().trim();
        RolUsuario rol = RolUsuario.EXTERNO;

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

        boolean exito = authService.registrarUsuario(rol, name, lastName, cedula, email, pass);

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