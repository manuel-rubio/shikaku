package shikaku.tablero;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase almacena una combinación de bloque de shikaku. Se almacenan
 * los bordes superior-izquierda e inferior-derecha.
 * 
 * @author Manuel Rubio
 *
 * @see Ordenada
 */
public class Combinacion {

	private int x1, x2, y1, y2;
	private int xsize, ysize;
	private int num;

	/**
	 * Constructor que inicializa las variables del objeto.
	 */
	public Combinacion() {
		this.x1 = this.x2 = this.y1 = this.y2 = this.num = 0;
	}

	/**
	 * Constructor que inicializa los datos básicos de la combinación.
	 * El tamaño del tablero y el número de cuadro que debe tener el bloque.
	 * @param xsize tamaño del tablero en el eje X.
	 * @param ysize tamaño del tablero en el eje Y.
	 * @param n número de cuadros que debe tener el bloque.
	 */
	public Combinacion(int xsize, int ysize, int n) {
		this.x1 = this.x2 = this.y1 = this.y2 = 0;
		this.xsize = xsize;
		this.ysize = ysize;
		this.num = n;
	}

	/**
	 * La posición que ocupa el bloque (la combinación) dentro del tablero.
	 * @param x1 posición izquierda
	 * @param y1 posición superior
	 * @param x2 posición derecha
	 * @param y2 posición inferior
	 */
	public void set(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	/**
	 * Comprueba si la configuración del bloque generado es válida
	 * dentro del tablero definido.
	 * @return verdadero si la combinación es válida respecto al tablero, falso en caso contrario.
	 */
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

	/**
	 * Comprueba si las combinaciones se solapan, o no.
	 * @param c Combinacion con la que se comprueba el solapamiento (colisión)
	 * @return verdadero si se solapan, falso en caso contrario.
	 */
	public boolean colision(Combinacion c) {
		return (x1 <= c.x1 && x2 >= c.x1 && y1 <= c.y1 && y2 >= c.y1) ||
			   (x1 <= c.x2 && x2 >= c.x2 && y1 <= c.y2 && y2 >= c.y2) ||
			   (x1 >= c.x1 && x1 <= c.x2 && c.y1 >= y1 && c.y1 <= y2) ||
			   (c.x1 <= x1 && c.x2 >= x1 && c.y1 <= y1 && c.y2 >= y1) ||
			   (c.x1 <= x2 && c.x2 >= x2 && c.y1 <= y2 && c.y2 >= y2) ||
			   (c.x1 >= x1 && c.x1 <= x2 && y1 >= c.y1 && y1 <= c.y2);
	}
	
	/**
	 * Comprueba si un punto está o no dentro del bloque.
	 * @param o Ordenada para comprobar si está dentro de la Combinacion.
	 * @return verdadero si la ordenada está dentro de la Combinacion, falso en caso contrario.
	 */
	public boolean colision(Ordenada o) {
		return (o.getX() >= this.x1 && o.getX() <= this.x2
				&& o.getY() >= this.y1 && o.getY() <= this.y2);
	}

	/**
	 * Toma el número de cuadros que debe tener el bloque (la combinación)
	 * @return el número de cuadros del bloque, de la combinación.
	 */
	public int getNum() {
		return num;
	}
	
	/**
	 * Algoritmo voraz por el que se eliminan combinaciones que son fáciles de descartar.
	 * @param fijos combinaciones que no pueden ponerse de otra forma.
	 * @param llc lista de combinaciones posibles para cada número (quitando fijos)
	 */
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
		
		// ponemos como bloques fijos los que tengan una sola combinacion,
		// así como descartaremos los que "colisionen" con ellos en el tablero.
		llc.sort();
		while (llc.size() > 0 && llc.get(0).size() == 1) {
			Combinacion fijo = llc.get(0).get(0);
			llc.remove(0);
			fijos.add(fijo);
		}
	}
	
	/**
	 * Genera todas las combinaciones posibles para un número encontrado en la posición específica.
	 * @param x punto en el eje X donde se encuentra el número.
	 * @param y punto en el eje Y donde se encuentra el número.
	 * @param n valor del número (número de cuadros del bloque).
	 * @param xsize tamaño en el eje X del tablero.
	 * @param ysize tamaño en el eje Y del tablero.
	 * @param puntos lista de los puntos del tablero.
	 * @return lista de combinaciones para el punto pasado como parámetro.
	 */
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
									result = new ArrayList<Combinacion>();
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

	/**
	 * Presenta en formato de cadena de texto la coordenada superior izquierda
	 * junto con la coordenada inferior derecha.
	 * 
	 * @return cadena de texto con el formato del bloque.
	 */
	public String toString() {
		return "(" + this.x1 + "," + this.y1 + ")-(" + this.x2 + ","
				+ this.y2 + ")";
	}
	
}
