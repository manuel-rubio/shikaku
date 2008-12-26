package shikaku.tablero;

import java.util.Comparator;
import java.util.ArrayList;

public class Heap<E> extends ArrayList<E> {
	
	Comparator<E> comparator;
	
	public void setComparator( final Comparator<E> comparator ) {
		this.comparator = comparator;
	}

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
//		while (tmp.size() > 0) {
//			E dato = tmp..poll();
//			this.add(dato);
//		}
	}
}
