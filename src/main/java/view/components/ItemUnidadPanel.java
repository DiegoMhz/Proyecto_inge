package view.components;

import javax.swing.*;
import java.awt.*;
import model.GestorDeFlota;

public class ItemUnidadPanel extends JPanel {

    public ItemUnidadPanel(String placa) {
        // 1. Configuramos directamente 'this' (este panel)
        this.setLayout(new BorderLayout(10, 0));
        this.setOpaque(false);
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        this.setPreferredSize(new Dimension(500, 40));

        // 2. Usamos JLabel simple para que NO vuelva a salir el formulario
        JLabel label = new JLabel(placa);
        label.setForeground(Color.BLACK);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));

        JPanel panelTexto = new JPanel(new BorderLayout());
        panelTexto.setBackground(Color.WHITE);
        panelTexto.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        panelTexto.add(label, BorderLayout.CENTER);

        // 3. Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotones.setOpaque(false);

        JButton btnEditar = new JButton("✏");
        JButton btnEliminar = new JButton("🗑");
        btnEditar.setFocusable(false);
        btnEliminar.setFocusable(false);
        btnEliminar.putClientProperty("placaAsociada", placa);
        btnEditar.putClientProperty("placaAsociada", placa);

        btnEliminar.addActionListener(e -> {
            String placaExtraida = (String) btnEliminar.getClientProperty("placaAsociada");
            GestorDeFlota gestor = new GestorDeFlota();
            gestor.EliminarUnidad(placaExtraida);
            Container contenedor = this.getParent();
            contenedor.remove(this);
            contenedor.revalidate();
            contenedor.repaint();
        });

        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        // 4. IMPORTANTE: Agregamos las partes a 'this'
        this.add(panelTexto, BorderLayout.CENTER);
        this.add(panelBotones, BorderLayout.EAST);
    }
}