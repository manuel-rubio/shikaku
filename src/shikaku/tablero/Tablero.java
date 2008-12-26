package shikaku.tablero;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Almacena el tamaño del tablero, las combinaciones fijas y la lista de soluciones.
 *  
 * @author Manuel Rubio
 *
 * @see Combinacion
 */
public class Tablero {

	private int xsize;
	private int ysize;
	
	private List<List<Combinacion>> soluciones;
	private List<Combinacion> fijos;

	/**
	 * Constructor que inicializa las variables del objeto.
	 */
	public Tablero() {
		xsize = ysize = 0;
		soluciones = null;
	}
	
	/**
	 * Constructor que inicializa las variables del objeto, pasando
	 * el tamaño del tablero.
	 * @param xsize tamaño del tablero en el eje X.
	 * @param ysize tamaño del tablero en el eje Y.
	 */
	public Tablero( int xsize, int ysize ) {
		this.xsize = xsize;
		this.ysize = ysize;
		this.soluciones = null;
	}
	
	/**
	 * Configura el tamaño del tablero.
	 * @param xsize tamaño del tablero en el eje X.
	 * @param ysize tamaño del tablero en el eje Y.
	 */
	public void setSize( int xsize, int ysize ) {
		this.xsize = xsize;
		this.ysize = ysize;
	}

	/**
	 * Lanza el proceso de búsqueda de soluciones, mediante un
	 * algoritmo heurístico, de vuelta atrás.
	 * @param llc lista de combinaciones.
	 */
	public void buscaSoluciones( Heap<List<Combinacion>> llc ) {
		fijos = new ArrayList<Combinacion>();
		Combinacion.depuradora(fijos, llc);
		int [] res = new int[llc.size()];
		
		if (Log.isDebugging()) {
			Log.print(Log.TYPE_DEBUG, "Combinaciones por pieza (después de la depuradora): \n");
			Iterator<List<Combinacion>> ilc = llc.iterator();
			for (int i=0; ilc.hasNext(); i++) {
				Log.print(Log.TYPE_DEBUG, i + " -> " + ilc.next().size() + "\n");
			}
		}
		
		buscaSoluciones(llc, res, 0);
	}
	
	/**
	 * método recursivo para la búsqueda de soluciones mediante el método de
	 * vuelta atrás.
	 * @param llc montículo con lista de combinaciones por cada número.
	 * @param res array de soluciones.
	 * @param idx índice que indica el número que se está comprobando.
	 */
	private void buscaSoluciones( List<List<Combinacion>> llc, int[] res, int idx ) {
		if (idx < res.length) {
			// tomamos el iterador del bloque de combinaciones que toque
			Iterator<Combinacion> ic = llc.get(idx).iterator();
			for (int i=0; ic.hasNext(); i++) {
				// tomamos la combinación que toque combinar
				Combinacion c = ic.next();
				boolean colisiones = false;
				Iterator<Combinacion> ifijo = fijos.iterator();
				while (ifijo.hasNext()) {
					Combinacion fijo = ifijo.next();
					if (c.colision(fijo)) {
						colisiones = true;
						break;
					}
				}
				for (int j=0; j<idx && !colisiones; j++) {
					// tomamos una de las combinaciones almacenadas con anterioridad
					Combinacion tc = llc.get(j).get(res[j]);
					if (c.colision(tc)) {
						// si se colisionó, se poda
						colisiones = true;
						break;
					}
				}
				if (!colisiones) {
					// si no ha colisionado con ninguno de los anteriores, se sigue en profundidad
					res[idx] = i;
					buscaSoluciones(llc, res, idx+1);
				}
			}
		} else {
			List<Combinacion> lc = new ArrayList<Combinacion>();
			for (int i=0; i<idx; i++) {
				lc.add(llc.get(i).get(res[i]));
			}
			if (soluciones == null) {
				soluciones = new ArrayList<List<Combinacion>>();
			}
			soluciones.add(lc);
		}
	}

	/**
	 * imprime por pantallas todas las soluciones almacenadas.
	 */
	public void render() {
		for (int i=0; i<soluciones.size(); i++) {
			Log.print("## Solución: " + (i + 1) + "\n");
			render(i);
			Log.print("##\n");
		}
		Log.print("Soluciones: " + soluciones.size() + "\n");
	}

	/**
	 * imprime por pantalla la solución pasada como parámetro.
	 * @param solucion índice de la solución a presentar.
	 */
	public void render( int solucion ) {
		if (soluciones.size() == 0) {
			Log.print("Tablero sin soluciones.\n");
		} else if (solucion < 0 || solucion >= soluciones.size()) {
			Log.print("Solución " + solucion + " no válida. Hay " + soluciones.size() + " solucion(es)\n");
		} else {
			List<Combinacion> slc = soluciones.get(solucion);
			if (Log.isDebugging()) {
				Log.print(Log.TYPE_DEBUG, "Size: " + xsize + " x " + ysize + "\nOK: ");
				for (int i=0; i<slc.size(); i++) {
					Log.print(Log.TYPE_DEBUG, slc.get(i) + " ");
				}
				Log.print(Log.TYPE_DEBUG, "\n");
			}
			for (int i=0; i<this.ysize; i++) {
				for (int j=0; j<this.xsize; j++) {
					Ordenada o = new Ordenada(j, i);
					boolean colision = false;
					for (int k=0; k<fijos.size(); k++) {
						if (fijos.get(k).colision(o)) {
							Log.print(String.format("%3d ", fijos.get(k).getNum()));
							colision = true;
							break;
						}
					}
					for (int k=0; k<slc.size() && !colision; k++) {
						if (slc.get(k).colision(o)) {
							Log.print(String.format("%3d ", slc.get(k).getNum()));
							colision = true;
							break;
						}
					}
					if (!colision) {
						Log.print("  X ");
					}
				}
				Log.print("\n");
			}
		}
	}
	
}
