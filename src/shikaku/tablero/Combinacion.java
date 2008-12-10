package shikaku.tablero;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Combinacion {

	private int x1, x2, y1, y2;
	private int xsize, ysize;
	private int num;

	public Combinacion() {
		this.x1 = this.x2 = this.y1 = this.y2 = this.num = 0;
	}

	public Combinacion(int xsize, int ysize, int n) {
		this.x1 = this.x2 = this.y1 = this.y2 = 0;
		this.xsize = xsize;
		this.ysize = ysize;
		this.num = n;
	}

	public void set(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	public boolean valido() {
		if (this.x1 < 0 || this.x1 >= this.xsize) {
			return false;
		}
		if (this.y1 < 0 || this.y1 >= this.ysize) {
			return false;
		}
		if (this.x2 < 0 || this.x2 >= this.xsize) {
			return false;
		}
		if (this.y2 < 0 || this.y2 >= this.ysize) {
			return false;
		}
		return true;
	}

	public boolean colision(Combinacion c) {
		if (x1 <= c.x1 && x2 >= c.x1 && y1 <= c.y1
				&& y2 >= c.y1) {
			return true;
		}
		if (x1 <= c.x2 && x2 >= c.x2 && y1 <= c.y2
				&& y2 >= c.y2) {
			return true;
		}
		if (x1 >= c.x1 && x1 <= c.x2 && c.y1 >= y1 && c.y1 <= y2) {
			return true;
		}
		if (c.x1 <= x1 && c.x2 >= x1 && c.y1 <= y1
				&& c.y2 >= y1) {
			return true;
		}
		if (c.x1 <= x2 && c.x2 >= x2 && c.y1 <= y2
				&& c.y2 >= y2) {
			return true;
		}
		if (c.x1 >= x1 && c.x1 <= x2 && y1 >= c.y1 && y1 <= c.y2) {
			return true;
		}
		return false;
	}
	
	public boolean colision(Ordenada o) {
		if (o.getX() >= this.x1 && o.getX() <= this.x2
				&& o.getY() >= this.y1 && o.getY() <= this.y2) {
			return true;
		}
		return false;
	}
	
	public int getNum() {
		return num;
	}
	
	public static void depuradora( List<Combinacion> fijos, Heap<List<Combinacion>> llc ) {
		// eliminamos las combinaciones que "colisionen" con todas las combinaciones
		// de un bloque específico.
		boolean colisiones;
		do {
			colisiones = false;
			for (int i=0; i<llc.size(); i++) {
				for (int j=0; j<llc.get(i).size(); j++) {
					for (int k=0; k<llc.size(); k++) {
						if (i != k) {
							int colision = 0;
							int l;
							for (l=0; l<llc.get(k).size(); l++) {
								if (llc.get(i).get(j).colision(llc.get(k).get(l))) {
									colision ++;
								}
							}
							if (colision == l) {
								llc.get(i).remove(j);
								if (j >= llc.get(i).size()) {
									j--;
								}
								colisiones = true;
							}
						}
					}
				}
			}
		} while (colisiones);
		llc.sort();
		
		// ponemos como bloques fijos los que tengan una sola combinacion,
		// así como descartaremos los que "colisionen" con ellos en el tablero.
		while (llc.size() > 0 && llc.get(0).size() == 1) {
			Combinacion fijo = llc.get(0).get(0);
			llc.remove(0);
			fijos.add(fijo);
			for (int i=0; i < llc.size(); i++) {
				for (int j=0; j < llc.get(i).size(); j++) {
					if (llc.get(i).get(j).colision(fijo)) {
						llc.get(i).remove(j);
						j--;
					}
				}
			}
			llc.sort();
		}
	}
	
	public static List<Combinacion> generador(int x, int y, int n, int xsize,
			int ysize, List<Ordenada> puntos ) {
		List<Combinacion> result = null;
		for (int f = n; f > 0; f--) {
			if (n % f == 0) {
				int xs = f;
				int ys = (n/f);
				for (int i=0; i<xs; i++) {
					for (int j=0; j<ys; j++) {
						Combinacion c1 = new Combinacion(xsize, ysize, n);
						c1.set(x-i, y-j, x+xs-1-i, y+ys-1-j);
						if (c1.valido()) {
							boolean colision = false;
							Iterator<Ordenada> io = puntos.iterator();
							for (int k=0; io.hasNext(); k++) {
								Ordenada o = io.next();
								if (c1.colision(o) && !o.equals(x, y)) {
									colision = true;
									break;
								}
							}
							if (!colision) {
								if (result == null) {
									result = new LinkedList<Combinacion>();
								}
								result.add(c1);
							}
						}
					}
				}
			}
		}
		return result;
	}

	public String toString() {
		return "(" + this.x1 + "," + this.y1 + ")-(" + this.x2 + ","
				+ this.y2 + ")";
	}
	
}
