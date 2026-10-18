package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.entities.ControlDeItinerario;
import model.entities.EstadoOperativo;
import model.services.RutaService;
import model.entities.Ruta;
import model.entities.Usuario;
import model.services.ConductorService;
import model.services.ItinerarioService;
import model.services.UnidadDeTransporte;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class ControlItineraios extends JFrame {

    private final ItinerarioService servicio;
    private final RutaService rutaService; // <-- Declarar RutaService
    private JComboBox<Ruta> cmbRutas;
    // Componentes del Formulario
    private JTextField txtId;
    private JComboBox<Ruta.TipoRuta> cmbTipoRuta;
    private JTextField txtUnidadNumero, txtUnidadPlaca, txtUnidadModelo, txtUnidadCapacidad;
    private JComboBox<EstadoOperativo> cmbEstadoUnidad;
    private JTextField txtConductorNombre, txtConductorApellido, txtConductorCedula, txtConductorCorreo;
    private JPasswordField txtConductorPassword;
    private JTextField txtHoraSalida, txtHoraLlegada;

    // Componentes de la Tabla
    private JTable tablaItinerarios;
    private DefaultTableModel modeloTabla;
    private ConductorService conductorService;
    private JComboBox<Usuario> cmbConductores;

    public ControlItineraios() {
        this.servicio = new ItinerarioService();
        this.conductorService = new ConductorService();
        this.rutaService = new RutaService();
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
        cargarDatosTabla();
    }

    private void actualizarComboRutas() {
        Ruta.TipoRuta tipoSeleccionado = (Ruta.TipoRuta) cmbTipoRuta.getSelectedItem();
        if (tipoSeleccionado != null) {
            cmbRutas.setModel(rutaService.obtenerModeloRutasPorTipo(tipoSeleccionado));
        }
    }

    // Pestaña 1: Formulario de entrada de datos
    private JPanel crearPanelFormulario() {
        cmbConductores = new JComboBox<>();
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelForm = new JPanel(new GridLayout(0, 2, 8, 8));

        // Campos del formulario
        txtId = new JTextField(servicio.obtenerSiguienteId());
        txtId.setEditable(false);
        
        cmbTipoRuta = new JComboBox<>(Ruta.TipoRuta.values());
        txtUnidadNumero = new JTextField();
        txtUnidadPlaca = new JTextField();
        txtUnidadModelo = new JTextField();
        txtUnidadCapacidad = new JTextField();
        cmbEstadoUnidad = new JComboBox<>(EstadoOperativo.values());

        txtHoraSalida = new JTextField("08:00");
        txtHoraLlegada = new JTextField("09:30");

        // Agregar etiquetas y campos
        panelForm.add(new JLabel("ID Itinerario:"));
        panelForm.add(txtId);

        panelForm.add(new JLabel("--- UNIDAD DE TRANSPORTE ---"));
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel("Placa:"));
        panelForm.add(txtUnidadPlaca);

        // RUTA
        panelForm.add(new JLabel("--- RUTA ---"));
        panelForm.add(new JLabel(""));
        // Selector de Tipo de Ruta
        cmbTipoRuta = new JComboBox<>(Ruta.TipoRuta.values());
        panelForm.add(new JLabel("Tipo de Ruta:"));
        panelForm.add(cmbTipoRuta);

        cmbRutas = new JComboBox<>(rutaService.obtenerModeloRutas());
        panelForm.add(new JLabel("Seleccionar Ruta:"));
        panelForm.add(cmbRutas);

        cmbTipoRuta.addActionListener(e -> actualizarComboRutas());

        actualizarComboRutas();
        // CONDUCTORES
        cmbConductores.setModel(conductorService.obtenerModeloConductores());
        panelForm.add(new JLabel("--- CONDUCTOR ---"));
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel("Seleccionar Conductor:"));
        panelForm.add(cmbConductores);

        panelForm.add(new JLabel("--- HORARIOS ---"));
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel("Salida (HH:mm):"));
        panelForm.add(txtHoraSalida);
        panelForm.add(new JLabel("Llegada Estimada (HH:mm):"));
        panelForm.add(txtHoraLlegada);

        // Botón Guardar
        JButton btnGuardar = new JButton("Guardar Itinerario");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(46, 139, 87));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> procesarGuardado());

        JScrollPane scrollPane = new JScrollPane(panelForm);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(btnGuardar, BorderLayout.SOUTH);

        return panelPrincipal;
    }

    /**
     * Pestaña 2: Visualización de datos en tabla
     */
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columnas = { "ID", "Ruta", "Origen", "Destino", "Unidad", "Conductor", "Salida", "Llegada" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilitar edición directa en celda
            }
        };

        tablaItinerarios = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaItinerarios);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

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
            String salida = txtHoraSalida.getText().trim();
            String llegada = txtHoraLlegada.getText().trim();
            if (salida.isEmpty() || llegada.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La salida y llegada estimada son obligatorias.", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Usuario conductorSeleccionado = (Usuario) cmbConductores.getSelectedItem();

            if (conductorSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un conductor de la lista.", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Ruta rutaSeleccionada = (Ruta) cmbRutas.getSelectedItem();

            if (rutaSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una ruta de la lista.", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            UnidadDeTransporte unidad = new UnidadDeTransporte();
            String placa = txtUnidadPlaca.getText().trim();
            unidad = unidad.getUnidadDeTransporte(placa);
            unidad.setEstado((EstadoOperativo) cmbEstadoUnidad.getSelectedItem());

            ControlDeItinerario nuevoItinerario = new ControlDeItinerario(
                    null, rutaSeleccionada, unidad, conductorSeleccionado, salida, llegada);

            Optional<String> conflicto = servicio.validarConflictoHorario(nuevoItinerario);
            if (conflicto.isPresent()) {
                JOptionPane.showMessageDialog(this, conflicto.get(), "Horario inválido o en conflicto",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Persistencia en JSON
            boolean exito = servicio.agregarItinerario(nuevoItinerario);

            if (exito) {
                JOptionPane.showMessageDialog(this, "¡Itinerario " + nuevoItinerario.getId()
                        + " guardado exitosamente en JSON!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el itinerario.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar los datos: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
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
            String origen = (it.getRuta() != null && it.getRuta().getOrigen() != null) 
                    ? it.getRuta().getOrigen() : "N/A";
            String destino = (it.getRuta() != null && it.getRuta().getDestino() != null) 
                    ? it.getRuta().getDestino() : "N/A";
            String unidad = (it.getUnidad() != null) ? it.getUnidad().getPlaca() : "N/A";
            String conductor = (it.getConductor() != null) ? it.getConductor().getName() : "N/A";
            String salidaStr = (it.getFechaSalida() != null) ? it.getFechaSalida() : "N/A";
            String llegadaStr = (it.getFechaLlegada() != null) ? it.getFechaLlegada() : "N/A";

            Object[] fila = {
                    it.getId(),
                    idRuta,
                    origen,
                    destino,
                    unidad,
                    conductor,
                    salidaStr,
                    llegadaStr
            };
            modeloTabla.addRow(fila);
        }
    }

    private void limpiarFormulario() {
        txtId.setText(servicio.obtenerSiguienteId());
       
        txtUnidadNumero.setText("");
        txtUnidadPlaca.setText("");
        txtUnidadModelo.setText("");
        txtUnidadCapacidad.setText("");
        txtConductorNombre.setText("");
        txtConductorApellido.setText("");
        txtConductorCedula.setText("");
        txtConductorCorreo.setText("");
        txtConductorPassword.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ControlItineraios().setVisible(true));
    }
}