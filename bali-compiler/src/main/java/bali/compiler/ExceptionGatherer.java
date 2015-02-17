package bali.compiler;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Richard
 * Date: 11 Dec
 */
public class ExceptionGatherer implements Thread.UncaughtExceptionHandler {

	private final Map<String, Throwable> gatheredExceptions = new HashMap<>();

	public void uncaughtException(Thread t, Throwable e) {
		if (!(e instanceof InterruptedException) && !(e.getCause() instanceof InterruptedException)){
			gatheredExceptions.put(t.getName(), e);
		}
	}

	public Map<String, Throwable> getGatheredExceptions(){
		return gatheredExceptions;
	}
}
