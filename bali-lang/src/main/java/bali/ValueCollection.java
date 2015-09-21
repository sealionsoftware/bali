package bali;

public interface ValueCollection<E extends Value<E>> extends Collection<E> {

	Boolean contains(E value);

}
