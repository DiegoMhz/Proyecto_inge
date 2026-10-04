package view.components;

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;

public class CustomIconButton extends JButton {

    public CustomIconButton(String text, String pngImagePath) {
        super(text);

        // Cargar la imagen desde el Classpath
        java.net.URL imgURL = getClass().getClassLoader().getResource(pngImagePath);

        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(scaledImage));
        } else {
            System.err.println("No se pudo encontrar la imagen en la ruta: " + pngImagePath);
        }
        this.setBackground(new Color(46, 139, 87));
        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setForeground(Color.WHITE);
        this.setFont(new Font("SansSerif", Font.BOLD, 13));

        
        

        this.setPreferredSize(new Dimension(140, 140));
        this.setMaximumSize(new Dimension(140, 140));
        this.setMinimumSize(new Dimension(140, 140));
    }
}