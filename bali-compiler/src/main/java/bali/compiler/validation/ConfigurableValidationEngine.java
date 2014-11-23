package bali.compiler.validation;

import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.validation.validator.Visitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ConfigurableValidationEngine implements ValidationEngine {

	private List<Visitor> validators;

	public ConfigurableValidationEngine(List<Visitor> visitors) {
		this.validators = visitors;
	}

	public Map<String, List<ValidationFailure>> validate(List<CompilationUnitNode> units, ClassLibrary classLibrary, ConstantLibrary constantLibrary) {

		Map<String, List<ValidationFailure>> failures = new HashMap<>();

		for (Visitor visitor : validators) {

			for (CompilationUnitNode unit : units) {
				try {
					List<ValidationFailure> unitFailures = unit.accept(visitor);
					failures.put(unit.getName(), unitFailures);
				} catch (Exception e) {
					System.err.println("Error running validator: " + visitor + " [" + e.getClass() + "]");
					e.printStackTrace(System.err);
				}
			}

		}

		return failures;
	}

}
