package PracticaParte4;

import java.util.List;

public interface IPokemon {

        public List<String> obtenerTodo();

        public Pokemon getPokemonPorNombre(String nombre);

        public boolean modificarPokemon(Pokemon pokemon, Region region);

        public boolean eliminarPokemon(String nombre);

        public boolean crear(Pokemon pokemon);

}
