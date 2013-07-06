package bali;

/**
 * User: Richard
 * Date: 05/07/13
 */
public interface Quantified<T extends Value> extends Value<T> {

	public Compared compareTo(T value);

	public enum Compared {
		GREATER_THAN,
		EQUAL,
		LESS_THAN
	}

}
