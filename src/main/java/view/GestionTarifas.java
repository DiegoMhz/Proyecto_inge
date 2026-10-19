package view;

import static view.components.Title.createTitlePanel;
import static view.components.InputFactory.createInputPanel;

import model.entities.Municipio;
import model.services.TarifaService;
import view.components.RadialGradient;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GestionTarifas extends JFrame {

    private final TarifaService tarifaService;
    private JTextField txtDescuentoEstudiante;
    private JTextField txtDescuentoEmpleado;

    // Configuración por Municipio
    private JTextField txtMunicipio;
    private JTextField txtTarifaBase;

    private JPanel panelListaTarifas;

    public GestionTarifas() {
        tarifaService = new TarifaService();
        initUI();
        cargarDatosGuardados();
    }

    private void initUI() {
        setTitle("Gestión de Tarifas y Descuentos");
        setSize(800, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Maximizar a pantalla completa
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Fondo Degradado Principal
        RadialGradient background = new RadialGradient();
        background.setLayout(new GridBagLayout());
        this.setContentPane(background);

        // Tarjeta Central
        JPanel panelTarjeta = new JPanel();
        panelTarjeta.setLayout(new BoxLayout(panelTarjeta, BoxLayout.Y_AXIS));
        panelTarjeta.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        panelTarjeta.setBackground(new Color(255, 255, 255, 30));
        panelTarjeta.setPreferredSize(new Dimension(530, 670));

        // --- ENCABEZADO ---
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setOpaque(false);

        JLabel title = createTitlePanel("TARIFAS Y DESCUENTOS");

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

        // --- BLOQUE 1: DESCUENTOS GENERALES (GLOBALES) ---
        JPanel panelDescuentos = new JPanel(new GridLayout(2, 1, 6, 6));
        panelDescuentos.setOpaque(false);
        panelDescuentos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120)),
                " Descuentos Generales del Sistema (%) ",
                0, 0, new Font("SansSerif", Font.BOLD, 12), Color.WHITE));

        txtDescuentoEstudiante = new JTextField("15");
        txtDescuentoEmpleado = new JTextField("35");

        panelDescuentos.add(createInputPanel("Porcentaje Aplicado a Estudiantes (%)", txtDescuentoEstudiante));
        panelDescuentos.add(createInputPanel("Porcentaje Aplicado a Empleados (%)", txtDescuentoEmpleado));

        panelTarjeta.add(panelDescuentos);

        panelTarjeta.add(Box.createVerticalStrut(10));

        // --- BLOQUE 2: TARIFA POR MUNICIPIO ---
        JPanel panelMunicipioForm = new JPanel(new GridLayout(2, 1, 6, 6));
        panelMunicipioForm.setOpaque(false);
        panelMunicipioForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120)),
                " Tarifa Base por Municipio ",
                0, 0, new Font("SansSerif", Font.BOLD, 12), Color.WHITE));

        txtMunicipio = new JTextField();
        txtTarifaBase = new JTextField();

        panelMunicipioForm.add(createInputPanel("Municipio / Zona", txtMunicipio));
        panelMunicipioForm.add(createInputPanel("Tarifa Base (BS)", txtTarifaBase));

        panelTarjeta.add(panelMunicipioForm);

        panelTarjeta.add(Box.createVerticalStrut(10));

        // --- BOTONES DE GUARDAR ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.setOpaque(false);

        JButton btnGuardarDescuentos = new JButton("Guardar Descuentos");
        btnGuardarDescuentos.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnGuardarDescuentos.setFocusPainted(false);
        btnGuardarDescuentos.addActionListener(e -> guardarDescuentosGenerales());

        JButton btnGuardarMunicipio = new JButton("Guardar Tarifa Municipio");
        btnGuardarMunicipio.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnGuardarMunicipio.setFocusPainted(false);
        btnGuardarMunicipio.addActionListener(e -> guardarTarifaMunicipio());

        panelBotones.add(btnGuardarDescuentos);
        panelBotones.add(btnGuardarMunicipio);

        panelTarjeta.add(panelBotones);

        panelTarjeta.add(Box.createVerticalStrut(15));

        // --- SECCIÓN INFERIOR: LISTA DE TARIFAS ---
        JLabel lblTituloLista = new JLabel("Tarifas Base Registradas");
        lblTituloLista.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTituloLista.setForeground(Color.WHITE);
        lblTituloLista.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTarjeta.add(lblTituloLista);

        panelTarjeta.add(Box.createVerticalStrut(8));

        // --- LISTA CON FONDO BLANCO SÓLIDO (OPACO) ---
        panelListaTarifas = new JPanel();
        panelListaTarifas.setLayout(new BoxLayout(panelListaTarifas, BoxLayout.Y_AXIS));
        panelListaTarifas.setBackground(Color.WHITE);
        panelListaTarifas.setOpaque(true);
        panelListaTarifas.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(panelListaTarifas);
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

    private void guardarDescuentosGenerales() {
        String estStr = txtDescuentoEstudiante.getText().trim();
        String empleadoStr = txtDescuentoEmpleado.getText().trim();

        try {
            int descEst = Integer.parseInt(estStr);
            int descEmpleado = Integer.parseInt(empleadoStr);

            if (descEst < 10 || descEst > 20 || descEmpleado < 30 || descEmpleado > 60) {
                JOptionPane.showMessageDialog(this,
                        "El porcentaje debe estar entre el 10 y 20% para estudiantes, "
                                + "y entre el 30 y 60% para empleados y profesores.",
                        "Rango Inválido", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!tarifaService.guardarDescuentos(descEst, descEmpleado)) {
                JOptionPane.showMessageDialog(this,
                        "No se pudieron guardar los descuentos. Revise el archivo y los permisos.",
                        "Error al guardar", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "¡Descuentos globales actualizados!\n\n" +
                            "🎓 Estudiantes: " + descEst + "%\n" +
                            "👥 Empleados: " + descEmpleado + "%",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese valores numéricos válidos para los porcentajes.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarTarifaMunicipio() {
        String municipio = txtMunicipio.getText().trim();
        String tarifaStr = txtTarifaBase.getText().trim();

        if (municipio.isEmpty() || tarifaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Complete el nombre del municipio y la tarifa base.",
                    "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            float tarifa = Float.parseFloat(tarifaStr.replace(",", "."));
            if (!Float.isFinite(tarifa) || tarifa < 0) {
                JOptionPane.showMessageDialog(this,
                        "Ingrese una tarifa válida, finita y no negativa.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!tarifaService.guardarMunicipio(new Municipio(municipio, tarifa))) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo guardar la tarifa. Revise el archivo y los permisos.",
                        "Error al guardar", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Tarifa guardada exitosamente para " + municipio + ": " + tarifa + " BS",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            txtMunicipio.setText("");
            txtTarifaBase.setText("");
            cargarTarifasGuardadas();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un monto válido para la tarifa.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosGuardados() {
        txtDescuentoEstudiante.setText(String.valueOf(tarifaService.getDescuentoEstudiante()));
        txtDescuentoEmpleado.setText(String.valueOf(tarifaService.getDescuentoEmpleado()));
        cargarTarifasGuardadas();
    }

    private void cargarTarifasGuardadas() {
        panelListaTarifas.removeAll();

        List<Municipio> municipios = tarifaService.obtenerMunicipios();
        if (municipios.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay tarifas de municipios registradas.");
            lblVacio.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelListaTarifas.add(lblVacio);
        } else {
            for (Municipio municipio : municipios) {
                if (municipio != null && municipio.getMunicipioNombre() != null) {
                    panelListaTarifas.add(crearFilaTarifaVisual(
                            municipio.getMunicipioNombre(), municipio.getMunicipioTarifa()));
                    panelListaTarifas.add(Box.createVerticalStrut(6));
                }
            }
        }

        panelListaTarifas.revalidate();
        panelListaTarifas.repaint();
    }

    private JPanel crearFilaTarifaVisual(String municipio, float tarifaBase) {
        JPanel fila = new JPanel(new BorderLayout(8, 0));
        fila.setBackground(new Color(242, 244, 245));
        fila.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 220), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lblInfo = new JLabel("📍 " + municipio + " — Tarifa Base: " + tarifaBase + " BS");
        lblInfo.setForeground(new Color(40, 40, 40));
        lblInfo.setFont(new Font("SansSerif", Font.BOLD, 13));

        JButton btnEditar = new JButton("✏️");
        btnEditar.setToolTipText("Cargar para editar");
        btnEditar.setMargin(new Insets(2, 6, 2, 6));
        btnEditar.setFocusPainted(false);
        btnEditar.addActionListener(e -> {
            txtMunicipio.setText(municipio);
            txtTarifaBase.setText(String.valueOf(tarifaBase));
        });

        fila.add(lblInfo, BorderLayout.CENTER);
        fila.add(btnEditar, BorderLayout.EAST);

        return fila;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionTarifas().setVisible(true));
    }
}