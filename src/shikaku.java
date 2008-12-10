import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import shikaku.tablero.Combinacion;
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
	
	private static void run( InputStream is, boolean debug ) throws IOException {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader bf = new BufferedReader(isr);
		boolean ins = false;
		Heap<List<Combinacion>> lc = new Heap<List<Combinacion>>();
		Tablero t = new Tablero(debug);
		List<List<Integer>> tch = new LinkedList<List<Integer>>();
		List<Ordenada> puntos = new LinkedList<Ordenada>();
		int suma = 0;

		// criterio de ordenación para el montículo
		lc.setComparator(new Comparator<List<Combinacion>>() {
			public int compare( List<Combinacion> lc1, List<Combinacion> lc2 ) {
				return lc1.size() - lc2.size();
			}
		});

		// leemos los datos de entrada o de fichero
		do {
			List<Integer> lch = new LinkedList<Integer>();
			String linea = bf.readLine().trim();
			
			if (linea.length() > 0 && linea.charAt(0) != '#') { 
				for (int i=0; i<linea.length(); i++) {
					if (linea.charAt(i) == '*') {
						lch.add(new Integer(0));
						ins = true;
						if (debug) {
							System.out.print("  *");
						}
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
						if (debug) {
							System.out.printf("%3d", n);
						}
					}
				}
				if (ins) {
					tch.add(lch);
				}
				if (debug) {
					System.out.print('\n');
				}
			}
		} while (bf.ready());
		
		int ysize = tch.size();
		int xsize = tch.get(0).size();
		
		if (debug) {
			System.out.printf("Tamaño: %d x %d\n", xsize, ysize);
		}
		
		if (suma != (xsize * ysize)) {
			System.out.println("Tablero imposible de resolver.");
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

		if (debug) {
			System.out.println("Combinaciones por pieza: ");
			Iterator<List<Combinacion>> ilc = lc.iterator();
			for (int i=0; ilc.hasNext(); i++) {
				System.out.println(i + " -> " + ilc.next().size());
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
				
		boolean debug = false;
		
		// comprobamos si hay información en la entrada estándar
		if (System.in.available() > 0) {
			// hay información en la entrada estándar
			for (int i=0; i<args.length; i++) {
				if (args[i].equals("-d")) {
					debug = true;
				}
			}
			run(System.in, debug);
		} else {
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-h")) {
					// presenta ayuda y sale del programa
					System.out.println(shikaku.help());
					return;
				} else if (args[i].equals("-d")) {
					debug = true;
				} else {
					try {
						FileInputStream fin = new FileInputStream(args[i]);
						run(fin, debug);
					} catch (FileNotFoundException ex) {
						System.out.println("Fichero no válido o no encontrado.");
						System.out.println(shikaku.help());
					}
				}
			}
		}
	}

}
