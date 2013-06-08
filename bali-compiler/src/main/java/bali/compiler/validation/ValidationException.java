package bali.compiler.validation;

import java.util.List;

/**
 * User: Richard
 * Date: 29/05/13
 */
public class ValidationException extends Exception {

	private List<ValidationFailure> failures;

	public ValidationException(List<ValidationFailure> failures) {
		this.failures = failures;
	}

	public List<ValidationFailure> getFailures() {
		return failures;
	}
}
