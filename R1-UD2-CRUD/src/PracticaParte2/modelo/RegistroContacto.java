package PracticaParte2.modelo;
import java.util.Arrays;
import java.util.Map;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RegistroContacto {
	// usamos codificación ISO para que sea un byte por caracter
	// pero funcionaría con otras al limitar los tamaños de los arrays de bytes resultantes
	public static final Charset CODIFICACIÓN = StandardCharsets.ISO_8859_1;
	public static final Map<String,Integer> TAMAÑOS = Map.of(
		    "nombre", 20,
		    "teléfono", 10
	);
	public static final int TAMAÑO_REGISTRO  = TAMAÑOS.values().stream().reduce(0,Integer::sum);
	
	// Convierte un Contacto en un array de bytes del tamaño de registro utilizando para cada campo el tamaño marcado en TAMAÑOS
	// Rellena con espacios si ocupa menos
	public static byte[] contactoToBytes(Contacto contacto) {
		byte[] resultado, bytesTeléfono;
		String nombre =  contacto.getNombre();
		String teléfono = contacto.getTeléfono();
		resultado = Arrays.copyOf(nombre.getBytes(CODIFICACIÓN),TAMAÑO_REGISTRO);
		bytesTeléfono = Arrays.copyOf( teléfono.getBytes(CODIFICACIÓN), TAMAÑOS.get("teléfono"));
		System.arraycopy(bytesTeléfono,0,resultado,TAMAÑOS.get("nombre"),TAMAÑOS.get("teléfono"));
		return resultado;
	} // contactoToBytes
	
	// Crea un contacto a partir de un array de bytes con sus campos empaquetados por la función anterior
	public static Contacto contactoFromBytes(byte[] bytes) {
		if (bytes.length < TAMAÑO_REGISTRO) return null;
		// Quitamos los ceros finales con trim (bytes a cero del array si el String tenía menos longitud)
		// https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html
		String nombre = (new String(Arrays.copyOf(bytes,TAMAÑOS.get("nombre")),CODIFICACIÓN)).trim();
		String teléfono = (new String(Arrays.copyOfRange(bytes,TAMAÑOS.get("nombre"),TAMAÑO_REGISTRO),CODIFICACIÓN)).trim();
		return new Contacto(nombre,teléfono);
	} // contactoFromBytes
	
	// main como prueba de concepto de que funciona la conversión en los dos sentidos
	public static void main(String[] args) {
		Contacto[] contactos = {
			new Contacto("Pepe","12345678910"),
			new Contacto("María de las Mercedes","987654")
		};
		for (Contacto contacto : contactos) {
			Contacto regenerado = contactoFromBytes(contactoToBytes(contacto));
			System.out.println( regenerado + 
					", length nombre: " + regenerado.getNombre().length() +
					", length teléfono: " + regenerado.getTeléfono().length());
		}
		
	}
} // class
