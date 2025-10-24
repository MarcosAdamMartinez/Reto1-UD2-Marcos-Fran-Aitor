package PracticaParte4;

import java.util.List;

public interface IRegion {

    public List<String> obtenerTodo();

    public Region getRegionPorNombre(String nombre);

    public boolean eliminar(int gen);

    public boolean crear(Region region);

}
