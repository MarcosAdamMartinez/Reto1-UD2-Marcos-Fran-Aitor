package PracticaParte1.gestionAlumnos.Model;

import java.util.List;

public interface IModeloAlumnos {
	
	public List<String> getAll();
	
	public Alumno getAlumnoPorDNI(String DNI);

	public boolean modificarAlumno(Alumno alumno);
	
	public boolean eliminarAlumno(String  DNI);
	
	public boolean crearAlumno(Alumno alumno);
	

}
