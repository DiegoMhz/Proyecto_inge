package view;

import static view.components.Title.createTitlePanel;

import model.entities.ControlDeItinerario;
import model.entities.Ruta;
import model.services.ItinerarioService;
import view.components.RadialGradient;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RutasUrbanas extends JFrame {

    private final ItinerarioService itinerarioService;
    private JPanel panelLista;

    public RutasUrbanas() {
        this.itinerarioService = new ItinerarioService();
        initUI();
        cargarItinerarios();
    }

    private void initUI() {
        setTitle("Itinerarios - Rutas Urbanas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel contenedor central
        JPanel panelContenedor = new JPanel(new BorderLayout(15, 15));
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panelContenedor.setBackground(new Color(255, 255, 255, 30));
        panelContenedor.setPreferredSize(new Dimension(500, 550));

        // --- ENCABEZADO ---
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setOpaque(false);

        JLabel title = createTitlePanel("RUTAS URBANAS");
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

        // --- LISTA DE ITINERARIOS ---
        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setOpaque(false);

        // ScrollPane transparente
        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);

        // Panel wrapper para forzar que los elementos se alineen ARRIBA
        JPanel panelScrollWrapper = new JPanel(new BorderLayout());
        panelScrollWrapper.setOpaque(false);
        panelScrollWrapper.add(scroll, BorderLayout.NORTH);

        panelContenedor.add(panelScrollWrapper, BorderLayout.CENTER);

        // --- CONFIGURAR FONDO DEGRADADO ---
        RadialGradient background = new RadialGradient();
        background.setLayout(new GridBagLayout());

        this.setContentPane(background);
        add(panelContenedor);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void cargarItinerarios() {
        panelLista.removeAll();
        
        // Obtener todos los itinerarios del archivo JSON y filtrar solo los de tipo URBANA
        List<ControlDeItinerario> itinerarios = itinerarioService.obtenerTodos().stream()
                .filter(it -> it.getRuta() != null && it.getRuta().getTipoRuta() == Ruta.TipoRuta.URBANA)
                .toList();

        if (itinerarios == null || itinerarios.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay itinerarios programados para rutas urbanas.", SwingConstants.CENTER);
            lblVacio.setForeground(Color.WHITE);
            lblVacio.setFont(new Font("SansSerif", Font.ITALIC, 14));
            panelLista.add(lblVacio);
        } else {
            for (ControlDeItinerario it : itinerarios) {
                panelLista.add(crearFilaItinerario(it));
                panelLista.add(Box.createVerticalStrut(10));
            }
        }

        panelLista.revalidate();
        panelLista.repaint();
    }

    private JPanel crearFilaItinerario(ControlDeItinerario it) {
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setBackground(new Color(255, 255, 255, 45));
        fila.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        // Nombre de la ruta tomada desde el objeto ControlDeItinerario
        String nombreRuta = (it.getRuta() != null && it.getRuta().getNombreRuta() != null)
                ? it.getRuta().getNombreRuta()
                : "Ruta Sin Nombre";

        JLabel lblNombre = new JLabel(nombreRuta);
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Botón "Ver"
        JButton btnVer = new JButton("Ver");
        btnVer.setPreferredSize(new Dimension(75, 28));
        btnVer.setFocusPainted(false);
        btnVer.addActionListener(e -> verDetalleItinerario(it));

        fila.add(lblNombre, BorderLayout.WEST);
        fila.add(btnVer, BorderLayout.EAST);

        return fila;
    }

    private void verDetalleItinerario(ControlDeItinerario it) {
        Ruta r = it.getRuta();

        String origen = (r != null && r.getOrigen() != null) ? r.getOrigen() : "N/A";
        String destino = (r != null && r.getDestino() != null) ? r.getDestino() : "N/A";
        String conductor = (it.getConductor() != null && it.getConductor().getName() != null)
                ? it.getConductor().getName()
                : "No asignado";
        String unidad = (it.getUnidad() != null && it.getUnidad().getPlaca() != null)
                ? it.getUnidad().getPlaca()
                : "No asignada";
        String salida = (it.getFechaSalida() != null) ? it.getFechaSalida() : "N/A";
        String llegada = (it.getFechaLlegada() != null) ? it.getFechaLlegada() : "N/A";

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("📌 Origen: ").append(origen).append("\n");
        mensaje.append("🏁 Destino: ").append(destino).append("\n");
        mensaje.append("👤 Conductor: ").append(conductor).append("\n");
        mensaje.append("🚌 Unidad (Placa): ").append(unidad).append("\n");
        mensaje.append("⏰ Hora de Salida: ").append(salida).append("\n");
        mensaje.append("⏳ Llegada Estimada: ").append(llegada).append("\n\n");

        mensaje.append("🚏 Paradas:\n");
        if (r != null && r.getParadas() != null && !r.getParadas().isEmpty()) {
            for (String p : r.getParadas()) {
                mensaje.append("  • ").append(p).append("\n");
            }
        } else {
            mensaje.append("  (Sin paradas intermedias)");
        }

        String tituloVentana = (r != null && r.getNombreRuta() != null) ? r.getNombreRuta() : "Itinerario";

        JOptionPane.showMessageDialog(this, mensaje.toString(), "Detalle del Itinerario: " + tituloVentana,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RutasUrbanas().setVisible(true));
    }
}