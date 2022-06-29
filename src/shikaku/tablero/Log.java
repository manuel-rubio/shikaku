package shikaku.tablero;

/**
 * Sistema para salida de datos.
 * 
 * @author Manuel Rubio
 *
 */
public class Log {
	
	private static boolean debug = false;
	
	public static final boolean TYPE_NORMAL = false;
	public static final boolean TYPE_DEBUG = true;

	/**
	 * Imprime un mensaje a la salida.
	 * @param type tipo de mensaje para imprimir (DEBUG o NORMAL)
	 * @param s cadena de salida.
	 */
	public static void print( boolean type, String s ) {
		if (debug || (!debug && !type)) {
			System.out.print(s);
		}
	}

	/**
	 * Imprime el mensaje de salida siempre en modo NORMAL.
	 * @param s cadena de salida.
	 */
	public static void print( String s ) {
		print(TYPE_NORMAL, s);
	}
	
	/**
	 * Configura si se activa/desactiva la depuración.
	 * @param dbg verdadero si se activa o falso en caso contrario.
	 */
	public static void setDebug( boolean dbg ) {
		debug = dbg;
	}
	
	/**
	 * Activa la depuración.
	 */
	public static void setDebug() {
		debug = true;
	}

	/**
	 * ¿Está depurando?
	 * @return verdadero si está activa la depuración o falso en caso contrario.
	 */
	public static boolean isDebugging() {
		return debug;
	}
}
