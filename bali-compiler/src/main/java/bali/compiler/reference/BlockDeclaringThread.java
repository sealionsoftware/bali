package bali.compiler.reference;

import java.util.concurrent.locks.ReentrantLock;

/**
 * User: Richard
 * Date: 04/11/13
 */
public class BlockDeclaringThread extends Thread {

	private ReentrantLock lock;
	private Boolean isBlocked = false;

	public BlockDeclaringThread(Runnable target, String name, ReentrantLock lock) {
		super(target, name);
		this.lock = lock;
	}

	public Boolean checkBlocked(){
		return isBlocked;
	}

	public void setBlocked(Boolean blocked){
		lock.lock();
		try {
			isBlocked = blocked;
		} finally {
			lock.unlock();
		}
	}

	public static void setCurrentBlocked(Boolean blocked){
		Thread current = Thread.currentThread();
		if (current instanceof BlockDeclaringThread){
			((BlockDeclaringThread) current).setBlocked(blocked);
		}
	}

}
