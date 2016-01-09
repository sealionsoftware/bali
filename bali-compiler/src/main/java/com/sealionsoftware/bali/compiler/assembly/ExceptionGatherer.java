package com.sealionsoftware.bali.compiler.assembly;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExceptionGatherer implements Thread.UncaughtExceptionHandler {

    private CompilationThreadManager threadManager;
	private final Map<String, Throwable> gatheredExceptions = new ConcurrentHashMap<>();

    public ExceptionGatherer(CompilationThreadManager threadManager) {
        this.threadManager = threadManager;
    }

    public void uncaughtException(Thread t, Throwable e) {
        if (!(e.getCause() instanceof InterruptedException)) {
            gatheredExceptions.put(t.getName(), e);
            threadManager.deregisterThread();
        }
	}

	public Map<String, Throwable> getGatheredExceptions(){
		return gatheredExceptions;
	}
}
