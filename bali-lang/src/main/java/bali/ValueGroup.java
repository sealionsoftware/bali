package bali;

public interface ValueGroup<E extends Value<E>> extends Group<E> {

	Logic contains(E value);

}
