package shikaku.tablero;

/**
 * Clase generada como tipo abstracto de dato (TAD) para
 * almacenar y procesar los datos de tipo ordenada (o coordenada).
 * 
 * @author Manuel Rubio
 */
public class Ordenada {

	private int x;
	private int y;
	
	/**
	 * Inicializa las variables internas.
	 */
	public Ordenada() {
		x = y = 0;
	}
	
	/**
	 * Inicializa las variables internas con un valor pasado como parámetro.
	 * @param x valor del eje X.
	 * @param y valor del eje Y.
	 */
	public Ordenada( int x, int y ) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Da los valores específicos a las ordenadas del objeto.
	 * @param x valor del eje X.
	 * @param y valor del eje Y.
	 */
	public void set( int x, int y ) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Retorna valor de la ordenada para el eje X.
	 * @return valor del eje X.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Retorna valor de la ordenada para el eje Y.
	 * @return valor del eje Y.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Retorna la ordenada formateada en una cadena de texto.
	 * @return cadena de texto.
	 */
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	/**
	 * Indica si los datos de la ordenada pasada en forma X e Y,
	 * es igual a la ordenada almacenada en el objeto.
	 * @param x valor del eje X.
	 * @param y valor del eje Y.
	 * @return verdadero si los parámetros pasados son iguales a los almacenados. Falso en caso contrario.
	 */
	public boolean equals( int x, int y ) {
		if (this.x == x && this.y == y) {
			return true;
		}
		return false;
	}
}
