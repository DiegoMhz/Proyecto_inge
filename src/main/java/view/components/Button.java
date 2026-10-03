package view.components;

import javax.swing.JButton;

public class Button {
    public static JButton createButton(String text){
        JButton button = new JButton(text);
        return button;
    }
}
