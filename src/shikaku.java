import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import shikaku.tablero.Combinacion;
import shikaku.tablero.Log;
import shikaku.tablero.Tablero;
import shikaku.tablero.Heap;
import shikaku.tablero.Ordenada;

/**
 * Shikaku 
 * Manuel Angel Rubio Jiménez
 * 
 * Este programa ha sido desarrollado como Práctica para la asignatura de
 * Programación III de la Universidad Nacional de Eduación a Distancia (UNED).
 */

public class shikaku {
	
	private List<Ordenada> puntos;
	private int xsize, ysize; 
	
	public shikaku() {
		puntos = new ArrayList<Ordenada>();
		xsize = ysize = 0;
	}

	private static String help() {
		return
				"Shikaku\n" +
				"Manuel Rubio\n" +
				"\n" +
				"Sintaxis: java shikaku [-h] [-d] [fichero]\n" +
				"\n" +
				"Opciones:\n" +
				"\t-h      muestra esta ayuda.\n" +
				"\t-d      opción de depuración (más mensajes sobre la salida.\n" +
				"\tfichero analiza un fichero de tablero y sus soluciones.\n";
	}
	
	/**
	 * Lee los datos de la entrada pasada como parámetro, almacenando los puntos
	 * encontrados en el tablero, 
	 * @param is
	 * @throws IOException
	 */
	private void leeEntrada( InputStream is ) throws IOException {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader bf = new BufferedReader(isr);
		List<List<Integer>> tch = new ArrayList<List<Integer>>();
		boolean ins = false;
		int suma = 0;
		
		do {
			List<Integer> lch = new ArrayList<Integer>();
			String linea = bf.readLine().trim();
			
			if (linea.length() > 0 && linea.charAt(0) != '#') { 
				for (int i=0; i<linea.length(); i++) {
					if (linea.charAt(i) == '*') {
						lch.add(new Integer(0));
						ins = true;
						Log.print(Log.TYPE_DEBUG, "   *");
					} else if (linea.charAt(i) >= '0' && linea.charAt(i) <= '9') {
						Integer n = new Integer(linea.charAt(i) - '0');
						while (linea.length() > i+1 && linea.charAt(i+1) >= '0' && linea.charAt(i+1) <= '9') {
							i++;
							n *= 10;
							n += linea.charAt(i) - '0';
						}
						suma += n.intValue();
						puntos.add(new Ordenada(lch.size(), tch.size(), n.intValue()));
						lch.add(n);
						ins = true;
						Log.print(Log.TYPE_DEBUG, String.format("%4d", n));
					}
				}
				if (ins) {
					tch.add(lch);
				}
				Log.print(Log.TYPE_DEBUG, "\n");
			}
		} while (bf.ready());
		
		ysize = tch.size();
		xsize = tch.get(0).size();
		
		Log.print(Log.TYPE_DEBUG, String.format("Tamaño: %d x %d\n", xsize, ysize));
		
		if (suma != (xsize * ysize)) {
			Log.print("Tablero imposible de resolver.\n");
			System.exit(0);
		}
	}
	
	/**
	 * Se encarga de la ejecución del algoritmo para la búsqueda de combinaciones,
	 * la resolución y presentación del tablero.
	 */
	private void run() {
		Heap<List<Combinacion>> lc = new Heap<List<Combinacion>>();
		Tablero t = new Tablero();

		// criterio de ordenación para el montículo
		lc.setComparator(new Comparator<List<Combinacion>>() {
			public int compare( List<Combinacion> lc1, List<Combinacion> lc2 ) {
				return lc1.size() - lc2.size();
			}
		});

		// agregamos las combinaciones generadas a la lista de combinaciones
		Iterator<Ordenada> ilo = puntos.iterator();
		while (ilo.hasNext()) {
			Ordenada o = ilo.next();
			lc.add(Combinacion.generador(o.getX(), o.getY(), o.getNum(), xsize, ysize, puntos));
		}

		// imprimimos las combinaciones por pieza (en caso de depuración)
		if (Log.isDebugging()) {
			Log.print(Log.TYPE_DEBUG, "Combinaciones por pieza: \n");
			Iterator<List<Combinacion>> ilc = lc.iterator();
			for (int i=0; ilc.hasNext(); i++) {
				Log.print(Log.TYPE_DEBUG, i + " -> " + ilc.next().size() + "\n");
			}
		}
		
		// resolvemos y presentamos las soluciones del tablero
		t.setSize(xsize, ysize);
		t.buscaSoluciones(lc);
		t.render();
	}
	
	/**
	 * Método de clase que se ejecutará desde la shell.
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws IOException {

		boolean ayuda = false;
		InputStream is = null;

		// comprobamos si hay información en la entrada estándar
		if (System.in.available() > 0) {
			// hay información en la entrada estándar
			is = System.in;
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-h")) {
				ayuda = true;
			} else if (args[i].equals("-d")) {
				Log.setDebug();
			} else {
				if (is == null) {
					try {
						is = new FileInputStream(args[i]);
					} catch (FileNotFoundException ex) {
						Log.print("Fichero no válido o no encontrado.\n\n" + shikaku.help() + "\n");
					}
				}
			}
		}
		if (ayuda) {
			Log.print(shikaku.help() + "\n");
		}
		if (is != null) {
			shikaku s = new shikaku();
			s.leeEntrada(is);
			s.run();
		}
	}

}
