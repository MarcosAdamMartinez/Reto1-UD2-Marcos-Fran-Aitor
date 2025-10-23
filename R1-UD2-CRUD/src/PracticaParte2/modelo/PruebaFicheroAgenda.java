package PracticaParte2.modelo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class PruebaFicheroAgenda {
	private static Scanner consola = new Scanner(System.in);
	final static int LISTAR = 1; // opción del menú para listar los contactos
	final static int AÑADIR = 2; // opción del menú para añadir
	final static int BORRAR = 3; // opción del menú para borrar
	final static String RUTA_FICHERO = "contactos.bin";

	public static void main(String[] args) {
		try (FicheroAgenda fichero = new FicheroAgenda(RUTA_FICHERO)) {
			// Mapa con los contactos por su nombre en orden alfabético
			// la clave siempre en minúsculas para no duplicar:
			Map<String, Contacto> contactos = new TreeMap<String, Contacto>();
			// generaFicheroEjemplo(fichero);  // en caso de querer regenerar el fichero para pruebas
			fichero.leeContactos().forEach(a -> contactos.put(a.getNombre().toLowerCase(), a));

			int opción;
			Contacto contacto, existente;
			while ((opción = menúAgenda()) != 0) {
				switch (opción) {
				case LISTAR:
					System.out.println("Agenda:");
					contactos.values().forEach(c -> System.out.println("\t" + c));
					break;
				case AÑADIR:
					contacto = menúAñadirModificar(contactos);
					if (contacto == null)
						break;
					if ((existente = contactos.get(contacto.getNombre().toLowerCase())) != null) {
						existente.setTeléfono(contacto.getTeléfono());
						if (fichero.escribeRegistro(existente)) { // para mantener el case del nombre
							System.out.println("Contacto modificado correctamente");
						} else
							System.out.println("Problema al modificar contacto");
					} else { // contacto nuevo
						contactos.put(contacto.getNombre().toLowerCase(), contacto);
						if (fichero.escribeRegistro(contacto)) {
							System.out.println("Contacto añadido correctamente");
						} else
							System.out.println("Problema al añadir contacto");
					}
					break;
				case BORRAR:
					contacto = menúBorrar(contactos);
					if (contacto != null) {
						if (fichero.borraRegistro(contacto)) {
							contactos.remove(contacto.getNombre());
							System.out.println("Registro borrado correctamente");
						} else
							System.out.println("Error al borrar registro");
					}
					break;
				default:
					System.out.println("Opción incorrecta");
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	} // main

	public static void generaFicheroEjemplo(FicheroAgenda fichero) {
		Contacto[] contactos = { new Contacto("Pepe", "12345678910"), new Contacto("María Luisa", "987654"),
				new Contacto("Marta", "333") };
		fichero.sobreescribeFichero(Arrays.asList(contactos));
	}

	public static int menúAgenda() {

		System.out.println("\nMenú:");
		System.out.println("\t1 Listar contactos");
		System.out.println("\t2 Añadir/Modificar contacto");
		System.out.println("\t3 Borrar contacto");
		System.out.println("\t0 Salir");
		System.out.print("Elija opción: ");
		int opción = 0;
		try {
			opción = consola.nextInt();
			consola.nextLine();
		} catch (Exception e) {
			System.out.println("Opción incorrecta");
			consola.nextLine();
		}
		return opción;
	} // menúAgenda

	public static Contacto menúBorrar(Map<String, Contacto> contactos) {
		Contacto contacto = elegirContacto(contactos);
		if (contacto == null)
			return null;
		System.out.print("¿Seguro que deseas borrar el contacto: " + contacto + "? (S/N) ");
		if (consola.nextLine().toLowerCase().startsWith("s"))
			return contacto;
		return null;
	} // menúBorrado

	public static Contacto menúAñadirModificar(Map<String, Contacto> contactos) {
		Contacto contacto = elegirContacto(contactos);
		if (contacto == null)
			return null;
		if (contacto.getTeléfono() != null) {
			System.out.println("Teléfono actual: " + contacto.getTeléfono());
		}
		System.out.print("Introduce nuevo teléfono: ");
		contacto.setTeléfono(consola.nextLine());
		return contacto;
	} // menúAñadirModificar

	public static Contacto elegirContacto(Map<String, Contacto> contactos) {
		System.out.print("Dime nombre del contacto: ");
		String nombre = consola.nextLine();
		Contacto contacto;
		if ((contacto = contactos.get(nombre.toLowerCase())) != null)
			return contacto;
		return new Contacto(nombre, null);
	} // elegirContacto

} // FicheroAgenda
