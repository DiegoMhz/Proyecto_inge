package view;

import static view.components.Title.createTitlePanel;
import static view.components.InputFactory.createInputPanel;

import model.entities.Ruta;
import model.services.GestorDerutas;
import view.components.RadialGradient;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class GestionRutas extends JFrame {

    private final GestorDerutas gestorDerutas;

    // Componentes del Formulario
    private JTextField txtNombre;
    private JTextField txtOrigen;
    private JTextField txtDestino;
    private JComboBox<Ruta.TipoRuta> cmbTipoRuta;
    private JTextField txtMunicipios;

    private JButton btnGuardar;
    private JButton btnCancelarEdicion;
    private JPanel panelListaRutas;

    // Control de modo de edición
    private boolean modoEdicion = false;
    private String nombreRutaEnEdicion = null;

    public GestionRutas() {
        this.gestorDerutas = new GestorDerutas();
        initUI();
        cargarRutasEnLista();
    }

    private void initUI() {
        setTitle("Gestión de Rutas");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Maximizar a pantalla completa
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Fondo Degradado Principal
        RadialGradient background = new RadialGradient();
        background.setLayout(new GridBagLayout());
        this.setContentPane(background);

        // Contenedor Tarjeta Central (Semi-transparente)
        JPanel panelTarjeta = new JPanel();
        panelTarjeta.setLayout(new BoxLayout(panelTarjeta, BoxLayout.Y_AXIS));
        panelTarjeta.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        panelTarjeta.setBackground(new Color(255, 255, 255, 30));
        panelTarjeta.setPreferredSize(new Dimension(520, 650));

        // --- ENCABEZADO ---
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setOpaque(false);

        JLabel title = createTitlePanel("REGISTRO DE RUTAS");
        
        JButton btnVolver = new JButton("🏠");
        btnVolver.setPreferredSize(new Dimension(50, 35));
        btnVolver.setFocusPainted(false);
        btnVolver.addActionListener(e -> {
            new AdminHome().setVisible(true);
            this.dispose();
        });

        JPanel panelBtnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelBtnWrapper.setOpaque(false);
        panelBtnWrapper.add(btnVolver);

        panelTop.add(title, BorderLayout.WEST);
        panelTop.add(panelBtnWrapper, BorderLayout.EAST);
        panelTarjeta.add(panelTop);

        panelTarjeta.add(Box.createVerticalStrut(10));

        // --- FORMULARIO SUPERIOR ---
        JPanel panelForm = new JPanel(new GridLayout(5, 1, 6, 6));
        panelForm.setOpaque(false);
        panelForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120)),
                " Datos de la Ruta ",
                0, 0, new Font("SansSerif", Font.BOLD, 12), Color.WHITE));

        txtNombre = new JTextField();
        txtOrigen = new JTextField();
        txtDestino = new JTextField();
        cmbTipoRuta = new JComboBox<>(Ruta.TipoRuta.values());
        cmbTipoRuta.setLightWeightPopupEnabled(false);
        txtMunicipios = new JTextField();

        panelForm.add(createInputPanel("Nombre Ruta", txtNombre));
        panelForm.add(createInputPanel("Origen", txtOrigen));
        panelForm.add(createInputPanel("Destino", txtDestino));
        panelForm.add(createInputPanel("Tipo de Ruta", cmbTipoRuta));
        panelForm.add(createInputPanel("Municipios / Paradas (separados por coma)", txtMunicipios));

        panelTarjeta.add(panelForm);

        panelTarjeta.add(Box.createVerticalStrut(10));

        // --- BOTONES DEL FORMULARIO ---
        JPanel panelAccionesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelAccionesForm.setOpaque(false);

        btnGuardar = new JButton("Registrar Ruta");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> procesarGuardado());

        btnCancelarEdicion = new JButton("Cancelar");
        btnCancelarEdicion.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnCancelarEdicion.setFocusPainted(false);
        btnCancelarEdicion.setVisible(false);
        btnCancelarEdicion.addActionListener(e -> limpiarFormulario());

        panelAccionesForm.add(btnGuardar);
        panelAccionesForm.add(btnCancelarEdicion);

        panelTarjeta.add(panelAccionesForm);

        panelTarjeta.add(Box.createVerticalStrut(15));

        // --- SECCIÓN INFERIOR: TÍTULO LISTA DE RUTAS ---
        JLabel lblTituloLista = new JLabel("Rutas Registradas");
        lblTituloLista.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTituloLista.setForeground(Color.WHITE);
        lblTituloLista.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTarjeta.add(lblTituloLista);

        panelTarjeta.add(Box.createVerticalStrut(10));

        // --- LISTA CON FONDO BLANCO SÓLIDO (CORRIGE EL SOLAPAMIENTO) ---
        panelListaRutas = new JPanel();
        panelListaRutas.setLayout(new BoxLayout(panelListaRutas, BoxLayout.Y_AXIS));
        panelListaRutas.setBackground(Color.WHITE); // Fondo blanco sólido
        panelListaRutas.setOpaque(true); // OPACO para evitar textos fantasma/solapados
        panelListaRutas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(panelListaRutas);
        scroll.setOpaque(true);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        JPanel wrapperScroll = new JPanel(new BorderLayout());
        wrapperScroll.setOpaque(false);
        wrapperScroll.add(scroll, BorderLayout.CENTER);

        panelTarjeta.add(wrapperScroll);

        background.add(panelTarjeta);
    }

    /**
     * Carga todas las rutas existentes desde GestorDerutas hacia la lista visual
     */
    private void cargarRutasEnLista() {
        panelListaRutas.removeAll();
        List<Ruta> lista = gestorDerutas.getRutas();

        if (lista == null || lista.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay rutas registradas.", SwingConstants.CENTER);
            lblVacio.setForeground(Color.DARK_GRAY);
            lblVacio.setFont(new Font("SansSerif", Font.ITALIC, 13));
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelListaRutas.add(lblVacio);
        } else {
            for (Ruta r : lista) {
                panelListaRutas.add(crearFilaRutaVisual(r));
                panelListaRutas.add(Box.createVerticalStrut(8));
            }
        }

        panelListaRutas.revalidate();
        panelListaRutas.repaint();
    }

    /**
     * Crea una tarjeta individual por ruta con fondo gris claro y texto oscuro para alta legibilidad
     */
    private JPanel crearFilaRutaVisual(Ruta r) {
        JPanel fila = new JPanel(new BorderLayout(8, 0));
        fila.setBackground(new Color(242, 244, 245)); // Fondo gris suave
        fila.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 220), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // Nombre de la Ruta y detalles
        String nombreText = (r.getNombreRuta() != null) ? r.getNombreRuta() : "Sin Nombre";
        String detalleText = " (" + (r.getOrigen() != null ? r.getOrigen() : "") + " -> " + 
                             (r.getDestino() != null ? r.getDestino() : "") + ")";
        
        JLabel lblNombre = new JLabel(nombreText + detalleText);
        lblNombre.setForeground(new Color(40, 40, 40)); // Texto oscuro legible
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Panel de Botones (Editar / Eliminar)
        JPanel panelBotonesAccion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelBotonesAccion.setOpaque(false);

        JButton btnEditar = new JButton("✏️");
        btnEditar.setToolTipText("Editar Ruta");
        btnEditar.setMargin(new Insets(2, 6, 2, 6));
        btnEditar.setFocusPainted(false);
        btnEditar.addActionListener(e -> prepararEdicion(r));

        JButton btnEliminar = new JButton("🗑️");
        btnEliminar.setToolTipText("Eliminar Ruta");
        btnEliminar.setMargin(new Insets(2, 6, 2, 6));
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> confirmarEliminacion(r));

        panelBotonesAccion.add(btnEditar);
        panelBotonesAccion.add(btnEliminar);

        fila.add(lblNombre, BorderLayout.CENTER);
        fila.add(panelBotonesAccion, BorderLayout.EAST);

        return fila;
    }

    private void procesarGuardado() {
        String nombre = txtNombre.getText().trim();
        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();
        Ruta.TipoRuta tipo = (Ruta.TipoRuta) cmbTipoRuta.getSelectedItem();
        String municipiosTexto = txtMunicipios.getText().trim();

        if (nombre.isEmpty() || origen.isEmpty() || destino.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor complete el nombre, origen y destino de la ruta.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> municipios = Arrays.stream(municipiosTexto.split(","))
                .map(String::trim)
                .filter(m -> !m.isEmpty())
                .toList();

        if (modoEdicion) {
            boolean exito = gestorDerutas.editarRuta(nombreRutaEnEdicion, origen, destino, tipo, municipios);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Ruta actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarRutasEnLista();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la ruta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            boolean exito = gestorDerutas.registrarRuta(nombre, origen, destino, tipo, municipios);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Ruta registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarRutasEnLista();
            } else {
                JOptionPane.showMessageDialog(this, "Ya existe una ruta registrada con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void prepararEdicion(Ruta r) {
        modoEdicion = true;
        nombreRutaEnEdicion = r.getNombreRuta();

        txtNombre.setText(r.getNombreRuta());
        txtOrigen.setText(r.getOrigen());
        txtDestino.setText(r.getDestino());

        if (r.getTipoRuta() != null) {
            cmbTipoRuta.setSelectedItem(r.getTipoRuta());
        }

        if (r.getMunicipios() != null) {
            txtMunicipios.setText(String.join(", ", r.getMunicipios()));
        } else {
            txtMunicipios.setText("");
        }

        btnGuardar.setText("Guardar Cambios");
        btnCancelarEdicion.setVisible(true);
    }

    private void confirmarEliminacion(Ruta r) {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar la ruta: " + r.getNombreRuta() + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            boolean exito = gestorDerutas.eliminarRuta(r.getNombreRuta());
            if (exito) {
                JOptionPane.showMessageDialog(this, "Ruta eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                if (modoEdicion && r.getNombreRuta().equalsIgnoreCase(nombreRutaEnEdicion)) {
                    limpiarFormulario();
                }
                cargarRutasEnLista();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la ruta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormulario() {
        modoEdicion = false;
        nombreRutaEnEdicion = null;

        txtNombre.setText("");
        txtOrigen.setText("");
        txtDestino.setText("");
        cmbTipoRuta.setSelectedIndex(0);
        txtMunicipios.setText("");

        btnGuardar.setText("Registrar Ruta");
        btnCancelarEdicion.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionRutas().setVisible(true));
    }
}