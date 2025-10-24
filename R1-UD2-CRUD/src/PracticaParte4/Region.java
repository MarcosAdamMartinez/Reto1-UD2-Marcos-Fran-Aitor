package PracticaParte4;

public class Region {

    private int NumGen;
    private String nombre;
    private String gimnasio;

    public Region() {}

    public Region(int NumGen, String nombre, String gimnasio) {
        this.NumGen = NumGen;
        this.nombre = nombre;
        this.gimnasio = gimnasio;
    }

    public String getGimnasio() {return gimnasio;}
    public void setGimnasio(String gimnasio) {this.gimnasio = gimnasio;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public int getNumGen() {return NumGen;}
    public void setNumGen(int numGen) {this.NumGen = NumGen;}

}
