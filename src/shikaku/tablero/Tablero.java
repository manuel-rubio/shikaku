package shikaku.tablero;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class Tablero {

	private int xsize;
	private int ysize;
	private boolean debug;
	
	private List<List<Combinacion>> soluciones;
	private List<Combinacion> fijos;
	
	public Tablero( boolean debug ) {
		xsize = ysize = 0;
		soluciones = null;
		this.debug = debug;
	}
	
	public Tablero( int xsize, int ysize, boolean debug ) {
		this.xsize = xsize;
		this.ysize = ysize;
		this.soluciones = null;
		this.debug = debug;
	}
	
	public void setSize( int xsize, int ysize ) {
		this.xsize = xsize;
		this.ysize = ysize;
	}
	
	public void buscaSoluciones( Heap<List<Combinacion>> llc ) {
		fijos = new ArrayList<Combinacion>();
		Combinacion.depuradora(fijos, llc);
		int [] res = new int[llc.size()];
		
		if (debug) {
			System.out.println("Combinaciones por pieza (después de la depuradora): ");
			Iterator<List<Combinacion>> ilc = llc.iterator();
			for (int i=0; ilc.hasNext(); i++) {
				System.out.println(i + " -> " + ilc.next().size());
			}
		}
		
		buscaSoluciones(llc, res, 0);
	}
	
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

	public void render() {
		for (int i=0; i<soluciones.size(); i++) {
			System.out.println("## Solución: " + (i + 1));
			render(i);
			System.out.println("##");
		}
		System.out.printf("Soluciones: %d\n", soluciones.size());
	}

	public void render( int solucion ) {
		if (soluciones.size() == 0) {
			System.out.println("Tablero sin soluciones.");
		} else if (solucion < 0 || solucion >= soluciones.size()) {
			System.out.println("Solución " + solucion + " no válida. Hay " + soluciones.size() + " solucion(es)");
		} else {
			List<Combinacion> slc = soluciones.get(solucion);
			if (debug) {
				System.out.printf("Size: %d x %d\n", xsize, ysize);
				System.out.print("OK: ");
				for (int i=0; i<slc.size(); i++) {
					System.out.print(slc.get(i) + " ");
				}
				System.out.print('\n');
			}
			for (int i=0; i<this.ysize; i++) {
				for (int j=0; j<this.xsize; j++) {
					Ordenada o = new Ordenada(j, i);
					boolean colision = false;
					for (int k=0; k<fijos.size(); k++) {
						if (fijos.get(k).colision(o)) {
							System.out.printf("%3d ", fijos.get(k).getNum());
							colision = true;
							break;
						}
					}
					for (int k=0; k<slc.size() && !colision; k++) {
						if (slc.get(k).colision(o)) {
							System.out.printf("%3d ", slc.get(k).getNum());
							colision = true;
							break;
						}
					}
					if (!colision) {
						System.out.print("  X ");
					}
				}
				System.out.print("\n");
			}
		}
	}
	
}
