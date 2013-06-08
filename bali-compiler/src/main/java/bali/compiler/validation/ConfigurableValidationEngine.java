package bali.compiler.validation;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.validation.visitor.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ConfigurableValidationEngine implements ValidationEngine {

	private List<Validator<CompilationUnit>> validators;

	public ConfigurableValidationEngine(List<Validator<CompilationUnit>> validators) {
		this.validators = validators;
	}

	public List<ValidationFailure> validate(CompilationUnit node) {

		List<ValidationFailure> failures = new ArrayList<>();

		for (Validator<CompilationUnit> validator : validators) {
			try {
				failures.addAll(validator.validate(node));
			} catch (Exception e) {
				System.err.println("Error running validator: " + validator + " [" + e.getClass() + "]");
				e.printStackTrace(System.err);
			}
		}

		return failures;
	}

}
