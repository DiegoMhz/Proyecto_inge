package view;

import static view.components.Title.createTitlePanel;
import static view.components.InputFactory.createInputPanel;

import view.components.RadialGradient;

import javax.swing.*;
import java.awt.*;

public class GestionTarifas extends JFrame {

    // Configuración Global de Descuentos
    private JTextField txtDescuentoEstudiante;
    private JTextField txtDescuentoProfesor;

    // Configuración por Municipio
    private JTextField txtMunicipio;
    private JTextField txtTarifaBase;

    private JPanel panelListaTarifas;

    public GestionTarifas() {
        initUI();
        cargarTarifasEjemplo();
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

        txtDescuentoEstudiante = new JTextField("50.0"); // Valor por defecto
        txtDescuentoProfesor = new JTextField("25.0");   // Valor por defecto

        panelDescuentos.add(createInputPanel("Descuento Estudiantes (%)", txtDescuentoEstudiante));
        panelDescuentos.add(createInputPanel("Descuento Profesores (%)", txtDescuentoProfesor));

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
        String profStr = txtDescuentoProfesor.getText().trim();

        try {
            double descEst = Double.parseDouble(estStr.replace(",", "."));
            double descProf = Double.parseDouble(profStr.replace(",", "."));

            if (descEst < 0 || descEst > 100 || descProf < 0 || descProf > 100) {
                JOptionPane.showMessageDialog(this,
                        "Los porcentajes de descuento deben estar entre 0% y 100%.",
                        "Rango Inválido", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Aquí guardas los porcentajes globales en tu servicio o configuración JSON
            
            JOptionPane.showMessageDialog(this,
                    "¡Descuentos globales actualizados!\n\n" +
                            "🎓 Estudiantes: " + descEst + "%\n" +
                            "👨‍🏫 Profesores: " + descProf + "%",
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
            double tarifa = Double.parseDouble(tarifaStr.replace(",", "."));
            if (tarifa < 0) {
                JOptionPane.showMessageDialog(this,
                        "La tarifa base no puede ser negativa.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Aquí guardas/actualizas la tarifa del municipio en tu JSON
            
            JOptionPane.showMessageDialog(this,
                    "Tarifa guardada exitosamente para " + municipio + ": " + tarifa + " BS",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            txtMunicipio.setText("");
            txtTarifaBase.setText("");
            cargarTarifasEjemplo();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un monto válido para la tarifa.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTarifasEjemplo() {
        panelListaTarifas.removeAll();

        // Datos de muestra (reemplazar por llamadas a tu servicio JSON de Tarifas)
        panelListaTarifas.add(crearFilaTarifaVisual("Libertador", 50.0));
        panelListaTarifas.add(Box.createVerticalStrut(6));
        panelListaTarifas.add(crearFilaTarifaVisual("Baruta", 60.0));
        panelListaTarifas.add(Box.createVerticalStrut(6));
        panelListaTarifas.add(crearFilaTarifaVisual("Chacao", 55.0));

        panelListaTarifas.revalidate();
        panelListaTarifas.repaint();
    }

    private JPanel crearFilaTarifaVisual(String municipio, double tarifaBase) {
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