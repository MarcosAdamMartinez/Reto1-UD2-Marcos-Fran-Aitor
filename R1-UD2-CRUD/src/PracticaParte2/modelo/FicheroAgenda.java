package PracticaParte2.modelo;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FicheroAgenda implements Closeable {
	private RandomAccessFile fichero;
	private Map<Contacto,Integer> posicionesContactos;
	private List<Integer> posicionesLibres;
	private int numPosiciones;
	
	public FicheroAgenda(String fileName) throws FileNotFoundException {
		fichero = new RandomAccessFile(fileName,"rws");
		// Mapa con los contactos y la posición que ocupan de registro en fichero:
		posicionesContactos = new HashMap<Contacto,Integer>();
		// Lista de posiciones libres por haberse borrado y quedar huecos
		posicionesLibres = new LinkedList<Integer>();
	} // constructor
	
	public List<Contacto> leeContactos() {
		List<Contacto> resultado = new ArrayList<Contacto>();
		int contador = 0;
		try {
			fichero.seek(0);
			while (true) {
				byte[] buffer = new byte[RegistroContacto.TAMAÑO_REGISTRO];
				if (fichero.read(buffer)<RegistroContacto.TAMAÑO_REGISTRO) break;
				Contacto c = RegistroContacto.contactoFromBytes(buffer);
				if (c.getNombre().equals("")) { // posición vacía
					posicionesLibres.add(contador);
				}
				else {
					resultado.add(c);
					posicionesContactos.put(c,contador);
				}
				contador++;
			} // while leer
		}
		catch (IOException e) {
			// ante cualquier excepción nos quedamos con lo que se haya podido leer
		}
		numPosiciones = contador;
		return resultado;
	} // leeContactos
	
	public boolean sobreescribeFichero(List<Contacto> contactos) {
		try {
			fichero.seek(0);
			for (Contacto contacto : contactos) {
				fichero.write(RegistroContacto.contactoToBytes(contacto));
			}
			fichero.setLength(fichero.getFilePointer());
			return true;
		} catch (IOException e) {
			return false;
		}
	} // sobreescribeFichero
	
	public boolean escribeRegistro(Contacto contacto) {
		Integer pos = posicionesContactos.get(contacto);
		if (pos!=null) {
			return escribeRegistro(contacto,pos);
		}
		else { // No está en disco, le buscamos posición
			if (posicionesLibres.size()>0) {
				pos = posicionesLibres.remove(0);
				posicionesContactos.put(contacto, pos);
				return escribeRegistro(contacto,pos);
			}
			else {
				// toca insertarlo al final
				posicionesContactos.put(contacto,numPosiciones);
				return escribeRegistro(contacto,numPosiciones++);
			}
		}
	} // escribeRegistro
	
	public boolean escribeRegistro(Contacto contacto, int posición) {
			try {
				fichero.seek(posición*RegistroContacto.TAMAÑO_REGISTRO);
				fichero.write(RegistroContacto.contactoToBytes(contacto));
			} catch (IOException e) {
				return false;
			}
			return true;
	} // escribeRegistro
	
	public Contacto leeRegistro(int posición) {
		try {
			fichero.seek(posición*RegistroContacto.TAMAÑO_REGISTRO);
			byte[] buffer = new byte[RegistroContacto.TAMAÑO_REGISTRO];
			if (fichero.read(buffer)==RegistroContacto.TAMAÑO_REGISTRO) {
				return RegistroContacto.contactoFromBytes(buffer);
			}
			return null;
		} catch (IOException e) {
			return null;
		}
	} // leeRegistro
	
	public boolean borraRegistro(Contacto contacto) {
		Integer posición = posicionesContactos.get(contacto);
		if (posición==null) {
			System.err.println("No encontrado registro a borrar");
			return false;
		}
		
		// Los siguientes pasos debería ser una transacción, o todos o ninguno:
		try {
			fichero.seek(posición*RegistroContacto.TAMAÑO_REGISTRO);
			//fichero.write(new byte[RegistroContacto.TAMAÑO_REGISTRO]);
			fichero.write(RegistroContacto.contactoToBytes(new Contacto("","")));
			posicionesContactos.remove(contacto);
			posicionesLibres.add(posición);
			
		} catch (IOException e) {
			System.err.println("Error escribiendo en disco al borrar");
			return false;
		}
		return true;
	} // borraRegistro
	
	
	@Override
	public void close() throws IOException {
		fichero.close();
	}
} // class FicheroAgenda
