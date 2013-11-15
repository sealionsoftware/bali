package bali.compiler.validation;

import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.validation.validator.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ConfigurableValidationEngine implements ValidationEngine {

	private List<Validator> validators;

	public ConfigurableValidationEngine(List<Validator> validators) {
		this.validators = validators;
	}

	public Map<String, List<ValidationFailure>> validate(List<CompilationUnitNode> units) {

		Map<String, List<ValidationFailure>> failures = new HashMap<>();

		for (Validator validator : validators) {

			for (CompilationUnitNode unit : units) {
				try {
					List<ValidationFailure> unitFailures = unit.accept(validator);
					failures.put(unit.getName(), unitFailures);
				} catch (Exception e) {
					System.err.println("Error running validator: " + validator + " [" + e.getClass() + "]");
					e.printStackTrace(System.err);
				}
			}

		}

		return failures;
	}

}
