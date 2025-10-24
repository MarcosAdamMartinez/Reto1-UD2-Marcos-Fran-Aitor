package PracticaParte4;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaPokemon extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    // Campos de texto
    public JTextField textFieldNombre;
    public JTextField textFieldNivel;
    public JTextField textFieldTipo1;
    public JTextField textFieldTipo2;
    public JTextField textFieldFechaCaptura;
    public JTextField textFieldGen;
    public JTextField textFieldRegion;
    public JTextField textFieldGimnasio;

    // Botones
    public JButton btnCargarTodos;
    public JButton btnCargarUno;
    public JButton btnCrear;
    public JButton btnModificar;
    public JButton btnEliminar;

    // Selector para tipo de carga
    private JComboBox<String> comboTipoCarga;

    // Lista y scroll
    private JScrollPane scrollPane;
    public JList<String> jListaPokemons;

    // Modelo de lista y conexión JDBC
    private DefaultListModel<String> modeloLista;
    private PokemonJDBC pokemonJDBC;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                VentanaPokemon frame = new VentanaPokemon();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public VentanaPokemon() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 750, 480);
        setTitle("Gestión Pokédex");

        pokemonJDBC = new PokemonJDBC();
        modeloLista = new DefaultListModel<>();

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        // --- LISTA ---
        scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        jListaPokemons = new JList<>(modeloLista);
        jListaPokemons.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(jListaPokemons);

        // --- PANEL FORMULARIO ---
        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.EAST);
        panel.setLayout(new GridLayout(0, 2, 6, 6));

        panel.add(new JLabel("Nombre:"));
        textFieldNombre = new JTextField();
        panel.add(textFieldNombre);

        panel.add(new JLabel("Nivel:"));
        textFieldNivel = new JTextField();
        panel.add(textFieldNivel);

        panel.add(new JLabel("Tipo 1:"));
        textFieldTipo1 = new JTextField();
        panel.add(textFieldTipo1);

        panel.add(new JLabel("Tipo 2:"));
        textFieldTipo2 = new JTextField();
        panel.add(textFieldTipo2);

        panel.add(new JLabel("Fecha Captura:"));
        textFieldFechaCaptura = new JTextField();
        panel.add(textFieldFechaCaptura);

        panel.add(new JLabel("Generación:"));
        textFieldGen = new JTextField();
        panel.add(textFieldGen);

        panel.add(new JLabel("Región:"));
        textFieldRegion = new JTextField();
        panel.add(textFieldRegion);

        panel.add(new JLabel("Gimnasio:"));
        textFieldGimnasio = new JTextField();
        panel.add(textFieldGimnasio);

        // --- PANEL BOTONES ---
        JPanel panelBotones = new JPanel();
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        btnCargarTodos = new JButton("Cargar Todos");
        btnCargarTodos.setActionCommand("CARGAR");
        btnCargarTodos.addActionListener(this);
        panelBotones.add(btnCargarTodos);

        // 🔹 Nuevo combo y botón para carga individual
        comboTipoCarga = new JComboBox<>(new String[]{"Pokémon", "Región"});
        panelBotones.add(comboTipoCarga);

        btnCargarUno = new JButton("Cargar Uno");
        btnCargarUno.setActionCommand("CARGAR_UNO");
        btnCargarUno.addActionListener(this);
        panelBotones.add(btnCargarUno);

        btnCrear = new JButton("Crear Nuevo");
        btnCrear.setActionCommand("CREAR");
        btnCrear.addActionListener(this);
        panelBotones.add(btnCrear);

        btnModificar = new JButton("Modificar");
        btnModificar.setActionCommand("MODIFICAR");
        btnModificar.addActionListener(this);
        panelBotones.add(btnModificar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setActionCommand("ELIMINAR");
        btnEliminar.addActionListener(this);
        panelBotones.add(btnEliminar);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        String comando = evento.getActionCommand();

        switch (comando) {
            case "CARGAR":
                cargarPokemons();
                break;

            case "CARGAR_UNO":
                cargarUno();
                break;

            case "CREAR":
                crear();
                break;

            case "MODIFICAR":
                modificarPokemon();
                break;

            case "ELIMINAR":
                eliminarPokemon();
                break;
        }
    }

    private void cargarPokemons() {
        modeloLista.clear();
        List<String> lista = pokemonJDBC.obtenerTodo();
        for (String p : lista) {
            modeloLista.addElement(p);
        }
        JOptionPane.showMessageDialog(this, "Pokémon cargados correctamente.");
    }

    private void cargarUno() {
        String tipoSeleccionado = (String) comboTipoCarga.getSelectedItem();
        String nombre = textFieldNombre.getText().trim();
        String region = textFieldRegion.getText().trim();

        if (nombre.isEmpty() && region.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce el nombre para buscar.");
            return;
        }

        modeloLista.clear();

        if ("Pokémon".equals(tipoSeleccionado)) {
            Pokemon p = pokemonJDBC.getPokemonPorNombre(nombre);
            if (p != null) {
                modeloLista.addElement(
                        "Pokémon: " + p.getNombre() + " - Nivel " + p.getNivel() +
                                " | " + p.getTipo1() + " / " + p.getTipo2() +
                                " | Gen " + p.getGen());
                JOptionPane.showMessageDialog(this, "Pokémon cargado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ese Pokémon.");
            }
        } else {
            Region r = pokemonJDBC.getRegionPorNombre(region);
            if (r != null) {
                modeloLista.addElement(
                        "Región: " + r.getNombre() +
                                " | Gimnasio: " + r.getGimnasio() +
                                " | GenJuego: " + r.getNumGen());
                JOptionPane.showMessageDialog(this, "Región cargada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró esa Región.");
            }
        }
    }

    private void crear() {
        String tipoSeleccionado = (String) comboTipoCarga.getSelectedItem();

        try {
            if ("Pokémon".equals(tipoSeleccionado)) {
                // Crear Pokémon
                Pokemon p = new Pokemon();
                p.setNombre(textFieldNombre.getText());
                p.setNivel(Integer.parseInt(textFieldNivel.getText()));
                p.setTipo1(textFieldTipo1.getText());
                p.setTipo2(textFieldTipo2.getText());
                p.setGen(Integer.parseInt(textFieldGen.getText()));
                // fechaCaptura opcional: si quieres la fecha actual
                p.setFechaCaptura(new java.sql.Date(System.currentTimeMillis()));

                boolean okPokemon = pokemonJDBC.crear(p);
                if (okPokemon) {
                    JOptionPane.showMessageDialog(this, "Pokémon creado con éxito.");
                    cargarPokemons();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear el Pokémon.");
                }

            } else if ("Región".equals(tipoSeleccionado)) {
                // Crear Región
                Region r = new Region();
                r.setNumGen(Integer.parseInt(textFieldGen.getText()));
                r.setNombre(textFieldRegion.getText());
                r.setGimnasio(textFieldGimnasio.getText());

                boolean okRegion = pokemonJDBC.crear(r);
                if (okRegion) {
                    JOptionPane.showMessageDialog(this, "Región creada con éxito.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear la Región.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos: " + e.getMessage());
        }
    }


    private void modificarPokemon() {
        try {
            Pokemon p = new Pokemon();
            p.setNombre(textFieldNombre.getText());
            p.setNivel(Integer.parseInt(textFieldNivel.getText()));
            p.setTipo1(textFieldTipo1.getText());
            p.setTipo2(textFieldTipo2.getText());
            p.setGen(Integer.parseInt(textFieldGen.getText()));

            Region r = new Region();
            r.setNumGen(Integer.parseInt(textFieldGen.getText()));
            r.setNombre(textFieldRegion.getText());
            r.setGimnasio(textFieldGimnasio.getText());

            boolean ok = pokemonJDBC.modificarPokemon(p, r);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Pokémon modificado correctamente.");
                cargarPokemons();
            } else {
                JOptionPane.showMessageDialog(this, "Error al modificar el Pokémon.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos: " + e.getMessage());
        }
    }

    private void eliminarPokemon() {
        String nombre = textFieldNombre.getText();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce el nombre del Pokémon a eliminar.");
            return;
        }

        boolean ok = pokemonJDBC.eliminarPokemon(nombre);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Pokémon eliminado correctamente.");
            cargarPokemons();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar el Pokémon.");
        }
    }
}
