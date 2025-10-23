package PracticaParte2.modelo;

import java.util.Objects;

public class Contacto implements Comparable<Contacto> {
	protected String nombre;
	protected String teléfono;
	
	public String getNombre() {
		return nombre;
	}
	public String getTeléfono() {
		return teléfono;
	}
	public void setTeléfono(String teléfono) {
		this.teléfono = teléfono;
	}
	public Contacto(String nombre, String teléfono) {
		super();
		this.nombre = nombre;
		this.teléfono = teléfono;
	}
	@Override
	public int compareTo(Contacto arg0) {
		int res = this.nombre.toLowerCase().compareTo(arg0.nombre.toLowerCase());
		if (res == 0) {
			return this.teléfono.compareTo(arg0.teléfono);
		}
		return res;
	}
	@Override
	public int hashCode() {
		return Objects.hash(nombre.toLowerCase());
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contacto other = (Contacto) obj;
		return Objects.equals(nombre.toLowerCase(), other.nombre.toLowerCase());
	}
	@Override
	public String toString() {
		return nombre + ": " + teléfono;
	}
	
	
} // class Contacto
