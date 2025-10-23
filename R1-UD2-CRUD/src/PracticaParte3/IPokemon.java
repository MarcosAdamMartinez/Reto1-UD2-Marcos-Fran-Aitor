package PracticaParte3;

import java.util.List;

public interface IPokemon {

        public List<String> obtenerTodo();

        public Pokemon getPokemonPorNombre(String nombre);

        public boolean modificarPokemon(Pokemon pokemon);

        public boolean eliminarPokemon(String nombre);

        public boolean crearPokemon(Pokemon pokemon);

}
