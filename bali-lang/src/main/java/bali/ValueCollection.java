package bali;

public interface ValueCollection<E extends Value<E>> extends Collection<E> {

	Logic contains(E value);

}
