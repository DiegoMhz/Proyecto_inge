package view;

import static view.components.Title.createTitlePanel;
import static view.components.InputFactory.createInputPanel;
import view.components.RadialGradient;
import javax.swing.*;
import java.awt.*;
import view.AdminHome;

public class Login extends JFrame {
    private JTextField inputCedula;
    private JPasswordField inputPassword;

    public Login() {
        initUI();
    }

    private void initUI() {
        // Configuración de la ventana principal
        setTitle("Inicio de Sesion");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setPreferredSize(new Dimension(420, 350));
        panel.setBackground(new Color(255, 255, 255, 40));

        // TITULO
        JLabel title = createTitlePanel("INICIO DE SESION");
        panel.add(title);

        // INPUT CEDULA
        inputCedula = new JTextField();
        panel.add(createInputPanel("Cédula", inputCedula));

        // INPUT PASSWORD
        inputPassword = new JPasswordField();
        panel.add(createInputPanel("Contraseña", inputPassword));

        // CREO UN DIV O CAJA PARA LOS BOTONES
        JPanel div = new JPanel(new GridLayout(2, 1, 0, 10));
        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnRegistro = new JButton("Registrarse");
        div.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        div.add(btnLogin);
        div.add(btnRegistro);
        panel.add(div);
        div.setOpaque(false);

        // PONERLE EL FONDO DEGRADADO CON COLOR
        RadialGradient background = new RadialGradient();
        background.setLayout(new GridBagLayout());
        this.setContentPane(background);
        add(panel);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Registro de eventos mediante lambdas
        btnLogin.addActionListener(e -> ejecutarLogin());
        btnRegistro.addActionListener(e -> {
            new Registro().setVisible(true);
            this.dispose();
        });
    }

    private void ejecutarLogin() {
        String cedula = inputCedula.getText();
        String password = new String(inputPassword.getPassword());

        if (cedula.isBlank() || password.isBlank()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos", "Atención",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            new AdminHome().setVisible(true);
            this.dispose();
        }
    }
}