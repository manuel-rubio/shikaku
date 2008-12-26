package shikaku.tablero;

import java.util.Comparator;
import java.util.ArrayList;

/**
 * El montículo hereda de ArrayList. Este tipo de dato se usa para almacenar datos
 * y ordenarlos según el comparador que se le pase.
 * 
 * @author bombadil
 *
 * @param <E> Tipo que se almacenará en el montículo.
 * 
 * @see java.util.ArrayList, java.util.Comparator
 */
public class Heap<E> extends ArrayList<E> {
	
	private static final long serialVersionUID = -2673380426460390622L;
	Comparator<E> comparator;

	/**
	 * configura un comparador para el montículo.
	 * @param comparator objeto de tipo comparador para el montículo.
	 */
	public void setComparator( final Comparator<E> comparator ) {
		this.comparator = comparator;
	}

	/**
	 * agrega un elemento al montículo.
	 * @param e elemento a agregar al montículo.
	 */
	public boolean add( E e ) {
		if (this.size() == 0) {
			return super.add(e);
		}
		for (int i=0; i<this.size(); i++) {
			if (comparator.compare(this.get(i), e) >= 0) {
				super.add(i, e);
				return true;
			}
		}
		return super.add(e);
	}
	
	/**
	 * Ordena el montículo. Se usa cuando se emplea el método
	 * addAll. Una vez insertados todos los elementos, se invoca
	 * a sort para ordenar el montículo adecuadamente.
	 */
	public void sort() {
		if (this.size() == 0) {
			return;
		}
		Heap<E> tmp = new Heap<E>();
		tmp.addAll(this);
		this.clear();
		for (int i=0; i<tmp.size(); i++) {
			E dato = tmp.get(i);
			this.add(dato);
		}
	}
}
