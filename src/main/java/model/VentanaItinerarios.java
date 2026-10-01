package model;

import model.ControlDeItinerario;
import model.Ruta;
import model.UnidadDeTransporte;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class VentanaItinerarios extends JFrame {

    private final ItinerarioService servicio;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Componentes del Formulario
    private JTextField txtId;
    private JTextField txtRutaId, txtRutaNombre, txtRutaOrigen, txtRutaDestino;
    private JTextField txtUnidadId, txtUnidadPlaca, txtUnidadModelo;
    private JTextField txtConductorId, txtConductorNombre;
    private JTextField txtFechaSalida, txtFechaLlegada;

    // Componentes de la Tabla
    private JTable tablaItinerarios;
    private DefaultTableModel modeloTabla;

    public VentanaItinerarios() {
        this.servicio = new ItinerarioService();

        setTitle("Gestión de Control de Itinerarios - SGT");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear contenedor de pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Crear Itinerario", crearPanelFormulario());
        tabbedPane.addTab("Ver Itinerarios Guardados", crearPanelTabla());

        // Escuchar cambios de pestaña para recargar la tabla automáticamente
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) {
                cargarDatosTabla();
            }
        });

        add(tabbedPane);
    }

    /**
     * Pestaña 1: Formulario de entrada de datos
     */
    private JPanel crearPanelFormulario() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelForm = new JPanel(new GridLayout(12, 2, 8, 8));

        // Campos del formulario
        txtId = new JTextField();
        txtRutaId = new JTextField();
        txtRutaNombre = new JTextField();
        txtRutaOrigen = new JTextField();
        txtRutaDestino = new JTextField();
        txtUnidadId = new JTextField();
        txtUnidadPlaca = new JTextField();
        txtUnidadModelo = new JTextField();
        txtConductorId = new JTextField();
        txtConductorNombre = new JTextField();
        txtFechaSalida = new JTextField("2026-10-15 08:00");
        txtFechaLlegada = new JTextField("2026-10-15 09:30");

        // Agregar etiquetas y campos
        panelForm.add(new JLabel("ID Itinerario:"));
        panelForm.add(txtId);

        panelForm.add(new JLabel("--- RUTA ---"));
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel("ID / Nombre Ruta:"));
        JPanel panelRutaGroup = new JPanel(new GridLayout(1, 2, 5, 0));
        panelRutaGroup.add(txtRutaId);
        panelRutaGroup.add(txtRutaNombre);
        panelForm.add(panelRutaGroup);

        panelForm.add(new JLabel("Origen / Destino:"));
        JPanel panelOrigenDestino = new JPanel(new GridLayout(1, 2, 5, 0));
        panelOrigenDestino.add(txtRutaOrigen);
        panelOrigenDestino.add(txtRutaDestino);
        panelForm.add(panelOrigenDestino);

        panelForm.add(new JLabel("--- UNIDAD DE TRANSPORTE ---"));
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel("ID / Placa / Modelo:"));
        JPanel panelUnidadGroup = new JPanel(new GridLayout(1, 3, 5, 0));
        panelUnidadGroup.add(txtUnidadId);
        panelUnidadGroup.add(txtUnidadPlaca);
        panelUnidadGroup.add(txtUnidadModelo);
        panelForm.add(panelUnidadGroup);

        panelForm.add(new JLabel("--- CONDUCTOR ---"));
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel("ID / Nombre Conductor:"));
        JPanel panelConductorGroup = new JPanel(new GridLayout(1, 2, 5, 0));
        panelConductorGroup.add(txtConductorId);
        panelConductorGroup.add(txtConductorNombre);
        panelForm.add(panelConductorGroup);

        panelForm.add(new JLabel("--- HORARIOS (yyyy-MM-dd HH:mm) ---"));
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel("Fecha/Hora Salida:"));
        panelForm.add(txtFechaSalida);
        panelForm.add(new JLabel("Fecha/Hora Llegada Estimada:"));
        panelForm.add(txtFechaLlegada);

        // Botón Guardar
        JButton btnGuardar = new JButton("Guardar Itinerario");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(46, 139, 87));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> procesarGuardado());

        panelPrincipal.add(new JScrollPane(panelForm), BorderLayout.CENTER);
        panelPrincipal.add(btnGuardar, BorderLayout.SOUTH);

        return panelPrincipal;
    }

    /**
     * Pestaña 2: Visualización de datos en tabla
     */
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columnas = {"ID", "Ruta", "Origen-Destino", "Unidad", "Conductor", "Salida", "Llegada"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilitar edición directa en celda
            }
        };

        tablaItinerarios = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaItinerarios);

        JButton btnRefrescar = new JButton("Actualizar Tabla");
        btnRefrescar.addActionListener(e -> cargarDatosTabla());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnRefrescar, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Lógica para validar, instanciar y guardar mediante ItinerarioService
     */
    private void procesarGuardado() {
        try {
            // Validaciones básicas de campos vacíos
            if (txtId.getText().trim().isEmpty() || txtFechaSalida.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El ID y las fechas son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Parsing de Fechas
            LocalDateTime salida = LocalDateTime.parse(txtFechaSalida.getText().trim(), formatter);
            LocalDateTime llegada = LocalDateTime.parse(txtFechaLlegada.getText().trim(), formatter);

            // Construcción de objetos internos (Asegúrate de ajustar los parámetros según los constructores de tus clases)
            Ruta ruta = new Ruta();
            //ruta.setId(txtRutaId.getText().trim());
            ruta.setNombreRuta(txtRutaNombre.getText().trim());
            ruta.setOrigen(txtRutaOrigen.getText().trim());
            ruta.setDestino(txtRutaDestino.getText().trim());

            UnidadDeTransporte unidad = new UnidadDeTransporte();
            //unidad.setNu(txtUnidadId.getText().trim());
            unidad.setPlaca(txtUnidadPlaca.getText().trim());
            unidad.setModelo(txtUnidadModelo.getText().trim());

            Usuario conductor = new Usuario();
            //conductor.setId(txtConductorId.getText().trim());
            conductor.setUsuario(txtConductorNombre.getText().trim());

            // Construcción del objeto principal
            ControlDeItinerario nuevoItinerario = new ControlDeItinerario();
            nuevoItinerario.setId(txtId.getText().trim());
            nuevoItinerario.setRuta(ruta);
            nuevoItinerario.setUnidad(unidad);
            nuevoItinerario.setConductor(conductor);
            nuevoItinerario.setFechaSalida(salida);
            nuevoItinerario.setFechaLlegada(llegada);

            // Persistencia en JSON
            boolean exito = servicio.agregarItinerario(nuevoItinerario);

            if (exito) {
                JOptionPane.showMessageDialog(this, "¡Itinerario guardado exitosamente en JSON!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar. Verifique si el ID ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use el formato: yyyy-MM-dd HH:mm\nEjemplo: 2026-10-15 08:30", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga todos los itinerarios del JSON hacia la tabla Swing
     */
    private void cargarDatosTabla() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<ControlDeItinerario> lista = servicio.obtenerTodos();

        for (ControlDeItinerario it : lista) {
            String idRuta = (it.getRuta() != null) ? it.getRuta().getNombreRuta() : "N/A";
            String origenDestino = (it.getRuta() != null) ? it.getRuta().getOrigen() + " -> " + it.getRuta().getDestino() : "N/A";
            String unidad = (it.getUnidad() != null) ? it.getUnidad().getPlaca() : "N/A";
            String conductor = (it.getConductor() != null) ? it.getConductor().getUsuario(): "N/A";
            String salidaStr = (it.getFechaSalida() != null) ? it.getFechaSalida().format(formatter) : "N/A";
            String llegadaStr = (it.getFechaLlegada() != null) ? it.getFechaLlegada().format(formatter) : "N/A";

            Object[] fila = {
                it.getId(),
                idRuta,
                origenDestino,
                unidad,
                conductor,
                salidaStr,
                llegadaStr
            };
            modeloTabla.addRow(fila);
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtRutaId.setText("");
        txtRutaNombre.setText("");
        txtRutaOrigen.setText("");
        txtRutaDestino.setText("");
        txtUnidadId.setText("");
        txtUnidadPlaca.setText("");
        txtUnidadModelo.setText("");
        txtConductorId.setText("");
        txtConductorNombre.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaItinerarios().setVisible(true));
    }
}