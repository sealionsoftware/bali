package bali.compiler.reference;

/**
 * User: Richard
 * Date: 17/09/13
 */
public class SimpleReference<T> implements Reference<T> {

	private T referenced;

	public SimpleReference() {
	}

	public SimpleReference(T referenced) {
		this.referenced = referenced;
	}

	public T get() {
		return referenced;
	}

	public void set(T referenced) {
		this.referenced = referenced;
	}

}
