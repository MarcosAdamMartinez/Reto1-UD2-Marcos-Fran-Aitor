package PracticaParte4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PokemonJDBC implements IRegion, IPokemon {

    private static String cadenaConexion = "jdbc:mysql://localhost:3306/adat2";
    private static String user = "dam2";
    private static String pass = "asdf.1234";

    public PokemonJDBC() {
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
        List<String> datos = new ArrayList<>();
        String sql = "SELECT * FROM pokedex p JOIN regiones r ON p.gen = r.NumGen";

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String pokemonStr = rs.getString("p.nombre") + " - Nivel: "
                        + rs.getInt("p.nivel") + " | "
                        + rs.getString("p.tipo1") + " / " + rs.getString("p.tipo2")
                        + " | Fecha: " + rs.getDate("p.fechaCaptura")
                        + " | Región: " + rs.getString("r.nombreRegion")
                        + " | Gimnasio: " + rs.getString("r.gimnasio")
                        + " | GenJuego: " + rs.getInt("r.NumGen");
                datos.add(pokemonStr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datos;
    }

    @Override
    public Region getRegionPorNombre(String nombre) {
        Region region = null;
        String sql = "SELECT NumGen AS numGen, nombreRegion, gimnasio FROM regiones WHERE nombreRegion = ?";


        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                region = new Region();
//                region.setNumGen(rs.getInt("numGen"));
//                region.setNombre(rs.getString("nombreRegion"));
//                region.setGimnasio(rs.getString("gimnasio"));
//            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("DEBUG: Gimnasio: " + rs.getString("gimnasio"));
                int numGen = rs.getInt("NumGen");
                System.out.println("DEBUG: NumGen: " + numGen);
                System.out.println("DEBUG: wasNull? " + rs.wasNull());

                region = new Region();
                region.setNumGen(numGen);
                region.setNombre(rs.getString("nombreRegion"));
                region.setGimnasio(rs.getString("gimnasio"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return region;
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
                pokemon.setFechaCaptura(rs.getDate("fechaCaptura"));
                pokemon.setGen(rs.getInt("gen"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pokemon;
    }

    @Override
    public boolean modificarPokemon(Pokemon pokemon, Region region) {
        String sql = "UPDATE pokedex SET nivel = ?, tipo1 = ?, tipo2 = ?, gen = ? WHERE nombre = ?";

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pokemon.getNivel());
            ps.setString(2, pokemon.getTipo1());
            ps.setString(3, pokemon.getTipo2());
            ps.setInt(4, pokemon.getGen());
            ps.setString(5, pokemon.getNombre());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int gen) {
        String sql = "DELETE FROM pokedex WHERE gen = ?";
        String sql2 = "DELETE FROM regiones WHERE NumGen = ?";
        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             PreparedStatement ps2 = conn.prepareStatement(sql2)) {

            ps.setInt(1, gen);
            ps2.setInt(1, gen);

            ps.executeUpdate();
            ps2.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(String nombre) {
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
    public boolean crear(Region region) {
        String sql = "INSERT INTO regiones (NumGen, nombreRegion, gimnasio) VALUES (?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, region.getNumGen());
            ps.setString(2, region.getNombre());
            ps.setString(3, region.getGimnasio());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Si ya existe, ignoramos error
            if (!e.getMessage().contains("Duplicate")) {
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public boolean crear(Pokemon pokemon) {
        String sql = "INSERT INTO pokedex (nombre, nivel, tipo1, tipo2, fechaCaptura, gen) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pokemon.getNombre());
            ps.setInt(2, pokemon.getNivel());
            ps.setString(3, pokemon.getTipo1());
            ps.setString(4, pokemon.getTipo2());
            ps.setDate(5,pokemon.getFechaCaptura());
            ps.setInt(6, pokemon.getGen());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
