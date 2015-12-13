package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.HashMultiMap;
import com.sealionsoftware.MultiMap;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.sealionsoftware.Collections.flatten;
import static com.sealionsoftware.bali.compiler.assembly.Interruptable.wrapException;
import static java.util.Collections.synchronizedSet;

public class CompilationThreadManager {

	private final Set<Thread> monitoredThreads = synchronizedSet(new HashSet<>());
	private final MultiMap<MonitoredProperty<?>, BlockageDescription> blockages = new HashMultiMap<>(ArrayList::new);
	private final Object launchMonitor = new Object();
	private volatile boolean running = false;

	public void run(Collection<Runnable> runnables) {

        ExceptionGatherer gatherer = new ExceptionGatherer();
        running = true;

        for (Runnable runnable : runnables){
            Thread t = new Thread(runnable);
            monitoredThreads.add(t);
            t.setUncaughtExceptionHandler(gatherer);
        }

        for (Thread t : monitoredThreads){
            t.start();
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
                try {
                    thread.join();
                } catch (InterruptedException ignored) {}
            }
        }

        if (!gatherer.getGatheredExceptions().isEmpty()){
            throw new RuntimeException(gatherer.getGatheredExceptions().toString());
        }
    }

	public synchronized void registerBlockage(final MonitoredProperty property) {
		assert onMonitoredThread();
        blockages.putOne(property, new BlockageDescription(Thread.currentThread().getName(), property.getNode(), property.getProperty()));
		checkContinue();
	}

	public synchronized void deregisterBlockage(final MonitoredProperty property) {
		assert onMonitoredThread();
        blockages.remove(property);
	}

	public synchronized void deregisterThread() {
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
        return flatten(blockages.values());
	}



}
