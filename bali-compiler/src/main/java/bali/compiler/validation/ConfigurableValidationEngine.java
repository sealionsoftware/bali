package bali.compiler.validation;

import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.validation.visitor.Validator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ConfigurableValidationEngine implements ValidationEngine {

	private List<Validator<? extends Node>> validators;

	public ConfigurableValidationEngine(List<Validator<? extends Node>> validators) {
		this.validators = validators;
	}

	public Map<String, List<ValidationFailure>> validate(List<CompilationUnitNode> units) {

		Map<String, List<ValidationFailure>> failures = new HashMap<>();

		for (Validator<? extends Node> validator : validators) {

			Class accepts = getAcceptedNodeClass(validator);

			for (CompilationUnitNode unit : units) {
				try {

					List<ValidationFailure> validatorFailures = walk(unit, validator, accepts);
					if (validatorFailures.size() > 0) {
						List<ValidationFailure> unitFailures = failures.get(unit.getName());
						if (unitFailures == null) {
							unitFailures = new ArrayList<>();
							failures.put(unit.getName(), unitFailures);
						}
						unitFailures.addAll(validatorFailures);
					}
				} catch (Exception e) {
					System.err.println("Error running validator: " + validator + " [" + e.getClass() + "]");
					e.printStackTrace(System.err);
				}
			}

		}

		return failures;
	}

	private Class<? extends Node> getAcceptedNodeClass(Validator<? extends Node> validator) {

		Type[] types = validator.getClass().getGenericInterfaces();
		for (Type type : types) {
			if (type instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) type;
				if (pt.getRawType().equals(Validator.class)) {
					Type actualType = pt.getActualTypeArguments()[0];
					if (actualType instanceof Class) {
						return (Class<? extends Node>) actualType;
					}
				}
			}
		}

		throw new RuntimeException("Could not get accepted type for validator " + validator);

	}

	public <T extends Node> List<ValidationFailure> walk(Node node, Validator<T> validator, Class<T> acceptedClass) {
		List<ValidationFailure> failures = new ArrayList<>();
		for (Node child : node.getChildren()) {
			failures.addAll(walk(child, validator, acceptedClass));
		}
		if (acceptedClass.isAssignableFrom(node.getClass())) {
			failures.addAll(validator.validate((T) node));
		}
		return failures;
	}

}
