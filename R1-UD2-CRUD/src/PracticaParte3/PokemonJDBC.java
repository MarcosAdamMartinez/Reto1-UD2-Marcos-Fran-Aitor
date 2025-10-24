package PracticaParte3;

import PracticaParte1.gestionAlumnos.Model.Alumno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PokemonJDBC implements IPokemon{

    private static String cadenaConexion = "jdbc:mysql://localhost:3306/adat1";
    private static String user = "dam2";
    private static String pass = "asdf.1234";

    public  PokemonJDBC() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(cadenaConexion, user, pass);
    }

    @Override
    public List<String> obtenerTodo() {
        List<String> pokemon = new ArrayList<>();
        String sql = "SELECT * FROM pokedex";

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String pokemonStr = rs.getString("nombre") + " - "
                        + rs.getString("nivel") + "    "
                        + rs.getString("tipo1") + " / "
                        + rs.getString("tipo2");
                pokemon.add(pokemonStr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pokemon;
    }

    @Override
    public Pokemon getPokemonPorNombre(String nombre) {
        Pokemon pokemon = null;
        String sql = "SELECT * FROM pokedex WHERE nombre = ?";

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                pokemon = new Pokemon();
                pokemon.setNombre(rs.getString("nombre"));
                pokemon.setNivel(rs.getInt("nivel"));
                pokemon.setTipo1(rs.getString("tipo1"));
                pokemon.setTipo2(rs.getString("tipo2"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pokemon;
    }

    @Override
    public boolean modificarPokemon(Pokemon pokemon) {
        String sql = "UPDATE pokedex SET nombre = ?, nivel = ?, tipo1 = ?, tipo2 = ? WHERE nombre = ?";

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pokemon.getNombre());
            ps.setInt(2, pokemon.getNivel());
            ps.setString(3, pokemon.getTipo1());
            ps.setString(4, pokemon.getTipo2());
            ps.setString(5, pokemon.getNombre());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean eliminarPokemon(String nombre) {
        String sql = "DELETE FROM pokedex WHERE nombre = ?";

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean crearPokemon(Pokemon pokemon) {
        String sql = "INSERT INTO pokedex (nombre, nivel, tipo1, tipo2) VALUES (?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pokemon.getNombre());
            ps.setInt(2, pokemon.getNivel());
            ps.setString(3, pokemon.getTipo1());
            ps.setString(4, pokemon.getTipo2());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
