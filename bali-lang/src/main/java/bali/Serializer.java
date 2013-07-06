package bali;

/**
 * User: Richard
 * Date: 04/07/13
 */
public interface Serializer<T extends Value> extends Formatter<T> {

	public T parse(String serialization);

}
