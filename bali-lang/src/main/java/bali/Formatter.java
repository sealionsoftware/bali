package bali;

/**
 * User: Richard
 * Date: 04/07/13
 */
public interface Formatter<T extends Value> {

	public String format(T in);

}
