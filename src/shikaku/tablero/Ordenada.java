package shikaku.tablero;

public class Ordenada {

	private int x;
	private int y;
	
	public Ordenada() {
		x = y = 0;
	}
	
	public Ordenada( int x, int y ) {
		this.x = x;
		this.y = y;
	}
	
	public void set( int x, int y ) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	public boolean equals( int x, int y ) {
		if (this.x == x && this.y == y) {
			return true;
		}
		return false;
	}
}
