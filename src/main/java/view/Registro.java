package view;
import static view.components.Title.createTitlePanel;
import static view.components.InputFactory.createInputPanel;

import model.entities.RolUsuario;
import model.services.AutenticacionService;

import javax.swing.*;
import java.awt.*;

import view.components.RadialGradient;

public class Registro extends JFrame {
    private AutenticacionService authService;
    private final boolean registroAdministrativo;
    JTextField inputName;
    JTextField inputLastName;
    JTextField inputCI;
    JTextField inputEmail;
    JPasswordField inputPassword;
    JPasswordField inputConfirmPassword;
    private JComboBox<RolUsuario> inputRol;

    public Registro() {
        this(false);
    }

    public Registro(boolean registroAdministrativo) {
        this.authService = new AutenticacionService();
        this.registroAdministrativo = registroAdministrativo;
        initUI();
    }

    private void initUI() {

        setTitle("Registro de Usuario");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(9, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(255, 255, 255, 40));
        // 1. Titulo
        JLabel title = createTitlePanel("REGISTRO");
        panel.add(title);
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

        inputRol = new JComboBox<>(registroAdministrativo
                ? new RolUsuario[] { RolUsuario.ADMINISTRADOR, RolUsuario.CONDUCTOR }
                : new RolUsuario[] { RolUsuario.ESTUDIANTE, RolUsuario.EMPLEADO_PROFESOR, RolUsuario.EXTERNO });
        inputRol.setLightWeightPopupEnabled(false);
        inputRol.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof RolUsuario rol) {
                    setText(nombreVisibleRol(rol));
                }
                return this;
            }
        });
        panel.add(createInputPanel("Rol de usuario", inputRol));

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
        //panel.setBackground(Color.WHITE);
        //panel.setOpaque(false);
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
        RolUsuario rol = (RolUsuario) inputRol.getSelectedItem();

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
            if (registroAdministrativo) {
                new AdminHome().setVisible(true);
            } else {
                new Login().setVisible(true);
            }
            this.dispose(); // Cierra la ventana de registro
        } else {
            JOptionPane.showMessageDialog(this, "El nombre de usuario ya existe.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String nombreVisibleRol(RolUsuario rol) {
        return switch (rol) {
            case ESTUDIANTE -> "Estudiante";
            case EMPLEADO_PROFESOR -> "Empleado/Profesor";
            case EXTERNO -> "Externo";
            case ADMINISTRADOR -> "Administrador";
            case CONDUCTOR -> "Conductor";
        };
    }
}