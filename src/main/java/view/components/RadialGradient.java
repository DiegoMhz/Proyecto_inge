package view.components;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.RadialGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;

public class RadialGradient extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        float cx = width / 2f;
        float cy = height / 3f;
        float radius = Math.max(width, height);
        
        float[] fractions = { 0.0f, 0.25f, 0.5f, 0.75f, 1.0f };
        
        // Colores exactos de tu paleta proporcionada
        Color[] colors = {
            Color.decode("#2FAD74"),
            Color.decode("#279872"),
            Color.decode("#1F836F"),
            Color.decode("#074568"),
            Color.decode("#003366")
        };
        
        RadialGradientPaint rgp = new RadialGradientPaint(
            cx, cy, radius, fractions, colors, CycleMethod.NO_CYCLE
        );
        
        g2d.setPaint(rgp);
        g2d.fillRect(0, 0, width, height);
    }
}