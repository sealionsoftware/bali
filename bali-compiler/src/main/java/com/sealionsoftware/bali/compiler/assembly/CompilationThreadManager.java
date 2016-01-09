package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.HashMultiMap;
import com.sealionsoftware.MultiMap;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.sealionsoftware.bali.compiler.assembly.Interruptable.wrapException;
import static java.util.Collections.synchronizedSet;

public class CompilationThreadManager {

	private final Set<Thread> monitoredThreads = synchronizedSet(new HashSet<>());
	private final MultiMap<MonitoredProperty<?>, BlockageDescription> blockages = new HashMultiMap<>(ArrayList::new);
	private final Object launchMonitor = new Object();
	private volatile boolean running = false;

	public void run(Collection<AssemblyTask> tasks) {

        ConcurrentHashMap<String, Throwable> exceptions = new ConcurrentHashMap<>();
        running = true;

        synchronized (this) {
            for (AssemblyTask task : tasks) {
                Thread t = new Thread(() -> {
                    try {
                        task.runnable.run();
                    } catch (Throwable e){
                        if (!(e.getCause() instanceof InterruptedException)){
                            exceptions.put(task.name, e);
                        }
                    } finally {
                        deregisterThread();
                    }
                }, task.name);
                monitoredThreads.add(t);
            }
            for (Thread t : monitoredThreads) {
                t.start();
            }
        }

        try {
            synchronized (launchMonitor) {
                while (running) wrapException(launchMonitor::wait, "Interrupted while waiting for registered threads to complete");
            }
        } finally {
            for (Thread thread : monitoredThreads) {
                thread.interrupt();
            }
            for (Thread thread : monitoredThreads) {
                wrapException(thread::join, "Interrupted while waiting for registered threads to complete");
            }
        }

        if (!exceptions.isEmpty()){
            throw new RuntimeException(exceptions.toString());
        }
    }

	public synchronized void registerBlockage(final MonitoredProperty property) {
		assert onMonitoredThread();
        blockages.put(property, new BlockageDescription(Thread.currentThread().getName(), property.getNode(), property.getProperty()));
		checkContinue();
	}

	public synchronized void deregisterBlockage(final MonitoredProperty property) {
		assert onMonitoredThread();
        blockages.remove(property);
	}

	private synchronized void deregisterThread() {
		assert onMonitoredThread();
		if (running){
			monitoredThreads.remove(Thread.currentThread());
			checkContinue();
		}
	}

	private void checkContinue() {
		if (blockages.size() == monitoredThreads.size()) {
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
        return blockages.values();
	}

}
