package com.sealionsoftware.bali.compiler.assembly;

import java.util.HashMap;
import java.util.Map;

public class ExceptionGatherer implements Thread.UncaughtExceptionHandler {

	private final Map<String, Throwable> gatheredExceptions = new HashMap<>();

	public synchronized void uncaughtException(Thread t, Throwable e) {
        if (!(e.getCause() instanceof InterruptedException)) {
            gatheredExceptions.put(t.getName(), e);
            e.printStackTrace();
        }
	}

	public synchronized Map<String, Throwable> getGatheredExceptions(){
		return gatheredExceptions;
	}
}
