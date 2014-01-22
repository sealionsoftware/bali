package bali.compiler.parser.tree;

import bali.compiler.validation.ValidationFailure;

import java.util.List;

/**
 * User: Richard
 * Date: 18/11/13
 */
public class TerminatedException extends RuntimeException {

	private List<ValidationFailure> failures;

	public TerminatedException(List<ValidationFailure> failures) {
		this.failures = failures;
	}

	public List<ValidationFailure> getFailures() {
		return failures;
	}
}
