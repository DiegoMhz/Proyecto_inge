package view;

import static view.components.Title.createTitlePanel;

import model.entities.SesionUsuario; // <-- Importar SesionUsuario
import model.entities.Usuario;
import model.services.GestorMonedero;
import model.services.Monedero;
import view.components.RadialGradient;

import javax.swing.*;
import java.awt.*;

public class RecargaPagoMovil extends JFrame {

    private final GestorMonedero gestorMonedero;

    private JTextField txtCedula;
    private JTextField txtMonto;
    private JTextField txtReferencia;
    private JLabel lblSaldoActual;

    public RecargaPagoMovil() {
        this.gestorMonedero = new GestorMonedero();
        initUI();
    }

    private void initUI() {
        setTitle("Recarga de Monedero - Pago Móvil");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Maximizar a pantalla completa
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Fondo Degradado Principal
        RadialGradient background = new RadialGradient();
        background.setLayout(new GridBagLayout());
        this.setContentPane(background);

        // Tarjeta Central Semi-Transparente
        JPanel panelTarjeta = new JPanel();
        panelTarjeta.setLayout(new BoxLayout(panelTarjeta, BoxLayout.Y_AXIS));
        panelTarjeta.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        panelTarjeta.setBackground(new Color(255, 255, 255, 35));
        panelTarjeta.setPreferredSize(new Dimension(460, 580));

        // --- ENCABEZADO ---
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setOpaque(false);

        JLabel title = createTitlePanel("RECARGA DE MONEDERO");

        JButton btnVolver = new JButton("🏠");
        btnVolver.setPreferredSize(new Dimension(45, 35));
        btnVolver.setFocusPainted(false);
        btnVolver.addActionListener(e -> {
            new UsuarioHome().setVisible(true);
            this.dispose();
        });

        // Contenedor para evitar que el botón se estire
        JPanel panelBtnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelBtnWrapper.setOpaque(false);
        panelBtnWrapper.add(btnVolver);

        panelTop.add(title, BorderLayout.WEST);
        panelTop.add(panelBtnWrapper, BorderLayout.EAST);
        panelTarjeta.add(panelTop);

        lblSaldoActual = new JLabel();
        lblSaldoActual.setForeground(Color.WHITE);
        lblSaldoActual.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblSaldoActual.setAlignmentX(Component.CENTER_ALIGNMENT);
        actualizarSaldoActual();
        panelTarjeta.add(lblSaldoActual);

        panelTarjeta.add(Box.createVerticalStrut(15));

        // --- DATOS DEL PAGO MÓVIL DESTINO ---
        JPanel panelDatosPago = new JPanel(new GridLayout(4, 1, 4, 4));
        panelDatosPago.setOpaque(false);
        panelDatosPago.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                " Datos para realizar el Pago Móvil ",
                0, 0, new Font("SansSerif", Font.BOLD, 12), Color.WHITE));

        JLabel lblBanco = new JLabel("  Banco: 0102 - Banco de Venezuela");
        JLabel lblCedulaDestino = new JLabel("  CI/RIF: J-123456789");
        JLabel lblTel = new JLabel("  Teléfono: 0414-1234567");
        JLabel lblTitular = new JLabel("  Titular: Sistema de Transporte (SGT)");

        lblBanco.setForeground(Color.WHITE);
        lblCedulaDestino.setForeground(Color.WHITE);
        lblTel.setForeground(Color.WHITE);
        lblTitular.setForeground(Color.WHITE);

        panelDatosPago.add(lblBanco);
        panelDatosPago.add(lblCedulaDestino);
        panelDatosPago.add(lblTel);
        panelDatosPago.add(lblTitular);

        panelTarjeta.add(panelDatosPago);

        panelTarjeta.add(Box.createVerticalStrut(15));

        // --- FORMULARIO DE RECARGA ---
        JPanel panelForm = new JPanel(new GridLayout(3, 2, 10, 12));
        panelForm.setOpaque(false);

        JLabel lblCedulaUser = new JLabel("Cédula de Usuario:");
        lblCedulaUser.setForeground(Color.WHITE);
        lblCedulaUser.setFont(new Font("SansSerif", Font.BOLD, 13));

        // OBTENER EL USUARIO DE LA SESIÓN GLOBAL
        Usuario usuarioActual = SesionUsuario.getUsuarioLogueado();
        String cedulaTexto = (usuarioActual != null && usuarioActual.getCedula() != null) 
                ? usuarioActual.getCedula() 
                : "V-00000000";

        txtCedula = new JTextField(cedulaTexto);
        txtCedula.setEditable(false); // BLOQUEADO: No se puede editar por el usuario
        txtCedula.setFocusable(false);

        JLabel lblMontoUser = new JLabel("Monto a Recargar (BS):");
        lblMontoUser.setForeground(Color.WHITE);
        lblMontoUser.setFont(new Font("SansSerif", Font.BOLD, 13));
        txtMonto = new JTextField();

        JLabel lblRefUser = new JLabel("N° de Referencia:");
        lblRefUser.setForeground(Color.WHITE);
        lblRefUser.setFont(new Font("SansSerif", Font.BOLD, 13));
        txtReferencia = new JTextField();

        panelForm.add(lblCedulaUser);
        panelForm.add(txtCedula);
        panelForm.add(lblMontoUser);
        panelForm.add(txtMonto);
        panelForm.add(lblRefUser);
        panelForm.add(txtReferencia);

        panelTarjeta.add(panelForm);

        panelTarjeta.add(Box.createVerticalStrut(20));

        // --- BOTÓN PROCESAR PAGO ---
        JButton btnPagar = new JButton("Procesar y Pagar");
        btnPagar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnPagar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPagar.setPreferredSize(new Dimension(200, 40));
        btnPagar.setMaximumSize(new Dimension(380, 40));
        btnPagar.setFocusPainted(false);

        btnPagar.addActionListener(e -> procesarPago());

        panelTarjeta.add(btnPagar);

        background.add(panelTarjeta);
    }

    private void procesarPago() {
        String cedula = txtCedula.getText().trim();
        String montoStr = txtMonto.getText().trim();
        String referencia = txtReferencia.getText().trim();

        // Validaciones básicas
        if (montoStr.isEmpty() || referencia.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, complete todos los campos obligatorios.",
                    "Campos Incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr.replace(",", "."));
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this,
                        "El monto debe ser mayor a 0.",
                        "Monto Inválido",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un número válido en el monto.",
                    "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear monedero automáticamente si no existe aún para este usuario
        Monedero m = gestorMonedero.buscarPorCedula(cedula);
        if (m == null) {
            gestorMonedero.crearMonedero(cedula, 0.0);
        }

        // Realizar la recarga
        boolean exito = gestorMonedero.recargarMonedero(cedula, monto, referencia);

        if (exito) {
            Monedero monederoActualizado = gestorMonedero.buscarPorCedula(cedula);
            double nuevoSaldo = (monederoActualizado != null) ? monederoActualizado.getSaldo() : monto;

            JOptionPane.showMessageDialog(this,
                    "¡Pago verificado y recarga exitosa!\n\n" +
                            "👤 Cédula: " + cedula + "\n" +
                            "💵 Monto Recargado: " + String.format("%.2f", monto) + " BS\n" +
                            "🔑 Referencia: " + referencia + "\n" +
                            "💰 Saldo Actual: " + String.format("%.2f", nuevoSaldo) + " BS",
                    "Recarga Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);

            actualizarSaldoActual();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo completar la recarga. Verifique la referencia.",
                    "Error de Procesamiento",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarSaldoActual() {
        Usuario usuarioActual = SesionUsuario.getUsuarioLogueado();
        String cedula = usuarioActual == null ? null : usuarioActual.getCedula();
        Monedero monedero = cedula == null ? null : gestorMonedero.buscarPorCedula(cedula);
        double saldo = monedero == null ? 0.0 : monedero.getSaldo();
        lblSaldoActual.setText("Monto actual del usuario: "
                + String.format(java.util.Locale.US, "%.2f", saldo) + " BS");
    }

    private void limpiarFormulario() {
        txtMonto.setText("");
        txtReferencia.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}