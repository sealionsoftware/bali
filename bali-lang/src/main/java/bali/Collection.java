package bali;

/**
 * User: Richard
 * Date: 16/08/13
 */
public interface Collection<T> extends Iterable<T> {

	Integer size();

	Boolean isEmpty();

	T get(Integer index);


}
