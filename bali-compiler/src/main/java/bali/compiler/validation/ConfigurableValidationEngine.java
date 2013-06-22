package bali.compiler.validation;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.validation.visitor.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ConfigurableValidationEngine implements ValidationEngine {

	private List<Validator<CompilationUnit>> validators;

	public ConfigurableValidationEngine(List<Validator<CompilationUnit>> validators) {
		this.validators = validators;
	}

	public Map<String, List<ValidationFailure>> validate(List<CompilationUnit> units) {

		Map<String, List<ValidationFailure>> failures = new HashMap<>();

		for (Validator<CompilationUnit> validator : validators) {
			for (CompilationUnit unit : units){
				try {
					List<ValidationFailure> unitFailures = validator.validate(unit);
					if (unitFailures.size() > 0){
						failures.put(unit.getName(), unitFailures);
					}
				} catch (Exception e) {
					System.err.println("Error running validator: " + validator + " [" + e.getClass() + "]");
					e.printStackTrace(System.err);
				}
			}

		}

		return failures;
	}

}
