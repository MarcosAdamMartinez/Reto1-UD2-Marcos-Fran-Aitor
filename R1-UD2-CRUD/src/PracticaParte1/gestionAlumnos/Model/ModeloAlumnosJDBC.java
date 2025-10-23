package PracticaParte1.gestionAlumnos.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModeloAlumnosJDBC implements IModeloAlumnos {

	private static String cadenaConexion = "jdbc:mysql://localhost:3306/instituto";
	private static String user = "dam2";
	private static String pass = "asdf.1234";

	/*
	 * 1.Implementar los métodos vacíos de la clase ModeloAlumnosJDBC en el proyecto 
	 * PracticaParte1.gestionAlumnos para implementar el CRUD de la entidad Alumno.
	 */
	public ModeloAlumnosJDBC() {
		try {
			// Carga explícitamente el driver JDBC de MySQL en memoria.
			// Esto asegura que el controlador se registre en DriverManager
			// y permita establecer conexiones con la base de datos mediante JDBC.
			Class.forName("com.mysql.cj.jdbc.Driver"); // Asegura el driver cargado
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// esto se usa para conectar a la base de datos
	private Connection conectar() throws SQLException {
		return DriverManager.getConnection(cadenaConexion, user, pass);
	}

	@Override
	// devuelve una lista de cadenas con los datos de todos los alumnos
	public List<String> getAll() {
		List<String> alumnos = new ArrayList<>();
		String sql = "SELECT * FROM alumnos";

		try (Connection conn = conectar();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				String alumnoStr = rs.getString("dni") + " - "
						+ rs.getString("nombre") + " "
						+ rs.getString("apellidos") + " - CP: "
						+ rs.getString("cp");
				alumnos.add(alumnoStr);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return alumnos;
	}

	@Override
	// devuelve un objeto Alumno con los datos del alumno cuyo DNI se pasa como parámetro
	public Alumno getAlumnoPorDNI(String DNI) {
		Alumno alumno = null;
		String sql = "SELECT * FROM alumnos WHERE dni = ?";

		try (Connection conn = conectar();
			 PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, DNI);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				alumno = new Alumno();
				alumno.setDNI(rs.getString("dni"));
				alumno.setNombre(rs.getString("nombre"));
				alumno.setApellidos(rs.getString("apellidos"));
				alumno.setCP(rs.getString("cp"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return alumno;
	}

	@Override
	// modifica los datos de un alumno en la base de datos
	public boolean modificarAlumno(Alumno alumno) {
		String sql = "UPDATE alumnos SET nombre = ?, apellidos = ?, cp = ? WHERE dni = ?";

		try (Connection conn = conectar();
			 PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, alumno.getNombre());
			ps.setString(2, alumno.getApellidos());
			ps.setString(3, alumno.getCP());
			ps.setString(4, alumno.getDNI());

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	// elimina un alumno de la base de datos según su DNI
	public boolean eliminarAlumno(String DNI) {
		String sql = "DELETE FROM alumnos WHERE dni = ?";

		try (Connection conn = conectar();
			 PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, DNI);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	// crea un nuevo alumno en la base de datos
	public boolean crearAlumno(Alumno alumno) {
		String sql = "INSERT INTO alumnos (dni, nombre, apellidos, cp) VALUES (?, ?, ?, ?)";

		try (Connection conn = conectar();
			 PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, alumno.getDNI());
			ps.setString(2, alumno.getNombre());
			ps.setString(3, alumno.getApellidos());
			ps.setString(4, alumno.getCP());

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
