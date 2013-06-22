package bali.compiler.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 29/05/13
 */
public class ValidationException extends Exception {

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
}
