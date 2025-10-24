package PracticaParte1.gestionAlumnos.UI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import PracticaParte1.gestionAlumnos.Model.Alumno;
import PracticaParte1.gestionAlumnos.Model.ModeloAlumnosJDBC;

public class VentanaAlumnos extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    public JTextField textFieldDNI;
    public JTextField textFieldNombre;
    public JTextField textFieldApellidos;
    public JTextField textFieldCP;
    public JButton btnCargarTodos;
    public JButton btnCrear;
    public JButton btnModificar;
    public JButton btnEliminar;
    private JScrollPane scrollPane;
    public JList<String> jListaAlumnos;

    // 🔗 Conexión al modelo
    private ModeloAlumnosJDBC modelo;
    private DefaultListModel<String> modeloLista;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                VentanaAlumnos frame = new VentanaAlumnos();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public VentanaAlumnos() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        setTitle("Gestión de Alumnos");

        modelo = new ModeloAlumnosJDBC();
        modeloLista = new DefaultListModel<>();

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        // --- LISTA ---
        scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        jListaAlumnos = new JList<>(modeloLista);
        jListaAlumnos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(jListaAlumnos);

        // --- PANEL DATOS ---
        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.EAST);
        panel.setLayout(new GridLayout(0, 2, 5, 5));

        panel.add(new JLabel("DNI:"));
        textFieldDNI = new JTextField();
        panel.add(textFieldDNI);

        panel.add(new JLabel("Nombre:"));
        textFieldNombre = new JTextField();
        panel.add(textFieldNombre);

        panel.add(new JLabel("Apellidos:"));
        textFieldApellidos = new JTextField();
        panel.add(textFieldApellidos);

        panel.add(new JLabel("CP:"));
        textFieldCP = new JTextField();
        panel.add(textFieldCP);

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
                cargarAlumnos();
                break;
            case "CREAR":
                crearAlumno();
                break;
            case "MODIFICAR":
                modificarAlumno();
                break;
            case "ELIMINAR":
                eliminarAlumno();
                break;
        }
    }

    // === FUNCIONES ===

    private void cargarAlumnos() {
        modeloLista.clear();
        List<String> lista = modelo.getAll();
        for (String a : lista) {
            modeloLista.addElement(a);
        }
        JOptionPane.showMessageDialog(this, "Alumnos cargados correctamente.");
    }

    private void crearAlumno() {
        Alumno a = new Alumno();
        a.setDNI(textFieldDNI.getText());
        a.setNombre(textFieldNombre.getText());
        a.setApellidos(textFieldApellidos.getText());
        a.setCP(textFieldCP.getText());

        boolean ok = modelo.crearAlumno(a);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Alumno creado correctamente.");
            cargarAlumnos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al crear el alumno.");
        }
    }

    private void modificarAlumno() {
        Alumno a = new Alumno();
        a.setDNI(textFieldDNI.getText());
        a.setNombre(textFieldNombre.getText());
        a.setApellidos(textFieldApellidos.getText());
        a.setCP(textFieldCP.getText());

        boolean ok = modelo.modificarAlumno(a);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Alumno modificado correctamente.");
            cargarAlumnos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar el alumno.");
        }
    }

    private void eliminarAlumno() {
        String dni = textFieldDNI.getText();
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce el DNI del alumno a eliminar.");
            return;
        }

        boolean ok = modelo.eliminarAlumno(dni);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Alumno eliminado correctamente.");
            cargarAlumnos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar el alumno.");
        }
    }
}
