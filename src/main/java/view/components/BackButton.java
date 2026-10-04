package view.components;

import javax.swing.*;
import java.awt.*;

public class BackButton extends JButton {

    public BackButton() {
        super("← Volver"); // Texto simple con flecha

        // Estilo del texto
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setForeground(Color.WHITE);

        // Quitar bordes feos y hacer el fondo transparente
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}