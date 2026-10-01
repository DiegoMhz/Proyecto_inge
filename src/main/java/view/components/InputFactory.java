package view.components;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;

public final class InputFactory {

    // Constructor privado para evitar instanciación
    private InputFactory() {}

    public static JPanel createInputPanel(String textoEtiqueta, JComponent componente) {
        JPanel div = new JPanel(new GridLayout(2, 1));
        JLabel text = new JLabel(textoEtiqueta);
        div.add(text);
        div.add(componente);
        div.setOpaque(false);
        return div;
    }
}