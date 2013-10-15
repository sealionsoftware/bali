package bali.compiler.reference;

/**
 * User: Richard
 * Date: 15/10/13
 */
public interface Reference<T> {

	T get();

	void set(T referenced);
}
