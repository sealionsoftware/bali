package bali.compiler.validation;

import bali.compiler.parser.tree.Node;
import bali.compiler.validation.visitor.Validator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class VisitorValidationEngine implements ValidationEngine {

	private List<TypeKey> validators;

	public VisitorValidationEngine(List<Validator<Node>> validators) {

		List<TypeKey> keyed = new ArrayList<>();
		for (Validator<Node> validator : validators){
			ParameterizedType pt = (ParameterizedType) validator.getClass().getGenericInterfaces()[0];
			Class<? extends Node> accepts = (Class<? extends Node>) pt.getActualTypeArguments()[0];
			keyed.add(new TypeKey(accepts, validator));
		}
		this.validators = keyed;
	}

	public List<ValidationFailure> validate(Node node) {

		List<ValidationFailure> failures = new ArrayList<>();

		for (TypeKey entry : validators){
			if (entry.getKey().isAssignableFrom(node.getClass())){
				failures.addAll(entry.getValidator().validate(node));
			}
		}

		for (Node child : node.getChildren()){
			failures.addAll(validate(child));
		}

		return failures;
	}

	private static class TypeKey {

		private Class key;
		private Validator validator;

		private TypeKey(Class key, Validator validator) {
			this.key = key;
			this.validator = validator;
		}

		private Class getKey() {
			return key;
		}

		private Validator getValidator() {
			return validator;
		}
	}
}
