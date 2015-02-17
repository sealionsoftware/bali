package bali.compiler;

import org.antlr.v4.runtime.RuleContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: Richard
 * Date: 09 Dec
 */
public class CompilationProgressMonitor {

	private final Set<Thread> monitoredThreads = new HashSet<>();
	private final Map<Thread, BlockageDescription> blockedThreads = new HashMap<>();
	private final Object launchMonitor = new Object();
	private volatile boolean running = false;

	public synchronized void registerThread() {
		monitoredThreads.add(Thread.currentThread());
		try {
			while (!running) wait();
		} catch (InterruptedException e) {
			throw new RuntimeException("Interrupted while waiting for run command", e);
		}
	}

	public void run() {
		synchronized (this) {
			running = true;
			notifyAll();
		}
		synchronized (launchMonitor) {
			try {
				while (running) launchMonitor.wait();
			} catch (InterruptedException e) {
				throw new RuntimeException("Interrupted while waiting for registered threads to complete", e);
			} finally {
				for (Thread thread : monitoredThreads) {
					thread.interrupt();
				}
				for (Thread thread : monitoredThreads) {
					try {
						thread.join();
					} catch (InterruptedException e) {}
				}
			}
		}

	}

	public synchronized void registerBlockage(RuleContext node, String name) {
		assert onMonitoredThread();
		Thread current = Thread.currentThread();
		blockedThreads.put(current, new BlockageDescription(current.getName(), node, name));
		checkContinue();
	}

	public synchronized void deregisterBlockage() {
		assert onMonitoredThread();
		blockedThreads.remove(Thread.currentThread());
	}

	public synchronized void deregisterThread() {
		assert onMonitoredThread();
		monitoredThreads.remove(Thread.currentThread());
		checkContinue();
	}

	private void checkContinue() {
		int numberMonitored = monitoredThreads.size();
		if (numberMonitored == 0 || blockedThreads.size() == numberMonitored) {
			synchronized (launchMonitor) {
				running = false;
				launchMonitor.notifyAll();
			}
		}
	}

	private boolean onMonitoredThread() {
		return monitoredThreads.contains(Thread.currentThread());
	}

	public synchronized Collection<BlockageDescription> getBlockages() {
		return blockedThreads.values();
	}

}
