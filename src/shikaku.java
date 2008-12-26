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
	
	private static void run( InputStream is ) throws IOException {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader bf = new BufferedReader(isr);
		boolean ins = false;
		Heap<List<Combinacion>> lc = new Heap<List<Combinacion>>();
		Tablero t = new Tablero();
		List<List<Integer>> tch = new ArrayList<List<Integer>>();
		List<Ordenada> puntos = new ArrayList<Ordenada>();
		int suma = 0;

		// criterio de ordenación para el montículo
		lc.setComparator(new Comparator<List<Combinacion>>() {
			public int compare( List<Combinacion> lc1, List<Combinacion> lc2 ) {
				return lc1.size() - lc2.size();
			}
		});

		// leemos los datos de entrada o de fichero
		do {
			List<Integer> lch = new ArrayList<Integer>();
			String linea = bf.readLine().trim();
			
			if (linea.length() > 0 && linea.charAt(0) != '#') { 
				for (int i=0; i<linea.length(); i++) {
					if (linea.charAt(i) == '*') {
						lch.add(new Integer(0));
						ins = true;
						Log.print(Log.TYPE_DEBUG, "  *");
					} else if (linea.charAt(i) >= '0' && linea.charAt(i) <= '9') {
						Integer n = new Integer(linea.charAt(i) - '0');
						while (linea.length() > i+1 && linea.charAt(i+1) >= '0' && linea.charAt(i+1) <= '9') {
							i++;
							n *= 10;
							n += linea.charAt(i) - '0';
						}
						suma += n.intValue();
						puntos.add(new Ordenada(lch.size(), tch.size()));
						lch.add(n);
						ins = true;
						Log.print(Log.TYPE_DEBUG, String.format("%3d", n));
					}
				}
				if (ins) {
					tch.add(lch);
				}
				Log.print(Log.TYPE_DEBUG, "\n");
			}
		} while (bf.ready());
		
		int ysize = tch.size();
		int xsize = tch.get(0).size();

		Log.print(Log.TYPE_DEBUG, String.format("Tamaño: %d x %d\n", xsize, ysize));
		
		if (suma != (xsize * ysize)) {
			Log.print("Tablero imposible de resolver.\n");
			return;
		}
		
		// agregamos las combinaciones generadas a la lista de combinaciones
		Iterator<List<Integer>> ili = tch.iterator();
		for (int i=0; ili.hasNext(); i++) {
			Iterator<Integer> ii = ili.next().iterator();
			for (int j=0; ii.hasNext(); j++) {
				int n = ii.next();
				if (n > 0) {
					lc.add(Combinacion.generador(j, i, n, xsize, ysize, puntos));
				}
			}
		}

		if (Log.isDebugging()) {
			Log.print(Log.TYPE_DEBUG, "Combinaciones por pieza: \n");
			Iterator<List<Combinacion>> ilc = lc.iterator();
			for (int i=0; ilc.hasNext(); i++) {
				Log.print(Log.TYPE_DEBUG, i + " -> " + ilc.next().size() + "\n");
			}
		}
		
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
				// presenta ayuda y sale del programa
				ayuda = true;
			} else if (args[i].equals("-d")) {
				Log.setDebug();
			} else {
				if (is == null) {
					try {
						is = new FileInputStream(args[i]);
					} catch (FileNotFoundException ex) {
						Log.print("Fichero no válido o no encontrado.\n" + shikaku.help() + "\n");
					}
				}
			}
		}
		if (ayuda) {
			Log.print(shikaku.help() + "\n");
		}
		if (is != null) {
			run(is);
		}
	}

}
