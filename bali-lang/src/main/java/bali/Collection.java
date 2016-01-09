package bali;

public interface Collection<T> extends Iterable<T> {

	Integer size();

	Boolean isEmpty();

	T get(Integer index);

}
