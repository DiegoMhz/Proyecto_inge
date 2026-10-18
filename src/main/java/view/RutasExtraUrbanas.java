package view;

import static view.components.Title.createTitlePanel;

import model.entities.Ruta;
import model.services.RutaService;
import view.components.RadialGradient;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RutasExtraUrbanas extends JFrame {

    private final RutaService rutaService;
    private JPanel panelLista;

    public RutasExtraUrbanas() {
        this.rutaService = new RutaService();
        initUI();
        cargarRutas();
    }

    private void initUI() {
        setTitle("Rutas Extraurbanas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel contenedor central
        JPanel panelContenedor = new JPanel(new BorderLayout(15, 15));
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panelContenedor.setBackground(new Color(255, 255, 255, 30)); // Transparencia suave[cite: 4]
        panelContenedor.setPreferredSize(new Dimension(500, 550));

        // --- ENCABEZADO ---
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setOpaque(false);

        JLabel title = createTitlePanel("RUTAS EXTRAURBANAS");
        JButton btnVolver = new JButton("🏠");
        btnVolver.setFocusPainted(false);

        btnVolver.addActionListener(e -> {
            new UsuarioHome().setVisible(true);
            this.dispose();
        });

        panelTop.add(title, BorderLayout.WEST);
        panelTop.add(btnVolver, BorderLayout.EAST);

        // --- ENCABEZADOS DE COLUMNAS (Ruta / Estatus) ---
        JPanel panelTitulos = new JPanel(new BorderLayout());
        panelTitulos.setOpaque(false);
        panelTitulos.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

        JLabel lblRuta = new JLabel("Ruta");
        JLabel lblEstatus = new JLabel("Estatus", SwingConstants.CENTER);
        lblRuta.setForeground(Color.WHITE);
        lblRuta.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblEstatus.setForeground(Color.WHITE);
        lblEstatus.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblEstatus.setPreferredSize(new Dimension(80, 20));

        panelTitulos.add(lblRuta, BorderLayout.WEST);
        panelTitulos.add(lblEstatus, BorderLayout.EAST);

        // Agrupamos el Título y los Enunciados en el Norte
        JPanel panelNorteCompleto = new JPanel(new BorderLayout());
        panelNorteCompleto.setOpaque(false);
        panelNorteCompleto.add(panelTop, BorderLayout.NORTH);
        panelNorteCompleto.add(panelTitulos, BorderLayout.SOUTH);

        panelContenedor.add(panelNorteCompleto, BorderLayout.NORTH);

        // --- LISTA DE RUTAS ---
        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setOpaque(false);

        // ScrollPane transparente
        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);

        // Panel wrapper para forzar que los elementos se alineen ARRIBA y no se
        // desparramen
        JPanel panelScrollWrapper = new JPanel(new BorderLayout());
        panelScrollWrapper.setOpaque(false);
        panelScrollWrapper.add(scroll, BorderLayout.NORTH);

        panelContenedor.add(panelScrollWrapper, BorderLayout.CENTER);

        // --- CONFIGURAR FONDO DEGRADADO ---
        RadialGradient background = new RadialGradient();
        background.setLayout(new GridBagLayout()); // Centra el tarjeta en pantalla completa[cite: 4]

        this.setContentPane(background);
        add(panelContenedor);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizado si lo deseas
    }

    private void cargarRutas() {
        panelLista.removeAll();
        List<Ruta> rutas = rutaService.obtenerRutasPorTipo(Ruta.TipoRuta.EXTRAURBANA);

        if (rutas == null || rutas.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay rutas registradas.", SwingConstants.CENTER);
            lblVacio.setForeground(Color.WHITE);
            lblVacio.setFont(new Font("SansSerif", Font.ITALIC, 14));
            panelLista.add(lblVacio);
        } else {
            for (Ruta r : rutas) {
                panelLista.add(crearFilaRuta(r));
                panelLista.add(Box.createVerticalStrut(10)); // Espaciado constante entre tarjetas
            }
        }

        panelLista.revalidate();
        panelLista.repaint();
    }

    private JPanel crearFilaRuta(Ruta ruta) {

        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setBackground(new Color(255, 255, 255, 45));
        fila.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        // Nombre de la ruta
        String nombre = ruta.getNombreRuta();
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Botón "Ver"
        JButton btnVer = new JButton("Ver");
        btnVer.setPreferredSize(new Dimension(75, 28));
        btnVer.setFocusPainted(false);
        btnVer.addActionListener(e -> verDetalleRuta(ruta));

        fila.add(lblNombre, BorderLayout.WEST);
        fila.add(btnVer, BorderLayout.EAST);

        return fila;
    }

    private void verDetalleRuta(Ruta ruta) {
        String mensaje = "Origen: " + ruta.getOrigen() + "\n"
                + "Destino: " + ruta.getDestino() + "\n"
                + "Tipo: " + ruta.getTipoRuta() + "\n\n"
                + "Paradas:\n";

        if (ruta.getParadas() != null && !ruta.getParadas().isEmpty()) {
            for (String p : ruta.getParadas()) {
                mensaje += "- " + p + "\n";
            }
        } else {
            mensaje += "(Sin paradas intermedias)";
        }

        JOptionPane.showMessageDialog(this, mensaje, "Detalle: " + ruta.getNombreRuta(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RutasUrbanas().setVisible(true));
    }
}