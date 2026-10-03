package view;

import java.util.List;
import static view.components.Title.createTitlePanel;
import static view.components.InputFactory.createInputPanel;
import static view.components.Button.createButton;
import model.GestorDeFlota;
import model.EstadoOperativo;
import model.UnidadDeTransporte;
import view.components.ItemUnidadPanel;
import view.components.RadialGradient;
import javax.swing.*;
import java.awt.*;

public class GestionDeFlota extends JFrame {
    private JTextField inputPlaca;
    private JTextField inputModelo;
    private JComboBox<String> inputEstadoOperativo;
    private JTextField inputCapacidad;

    public GestionDeFlota() {
        initUI();
    }

    private void initUI() {
        // Configuración de la ventana principal
        setTitle("Registro De Unidades");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // MAXIMIZAR LA VENTANA AL INICIAR
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setPreferredSize(new Dimension(420, 350));
        panel.setBackground(new Color(255, 255, 255, 40));

        // PONERLE EL FONDO DEGRADADO CON COLOR
        RadialGradient background = new RadialGradient();
        background.setLayout(new GridLayout(2, 1, 20, 20));
        background.setBorder(BorderFactory.createEmptyBorder(40, 200, 40, 200));
        this.setContentPane(background);
        background.add(panel);

        // TITULO
        JLabel title = createTitlePanel("Registro De Unidades");
        panel.add(title);

        // INPUT PLACA
        inputPlaca = new JTextField();
        panel.add(createInputPanel("Placa", inputPlaca));

        // INPUT MODEL
        inputModelo = new JTextField();
        panel.add(createInputPanel("Modelo", inputModelo));

        // INPUT ESTADO OPERATIVO
        inputEstadoOperativo = new JComboBox<>();
        for (EstadoOperativo estado : EstadoOperativo.values()) {
            inputEstadoOperativo.addItem(estado.name());
        }
        panel.add(createInputPanel("Estado Operativo", inputEstadoOperativo));

        // INPUT CAPACIDAD
        inputCapacidad = new JTextField();
        panel.add(createInputPanel("Capacidad", inputCapacidad));

        // CREO UN DIV O CAJA PARA LOS BOTONES
        JPanel div = new JPanel(new GridLayout(2, 1, 0, 10));
        JButton btnRegistro = createButton("Registrar Unidad");
        div.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        div.add(btnRegistro);
        panel.add(div);
        div.setOpaque(false);

        // CREO UN PARA LAS UNIDADES REGISTRADAS
        JPanel unidades = new JPanel();
        unidades.setLayout(new BoxLayout(unidades, BoxLayout.Y_AXIS));
        unidades.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        unidades.setBackground(new Color(255, 255, 255, 40));
        JLabel titleUnidades = createTitlePanel("Unidades Registradas");
        unidades.add(titleUnidades);

        background.add(unidades);

        JPanel listaUnidades = new JPanel();
        listaUnidades.setLayout(new BoxLayout(listaUnidades, BoxLayout.Y_AXIS));
        listaUnidades.setOpaque(false);

        // Espacio entre el título y la primera fila
        unidades.add(Box.createVerticalStrut(20));
        unidades.add(listaUnidades);

        GestorDeFlota gestor = new GestorDeFlota();
        List<UnidadDeTransporte> unidadesAll = gestor.getUnidades();

        for (UnidadDeTransporte unidad : unidadesAll) {
            JPanel fila = new ItemUnidadPanel(unidad.getPlaca());
            listaUnidades.add(fila);
        }

        // 1. Crear el JScrollPane
        JScrollPane scrollPane = new JScrollPane(listaUnidades);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        unidades.add(scrollPane);

        btnRegistro.addActionListener(e -> {
            String placa = inputPlaca.getText().trim();
            String modelo = inputModelo.getText().trim();
            String capacidad = inputCapacidad.getText().trim();

            if (placa.isEmpty() || modelo.isEmpty() || capacidad.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            ItemUnidadPanel newItem = new ItemUnidadPanel(placa);
            listaUnidades.add(newItem);
            listaUnidades.revalidate();
            listaUnidades.repaint();
            GestorDeFlota gestorDeFlota = new GestorDeFlota();

            int capacidadInt = Integer.parseInt(capacidad);
            gestorDeFlota.RegistrarUnidad(placa, modelo, capacidadInt);
        });
    }

}