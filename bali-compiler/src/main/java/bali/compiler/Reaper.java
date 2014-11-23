package bali.compiler;

import bali.compiler.reference.BlockDeclaringThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * User: Richard
 * Date: 18/11/13
 */
public class Reaper implements Runnable {

	// TODO: internalise these resources
	private List<BlockDeclaringThread> monitoredThreads;
	private Lock lock;

	private volatile boolean killReaper = false;

	public Reaper(List<BlockDeclaringThread> monitoredThreads, Lock lock) {
		this.monitoredThreads = monitoredThreads;
		this.lock = lock;
	}

	public void run() {
		List<BlockDeclaringThread> liveThreads = new ArrayList<>(monitoredThreads);
		try {
			while(!killReaper){
				Thread.sleep(2000);
				lock.lock();
				try {
					boolean forceEnd = true;
					for (BlockDeclaringThread t : new ArrayList<>(liveThreads)){
						if (!t.isAlive()){
							liveThreads.remove(t);
							continue;
						}
						if (!t.checkBlocked()){
							forceEnd = false;
							break;
						}
					}
					if (forceEnd){
						killReaper = true;
						for (BlockDeclaringThread t : new ArrayList<>(liveThreads)){
							t.interrupt();
						}
					}
				} finally {
					lock.unlock();
				}
			}
		} catch (InterruptedException ie){
			// Whatever
		}
	}

	public void kill(){
		killReaper = true;
	}

}
