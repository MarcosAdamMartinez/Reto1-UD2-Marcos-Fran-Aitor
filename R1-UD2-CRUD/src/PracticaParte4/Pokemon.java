package PracticaParte4;


import java.sql.Date;

public class Pokemon {

    private int nivel;
    private String nombre;
    private String tipo1;
    private String tipo2;
    private Date fechaCaptura;
    private int gen;

    Pokemon() {}

    Pokemon(String nombre, int nivel, String tipo1, String tipo2, Date fechaCaptura, int gen) {
        this.nombre = nombre;
        this.nivel = nivel;
        this.tipo1 = tipo1;
        this.tipo2 = tipo2;
        this.fechaCaptura = fechaCaptura;
        this.gen = gen;
    }

    public int getNivel() {return nivel;}
    public void setNivel(int nivel) {this.nivel = nivel;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getTipo1() {return tipo1;}
    public void setTipo1(String tipo1) {this.tipo1 = tipo1;}
    public String getTipo2() {return tipo2;}
    public void setTipo2(String tipo2) {this.tipo2 = tipo2;}
    public Date getFechaCaptura() {return fechaCaptura;}
    public void setFechaCaptura(Date fechaCaptura) {this.fechaCaptura = fechaCaptura;}
    public int getGen() {return gen;}
    public void setGen(int gen) {this.gen = gen;}
}
