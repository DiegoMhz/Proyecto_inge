package view.components;

import java.awt.*;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Title {
    public static JLabel createTitlePanel(String titleText) {
        JLabel title = new JLabel(titleText, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        return title;
    }
}
