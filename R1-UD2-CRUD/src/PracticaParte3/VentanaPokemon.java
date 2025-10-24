package PracticaParte3;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
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

    // Botones
    public JButton btnCargarTodos;
    public JButton btnCrear;
    public JButton btnModificar;
    public JButton btnEliminar;

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
        setBounds(100, 100, 600, 400);
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
        panel.setLayout(new GridLayout(0, 2, 5, 5));

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

        // --- PANEL BOTONES ---
        JPanel panelBotones = new JPanel();
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        btnCargarTodos = new JButton("Cargar Todos");
        btnCargarTodos.setActionCommand("CARGAR");
        btnCargarTodos.addActionListener(this);
        panelBotones.add(btnCargarTodos);

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

            case "CREAR":
                crearPokemon();
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

    private void crearPokemon() {
        try {
            Pokemon p = new Pokemon();
            p.setNombre(textFieldNombre.getText());
            p.setNivel(Integer.parseInt(textFieldNivel.getText()));
            p.setTipo1(textFieldTipo1.getText());
            p.setTipo2(textFieldTipo2.getText());

            boolean ok = pokemonJDBC.crearPokemon(p);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Pokémon creado con éxito.");
                cargarPokemons();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear el Pokémon.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos: " + e.getMessage());
        }
    }

    private void modificarPokemon() {
        String nombre = textFieldNombre.getText();
        Pokemon p = new Pokemon();
        p.setNombre(nombre);
        p.setNivel(Integer.parseInt(textFieldNivel.getText()));
        p.setTipo1(textFieldTipo1.getText());
        p.setTipo2(textFieldTipo2.getText());

        boolean ok = pokemonJDBC.modificarPokemon(p);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Pokémon modificado correctamente.");
            cargarPokemons();
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar el Pokémon.");
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
