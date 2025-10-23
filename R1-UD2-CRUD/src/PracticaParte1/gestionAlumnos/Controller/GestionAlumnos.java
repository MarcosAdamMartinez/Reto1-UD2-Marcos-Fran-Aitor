package PracticaParte1.gestionAlumnos.Controller;

import PracticaParte1.gestionAlumnos.Model.IModeloAlumnos;
import PracticaParte1.gestionAlumnos.Model.ModeloAlumnosJDBC;
import PracticaParte1.gestionAlumnos.UI.VentanaAlumnos;

public class GestionAlumnos {

	public static void main(String[] args) {
		 try {
        	VentanaAlumnos view = new VentanaAlumnos();
        	IModeloAlumnos model = new ModeloAlumnosJDBC();
        	ControladorGestionAlumnos controller = new ControladorGestionAlumnos(model, view);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
