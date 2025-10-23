package PracticaParte3;

public class Pokemon {

    private int nivel;
    private String nombre;
    private String tipo1;
    private String tipo2;

    Pokemon() {}

    Pokemon(String nombre, int nivel, String tipo1, String tipo2) {
        this.nombre = nombre;
        this.nivel = nivel;
        this.tipo1 = tipo1;
        this.tipo2 = tipo2;
    }

    public int getNivel() {return nivel;}
    public void setNivel(int nivel) {this.nivel = nivel;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getTipo1() {return tipo1;}
    public void setTipo1(String tipo1) {this.tipo1 = tipo1;}
    public String getTipo2() {return tipo2;}
    public void setTipo2(String tipo2) {this.tipo2 = tipo2;}

}
