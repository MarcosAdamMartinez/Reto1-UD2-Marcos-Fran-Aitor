package PracticaParte2.modelo;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Nueva clase con misma funcion que FicheroAgenda, pero con persistencia en base de datos

public class BD_Agenda implements Closeable {

    private Connection connection;

    public BD_Agenda(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);

        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS contactos (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100) UNIQUE NOT NULL,
                telefono VARCHAR(50)
            );
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    public List<Contacto> leeContactos() {
        List<Contacto> lista = new ArrayList<>();
        String sql = "SELECT nombre, telefono FROM contactos ORDER BY nombre";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Contacto(rs.getString("nombre"), rs.getString("telefono")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean escribeRegistro(Contacto contacto) {
        String sqlSelect = "SELECT * FROM contactos WHERE LOWER(nombre) = LOWER(?)";
        try (PreparedStatement ps = connection.prepareStatement(
                sqlSelect, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {

            ps.setString(1, contacto.getNombre());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Actualiza el registro existente
                rs.updateString("telefono", contacto.getTeléfono());
                rs.updateRow();
            } else {
                // Inserta uno nuevo
                String sqlInsert = "INSERT INTO contactos (nombre, telefono) VALUES (?, ?)";
                try (PreparedStatement insert = connection.prepareStatement(sqlInsert)) {
                    insert.setString(1, contacto.getNombre());
                    insert.setString(2, contacto.getTeléfono());
                    insert.executeUpdate();
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean borraRegistro(Contacto contacto) {
        String sql = "DELETE FROM contactos WHERE LOWER(nombre) = LOWER(?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, contacto.getNombre());
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sobreescribeFichero(List<Contacto> contactos) {
        try {
            String truncateSQL = "TRUNCATE TABLE contactos";
            try (Statement st = connection.createStatement()) {
                st.execute(truncateSQL);
            }
            for (Contacto c : contactos) {
                escribeRegistro(c);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// Con esta nueva clase buscamos hacer que persista en base de datos, y al crearla en el mismo paquete que la antigua conseguimos
// que no sea necesario modificar el resto de código (PruebaFicheroAgenda). Con esto conseguimos que el identificador que use BD_Agenda
// sea nombre (y adicionalmente ID) en vez de la posición/registro que usa FicheroAgenda; en cuanto a las búsquedas y actualizaciones, al
// hacerlo con SELECT, INSERT y UPDATE conseguimos que sea de manera más directa en vez de tener que mapear; en cuanto a la concurrencia,
// la base de datos gestiona mejor que un fichero compartido; y por último, las bases de datos relacionales ofrecen consultas complejas,
// lo que nos garantiza la persistencia.