package bali.compiler.reference;

/**
 * User: Richard
 * Date: 13/10/13
 */
public class Semaphore {

	private boolean set = false;

	public synchronized void check(){
		if (!set){
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}

	public synchronized void release(){
		set = true;
		notifyAll();
	}
}
