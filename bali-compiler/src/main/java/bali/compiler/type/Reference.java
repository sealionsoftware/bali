package bali.compiler.type;

/**
 * User: Richard
 * Date: 17/09/13
 */
public class Reference<T> {

	private T referenced;

	public Reference() {
	}

	public Reference(T referenced) {
		this.referenced = referenced;
	}

	public T get() {
		return referenced;
	}

	public void set(T referenced) {
		this.referenced = referenced;
	}

}
