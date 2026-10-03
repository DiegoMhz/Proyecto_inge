package view;

import static view.components.Title.createTitlePanel;
import static view.components.InputFactory.createInputPanel;
import static view.components.Button.createButton;
import model.EstadoOperativo;
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

        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Controla el alto de la fila

        
        JLabel label = createTitlePanel("Placa");
      

        
        JPanel panelTexto = new JPanel(new BorderLayout());
        panelTexto.setBackground(new Color(255, 255, 255, 30));
        panelTexto.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        panelTexto.add(label, BorderLayout.CENTER);

        // Botones (Editar y Eliminar)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotones.setOpaque(false);

        JButton btnEditar = new JButton("✏");
        JButton btnEliminar = new JButton("🗑");

        btnEditar.setFocusable(false);
        btnEliminar.setFocusable(false);

        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        fila.add(panelTexto, BorderLayout.CENTER);
        fila.add(panelBotones, BorderLayout.EAST);

        listaUnidades.add(fila);
    }

}