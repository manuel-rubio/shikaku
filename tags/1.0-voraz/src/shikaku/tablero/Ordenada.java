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
	private int n;
	
	/**
	 * Inicializa las variables internas.
	 */
	public Ordenada() {
		x = y = n = 0;
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
	 * Inicializa las variables internas con un valor pasado como parámetro.
	 * @param x valor del eje X.
	 * @param y valor del eje Y.
	 * @param n peso de la coordenada.
	 */
	public Ordenada( int x, int y, int n ) {
		this.x = x;
		this.y = y;
		this.n = n;
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
	 * Da los valores específicos a las ordenadas del objeto.
	 * @param x valor del eje X.
	 * @param y valor del eje Y.
	 * @param n peso de la coordenada.
	 */
	public void set( int x, int y, int n ) {
		this.x = x;
		this.y = y;
		this.n = n;
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
	 * Retorna valor del peso del punto (X,Y).
	 * @return valor del peso.
	 */
	public int getNum() {
		return n;
	}

	/**
	 * Retorna la ordenada formateada en una cadena de texto.
	 * @return cadena de texto.
	 */
	public String toString() {
		String retorno = "(" + x + "," + y + ")";
		if (this.n > 0) {
			return retorno + "[" + n + "]";
		}
		return retorno;
	}
	
	/**
	 * Indica si los datos de la ordenada pasada en forma X e Y,
	 * son iguales a la ordenada almacenada en el objeto.
	 * @param x valor del eje X.
	 * @param y valor del eje Y.
	 * @return verdadero si los parámetros pasados son iguales a los almacenados. Falso en caso contrario.
	 */
	public boolean equals( int x, int y ) {
		return (this.x == x && this.y == y);
	}
	
	/**
	 * Indica si los datos de la ordenada pasada,
	 * son iguales a la ordenada almacenada en el objeto.
	 * @param o Ordenada a comparar.
	 * @return verdadero si los parámetros pasados son iguales a los almacenados. Falso en caso contrario.
	 */
	public boolean equals( Ordenada o ) {
		return (this.x == o.x && this.y == o.y);
	}

}
