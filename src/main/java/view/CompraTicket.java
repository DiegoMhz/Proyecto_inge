package view;

import static view.components.Title.createTitlePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import model.entities.ControlDeItinerario;
import model.entities.Municipio;
import model.entities.RolUsuario;
import model.entities.SesionUsuario;
import model.entities.Usuario;
import model.services.GestorDeTicket;
import model.services.GestorMonedero;
import model.services.ItinerarioService;
import model.services.Monedero;
import model.services.TarifaService;
import view.components.RadialGradient;

public class CompraTicket extends JFrame {

    private final TarifaService tarifaService;
    private final ItinerarioService itinerarioService;
    private final GestorMonedero gestorMonedero;
    private final GestorDeTicket gestorDeTicket;
    private final DefaultTableModel modeloItinerarios;
    private final JTable tablaItinerarios;
    private final JLabel lblSaldo;
    private final JLabel lblResumenCompra;
    private final JButton btnPagar;
    private List<ControlDeItinerario> itinerarios;

    public CompraTicket() {
        tarifaService = new TarifaService();
        itinerarioService = new ItinerarioService(tarifaService);
        gestorMonedero = new GestorMonedero();
        gestorDeTicket = new GestorDeTicket(gestorMonedero);
        modeloItinerarios = new DefaultTableModel(
                new String[] { "Ruta", "Origen", "Destino", "Conductor", "Unidad", "Salida", "Llegada", "Paradas" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaItinerarios = new JTable(modeloItinerarios);
        lblSaldo = new JLabel();
        lblResumenCompra = new JLabel("Selecciona un itinerario para calcular el monto.");
        btnPagar = new JButton("Pagar y comprar ticket");

        initUI();
        actualizarSaldo();
        cargarItinerarios();
    }

    private void initUI() {
        setTitle("Compra de Ticket");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        RadialGradient background = new RadialGradient();
        background.setLayout(new GridBagLayout());
        setContentPane(background);

        JPanel tarjeta = new JPanel(new BorderLayout(15, 15));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        tarjeta.setBackground(new Color(255, 255, 255, 45));
        tarjeta.setPreferredSize(new Dimension(1100, 680));

        JPanel encabezado = new JPanel(new BorderLayout(10, 10));
        encabezado.setOpaque(false);
        encabezado.add(createTitlePanel("COMPRA DE TICKETS"), BorderLayout.WEST);

        JButton btnInicio = new JButton("Inicio");
        btnInicio.setFocusPainted(false);
        btnInicio.addActionListener(e -> {
            new UsuarioHome().setVisible(true);
            dispose();
        });
        encabezado.add(btnInicio, BorderLayout.EAST);

        JPanel panelEstado = new JPanel(new GridLayout(2, 1, 0, 8));
        panelEstado.setOpaque(false);
        lblSaldo.setForeground(Color.WHITE);
        lblSaldo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblResumenCompra.setForeground(Color.WHITE);
        lblResumenCompra.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblResumenCompra.setOpaque(true);
        lblResumenCompra.setBackground(new Color(47, 173, 116));
        panelEstado.add(lblSaldo);
        panelEstado.add(lblResumenCompra);

        JPanel panelSuperior = new JPanel(new BorderLayout(0, 12));
        panelSuperior.setOpaque(false);
        panelSuperior.add(encabezado, BorderLayout.NORTH);
        panelSuperior.add(panelEstado, BorderLayout.SOUTH);

        tablaItinerarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaItinerarios.setFillsViewportHeight(true);
        tablaItinerarios.setOpaque(true);
        tablaItinerarios.setBackground(Color.WHITE);
        tablaItinerarios.setDoubleBuffered(true);
        tablaItinerarios.getSelectionModel().addListSelectionListener(this::seleccionCambio);
        JScrollPane scroll = new JScrollPane(tablaItinerarios);
        scroll.setOpaque(true);
        scroll.getViewport().setOpaque(true);
        scroll.getViewport().setBackground(Color.WHITE);

        JButton btnActualizar = new JButton("Actualizar itinerarios");
        btnActualizar.addActionListener(e -> cargarItinerarios());

        btnPagar.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnPagar.setBackground(new Color(46, 139, 87));
        btnPagar.setForeground(Color.WHITE);
        btnPagar.setFocusPainted(false);
        btnPagar.setEnabled(false);
        btnPagar.addActionListener(e -> pagarTicket());

        JPanel panelAcciones = new JPanel(new BorderLayout(10, 0));
        panelAcciones.setOpaque(false);
        panelAcciones.add(btnActualizar, BorderLayout.WEST);
        panelAcciones.add(btnPagar, BorderLayout.EAST);

        tarjeta.add(panelSuperior, BorderLayout.NORTH);
        tarjeta.add(scroll, BorderLayout.CENTER);
        tarjeta.add(panelAcciones, BorderLayout.SOUTH);
        background.add(tarjeta);
    }

    private void cargarItinerarios() {
        modeloItinerarios.setRowCount(0);
        itinerarios = itinerarioService.obtenerTodos().stream()
                .filter(itinerario -> itinerario != null && itinerario.getRuta() != null)
                .toList();
        for (ControlDeItinerario itinerario : itinerarios) {
            var ruta = itinerario.getRuta();
            String conductor = itinerario.getConductor() == null
                    ? "No asignado"
                    : textoOr(itinerario.getConductor().getName(), "No asignado");
            String unidad = itinerario.getUnidad() == null
                    ? "No asignada"
                    : textoOr(itinerario.getUnidad().getPlaca(), "No asignada");
            String paradas = ruta.getParadas() == null || ruta.getParadas().isEmpty()
                    ? "Sin paradas"
                    : String.join(", ", ruta.getParadas());
            modeloItinerarios.addRow(new Object[] {
                    textoOr(ruta.getNombreRuta(), "Ruta sin nombre"),
                    textoOr(ruta.getOrigen(), "N/A"),
                    textoOr(ruta.getDestino(), "N/A"),
                    conductor,
                    unidad,
                    textoOr(itinerario.getFechaSalida(), "N/A"),
                    textoOr(itinerario.getFechaLlegada(), "N/A"),
                    paradas
            });
        }
        tablaItinerarios.clearSelection();
        actualizarResumen();
    }

    private void seleccionCambio(ListSelectionEvent evento) {
        if (!evento.getValueIsAdjusting()) {
            actualizarResumen();
        }
    }

    private void actualizarResumen() {
        Optional<ControlDeItinerario> seleccion = obtenerItinerarioSeleccionado();
        if (seleccion.isEmpty()) {
            lblResumenCompra.setText("Selecciona un itinerario para calcular el monto.");
            btnPagar.setEnabled(false);
            return;
        }

        Optional<CalculoTarifa> calculo = calcularTarifa(seleccion.get());
        if (calculo.isEmpty()) {
            lblResumenCompra.setText("No se puede calcular: revisa municipio y tarifa registrados.");
            btnPagar.setEnabled(false);
            return;
        }

        int asientos = seleccion.get().getUnidad() == null
                ? 0
                : seleccion.get().getUnidad().getAsientosDisponibles();
        lblResumenCompra.setText(String.format(
                java.util.Locale.US,
                "Tarifa: %.2f BS | Paga: %d%% | Total: %.2f BS | Asientos disponibles: %d",
                calculo.get().tarifaMunicipal(), calculo.get().porcentajeAplicado(),
                calculo.get().monto(), asientos));
        btnPagar.setEnabled(asientos > 0 && SesionUsuario.getUsuarioLogueado() != null);
        if (asientos <= 0) {
            lblResumenCompra.setText(lblResumenCompra.getText() + " — SIN ASIENTOS");
        }
    }

    private Optional<CalculoTarifa> calcularTarifa(ControlDeItinerario itinerario) {
        if (itinerario.getRuta() == null || itinerario.getRuta().getMunicipios() == null
                || itinerario.getRuta().getMunicipios().size() != 1) {
            return Optional.empty();
        }

        String nombreMunicipio = itinerario.getRuta().getMunicipios().get(0);
        if (nombreMunicipio == null || nombreMunicipio.isBlank()) {
            return Optional.empty();
        }
        Optional<Municipio> municipio = tarifaService.obtenerMunicipios().stream()
                .filter(item -> item != null && item.getMunicipioNombre() != null
                        && item.getMunicipioNombre().equalsIgnoreCase(nombreMunicipio.trim()))
                .findFirst();
        if (municipio.isEmpty() || !Float.isFinite(municipio.get().getMunicipioTarifa())
                || municipio.get().getMunicipioTarifa() < 0) {
            return Optional.empty();
        }

        Usuario usuario = SesionUsuario.getUsuarioLogueado();
        if (usuario == null || usuario.getRol() == null) {
            return Optional.empty();
        }

        int porcentajeAplicado;
        if (usuario.getRol() == RolUsuario.ESTUDIANTE) {
            porcentajeAplicado = tarifaService.getDescuentoEstudiante();
        } else if (usuario.getRol() == RolUsuario.EMPLEADO_PROFESOR) {
            porcentajeAplicado = tarifaService.getDescuentoEmpleado();
        } else if (usuario.getRol() == RolUsuario.EXTERNO) {
            porcentajeAplicado = 100;
        } else {
            return Optional.empty();
        }

        double monto = BigDecimal.valueOf(municipio.get().getMunicipioTarifa())
                .multiply(BigDecimal.valueOf(porcentajeAplicado))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                .doubleValue();
        return Optional.of(new CalculoTarifa(
                municipio.get().getMunicipioTarifa(), porcentajeAplicado, monto));
    }

    private void pagarTicket() {
        Optional<ControlDeItinerario> seleccion = obtenerItinerarioSeleccionado();
        Usuario usuario = SesionUsuario.getUsuarioLogueado();
        if (seleccion.isEmpty() || usuario == null || usuario.getCedula() == null) {
            JOptionPane.showMessageDialog(this,
                    "Inicia sesión y selecciona un itinerario para continuar.",
                    "Compra incompleta", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idItinerario = seleccion.get().getId();
        Optional<ControlDeItinerario> itinerarioActual = itinerarioService.buscarPorId(idItinerario);
        if (itinerarioActual.isEmpty() || itinerarioActual.get().getUnidad() == null) {
            JOptionPane.showMessageDialog(this,
                    "El itinerario o su unidad ya no están disponibles.",
                    "Itinerario no disponible", JOptionPane.ERROR_MESSAGE);
            cargarItinerarios();
            return;
        }
        if (itinerarioActual.get().getUnidad().getAsientosDisponibles() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Este itinerario ya no tiene asientos disponibles.",
                    "Sin asientos", JOptionPane.WARNING_MESSAGE);
            cargarItinerarios();
            return;
        }

        Optional<CalculoTarifa> calculo = calcularTarifa(itinerarioActual.get());
        if (calculo.isEmpty() || calculo.get().monto() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo calcular una tarifa válida para este itinerario.",
                    "Tarifa no disponible", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Monedero monedero = gestorMonedero.buscarPorCedula(usuario.getCedula());
        if (monedero == null || monedero.getSaldo() < calculo.get().monto()) {
            JOptionPane.showMessageDialog(this,
                    "No tienes saldo suficiente en el monedero para pagar este ticket.",
                    "Saldo insuficiente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                String.format(java.util.Locale.US,
                        "¿Confirmas la compra por %.2f BS?%nAsientos disponibles: %d",
                        calculo.get().monto(), itinerarioActual.get().getUnidad().getAsientosDisponibles()),
                "Confirmar compra", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        if (!itinerarioService.reservarAsiento(idItinerario)) {
            JOptionPane.showMessageDialog(this,
                    "No fue posible reservar el asiento. Es posible que otro usuario haya comprado el último.",
                    "Asiento no disponible", JOptionPane.WARNING_MESSAGE);
            cargarItinerarios();
            return;
        }

        boolean compraExitosa = gestorDeTicket.procesarCompraTicket(
                usuario.getCedula(),
                itinerarioActual.get().getRuta(),
                usuario.getRol().name(),
                calculo.get().monto());
        if (!compraExitosa) {
            if (!itinerarioService.liberarAsiento(idItinerario)) {
                System.err.println("No se pudo liberar el asiento después de fallar el pago del itinerario " + idItinerario);
            }
            JOptionPane.showMessageDialog(this,
                    "No se pudo completar el pago. No se cobró el ticket.",
                    "Error de pago", JOptionPane.ERROR_MESSAGE);
            cargarItinerarios();
            actualizarSaldo();
            return;
        }

        JOptionPane.showMessageDialog(this,
                String.format(java.util.Locale.US, "Compra exitosa. Se cobraron %.2f BS.", calculo.get().monto()),
                "Ticket comprado", JOptionPane.INFORMATION_MESSAGE);
        actualizarSaldo();
        cargarItinerarios();
    }

    private Optional<ControlDeItinerario> obtenerItinerarioSeleccionado() {
        int filaSeleccionada = tablaItinerarios.getSelectedRow();
        if (filaSeleccionada < 0 || filaSeleccionada >= itinerarios.size()) {
            return Optional.empty();
        }
        ControlDeItinerario itinerario = itinerarios.get(filaSeleccionada);
        return itinerario == null ? Optional.empty() : Optional.of(itinerario);
    }

    private void actualizarSaldo() {
        Usuario usuario = SesionUsuario.getUsuarioLogueado();
        Monedero monedero = usuario == null ? null : gestorMonedero.buscarPorCedula(usuario.getCedula());
        double saldo = monedero == null ? 0 : monedero.getSaldo();
        lblSaldo.setText(String.format(java.util.Locale.US, "Saldo del monedero: %.2f BS", saldo));
    }

    private String textoOr(String texto, String alternativo) {
        return texto == null || texto.isBlank() ? alternativo : texto;
    }

    private record CalculoTarifa(double tarifaMunicipal, int porcentajeAplicado, double monto) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CompraTicket().setVisible(true));
    }
}
