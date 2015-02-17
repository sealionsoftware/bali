package bali.compiler.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 29/05/13
 */
public class ValidationException extends RuntimeException {

	private Map<String, List<ValidationFailure>> failures;

	public ValidationException(Map<String, List<ValidationFailure>> failures) {
		this.failures = failures;
	}

	public List<String> getFailedFiles() {
		return new ArrayList<>(failures.keySet());
	}

	public List<ValidationFailure> getFailures(String packageName) {
		return new ArrayList<>(failures.get(packageName));
	}

	public String toString() {
		return failures.values().toString();
	}
}
