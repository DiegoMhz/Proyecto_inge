package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.ControlDeItinerario;
import model.EstadoOperativo;
import model.ItinerarioService;
import model.RolUsuario;
import model.Ruta;
import model.UnidadDeTransporte;
import model.Usuario;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ControlItineraios extends JFrame {

    private final ItinerarioService servicio;

    // Componentes del Formulario
    private JTextField txtId;
    private JTextField txtRutaNombre, txtRutaOrigen, txtRutaDestino, txtParadas;
    private JComboBox<Ruta.TipoRuta> cmbTipoRuta;
    private JTextField txtUnidadNumero, txtUnidadPlaca, txtUnidadModelo, txtUnidadCapacidad;
    private JComboBox<EstadoOperativo> cmbEstadoUnidad;
    private JTextField txtConductorNombre, txtConductorApellido, txtConductorCedula, txtConductorCorreo;
    private JPasswordField txtConductorPassword;
    private JTextField txtHoraSalida, txtHoraLlegada;

    // Componentes de la Tabla
    private JTable tablaItinerarios;
    private DefaultTableModel modeloTabla;

    public ControlItineraios() {
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
        cargarDatosTabla();
    }

    /**
     * Pestaña 1: Formulario de entrada de datos
     */
    private JPanel crearPanelFormulario() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelForm = new JPanel(new GridLayout(0, 2, 8, 8));

        // Campos del formulario
        txtId = new JTextField(servicio.obtenerSiguienteId());
        txtId.setEditable(false);
        txtRutaNombre = new JTextField();
        txtRutaOrigen = new JTextField();
        txtRutaDestino = new JTextField();
        txtParadas = new JTextField();
        cmbTipoRuta = new JComboBox<>(Ruta.TipoRuta.values());
        txtUnidadNumero = new JTextField();
        txtUnidadPlaca = new JTextField();
        txtUnidadModelo = new JTextField();
        txtUnidadCapacidad = new JTextField();
        cmbEstadoUnidad = new JComboBox<>(EstadoOperativo.values());
        txtConductorNombre = new JTextField();
        txtConductorApellido = new JTextField();
        txtConductorCedula = new JTextField();
        txtConductorCorreo = new JTextField();
        txtConductorPassword = new JPasswordField();
        txtHoraSalida = new JTextField("08:00");
        txtHoraLlegada = new JTextField("09:30");

        // Agregar etiquetas y campos
        panelForm.add(new JLabel("ID Itinerario:"));
        panelForm.add(txtId);

        panelForm.add(new JLabel("--- UNIDAD DE TRANSPORTE ---"));
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel("Placa:"));
        panelForm.add(txtUnidadPlaca);

        panelForm.add(new JLabel("--- RUTA ---"));
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel("Nombre de ruta:"));
        panelForm.add(txtRutaNombre);
        panelForm.add(new JLabel("Origen:"));
        panelForm.add(txtRutaOrigen);
        panelForm.add(new JLabel("Destino:"));
        panelForm.add(txtRutaDestino);
        panelForm.add(new JLabel("Tipo de ruta:"));
        panelForm.add(cmbTipoRuta);
        panelForm.add(new JLabel("Paradas (separadas por comas):"));
        panelForm.add(txtParadas);

        panelForm.add(new JLabel("--- CONDUCTOR ---"));
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtConductorNombre);
        panelForm.add(new JLabel("Apellido:"));
        panelForm.add(txtConductorApellido);
        panelForm.add(new JLabel("Cédula:"));
        panelForm.add(txtConductorCedula);
        panelForm.add(new JLabel("Correo:"));
        panelForm.add(txtConductorCorreo);
        panelForm.add(new JLabel("Contraseña:"));
        panelForm.add(txtConductorPassword);

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

        String[] columnas = {"ID", "Ruta", "Origen-Destino", "Unidad", "Conductor", "Salida", "Llegada"};
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
                JOptionPane.showMessageDialog(this, "La salida y llegada estimada son obligatorias.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Ruta ruta = new Ruta(
                txtRutaNombre.getText().trim(),
                txtRutaOrigen.getText().trim(),
                txtRutaDestino.getText().trim(),
                (Ruta.TipoRuta) cmbTipoRuta.getSelectedItem()
            );
            List<String> paradas = Arrays.stream(txtParadas.getText().split(","))
                .map(String::trim)
                .filter(parada -> !parada.isEmpty())
                .toList();
            ruta.setParadas(paradas);

            UnidadDeTransporte unidad = new UnidadDeTransporte();
            String placa = txtUnidadPlaca.getText().trim();
            unidad = unidad.getUnidadDeTransporte(placa);
            unidad.setEstado((EstadoOperativo) cmbEstadoUnidad.getSelectedItem());

            Usuario conductor = new Usuario(
                RolUsuario.CONDUCTOR,
                txtConductorNombre.getText().trim(),
                txtConductorApellido.getText().trim(),
                txtConductorCedula.getText().trim(),
                txtConductorCorreo.getText().trim(),
                new String(txtConductorPassword.getPassword())
            );

            ControlDeItinerario nuevoItinerario = new ControlDeItinerario(
                null, ruta, unidad, conductor, salida, llegada
            );

            Optional<String> conflicto = servicio.validarConflictoHorario(nuevoItinerario);
            if (conflicto.isPresent()) {
                JOptionPane.showMessageDialog(this, conflicto.get(), "Horario inválido o en conflicto", JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "No se pudo guardar el itinerario.", "Error", JOptionPane.ERROR_MESSAGE);
            }

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
            String conductor = (it.getConductor() != null) ? it.getConductor().getName() : "N/A";
            String salidaStr = (it.getFechaSalida() != null) ? it.getFechaSalida() : "N/A";
            String llegadaStr = (it.getFechaLlegada() != null) ? it.getFechaLlegada() : "N/A";

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
        txtId.setText(servicio.obtenerSiguienteId());
        txtRutaNombre.setText("");
        txtRutaOrigen.setText("");
        txtRutaDestino.setText("");
        txtParadas.setText("");
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