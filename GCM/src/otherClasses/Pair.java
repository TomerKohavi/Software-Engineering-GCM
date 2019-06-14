package otherClasses;

import java.io.Serializable;

/**
 * Class of Pair object (like tuple but of size 2)
 * @author Ron Cohen
 *
 * @param <T1> class of first variable in the pair
 * @param <T2> class of second variable in the pair
 */
public class Pair<T1, T2> implements Serializable {
	public T1 a;
	public T2 b;

	/**
	 * Constructor of pair
	 * @param a the first variable in the pair
	 * @param b the second variable in the pair
	 */
	public Pair(T1 a, T2 b) {
		this.a = a;
		this.b = b;
	}
}
