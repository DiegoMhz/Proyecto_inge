import view.Login;
import javax.swing.SwingUtilities;
// import com.formdev.flatlaf.FlatDarkLaf;

public class Main {
    public static void main(String[] args) {
        com.formdev.flatlaf.FlatLightLaf.setup();
        // Ejecutar la interfaz gráfica dentro del hilo de eventos de Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            // 1. Instanciar la ventana
            Login loginFrame = new Login();
            // new view.Registro().setVisible(true);
            // // 2. Hacerla visible en pantalla
            loginFrame.setVisible(true);
        });
    }
}