import view.Login;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        com.formdev.flatlaf.FlatLightLaf.setup();
        // Ejecutar la interfaz gráfica dentro del hilo de eventos de Swing (EDT)
        SwingUtilities.invokeLater(() -> {

            Login loginFrame = new Login();

            loginFrame.setVisible(true);
        });
    }
}
