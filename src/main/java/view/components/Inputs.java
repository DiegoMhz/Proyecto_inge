package view.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;

public class Inputs {
    public static JPanel crearCampo(String textoEtiqueta, javax.swing.JComponent componente) {
        JPanel div = new JPanel(new GridLayout(2, 1));
        JLabel text = new JLabel(textoEtiqueta);
        div.add(text);
        div.add(componente);
        div.setOpaque(false);
        return div;
    }
}
