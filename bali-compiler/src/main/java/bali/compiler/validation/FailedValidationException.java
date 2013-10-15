package bali.compiler.validation;

import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * User: Richard
 * Date: 11/10/13
 */
public class FailedValidationException extends RuntimeException {

	private List<CancellationException> cancellationExceptions;

	public FailedValidationException(List<CancellationException> cancellationExceptions) {
		this.cancellationExceptions = cancellationExceptions;
	}

	public List<CancellationException> getCancellationExceptions() {
		return cancellationExceptions;
	}

	public synchronized Throwable initCause(Throwable cause) {
		return cancellationExceptions.iterator().next();
	}
}
