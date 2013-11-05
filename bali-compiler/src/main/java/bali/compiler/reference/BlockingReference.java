package bali.compiler.reference;

/**
 * User: Richard
 * Date: 06/10/13
 */
public class BlockingReference<T> implements Reference<T> {

	private boolean isSet = false;
	private T referenced;

	public BlockingReference() {
	}

	public BlockingReference(T referenced) {
		isSet = true;
		this.referenced = referenced;
	}

	public synchronized T get() {
		while (!isSet){
			try {
				BlockDeclaringThread.setCurrentBlocked(true);
				wait();
			} catch (InterruptedException e) {
 				throw new RuntimeException(e);
			}
		}
		return referenced;
	}

	public synchronized void set(T referenced) {
		this.referenced = referenced;
		isSet = true;
		BlockDeclaringThread.setCurrentBlocked(false);
		notifyAll();
	}
}
