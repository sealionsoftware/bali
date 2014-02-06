package bali.compiler.validation.validator;

import bali.Exception;
import bali.compiler.parser.tree.Node;
import bali.compiler.type.Type;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.Collections;
import java.util.List;

/**
 * Checks that throw statements are attempting to throw Throwable expressions
 * <p/>
 * User: Richard
 * Date: 07/07/13
 */
public class ThrowStatementValidatorFactory implements ValidatorFactory {

	private Type throwableType;

	public ThrowStatementValidatorFactory(TypeLibrary library) {
		throwableType = library.getType(Exception.class.getName());
	}

	public Validator createValidator() {
		return new Validator() {
			//TODO
			public List<ValidationFailure> validate(Node node, Control control) {

	//		List<ValidationFailure> failures = new ArrayList<>();
	//		Expression value = node.getValue();
	//		if (node.getValue().getType().isAssignableTo(throwableType)) {
	//			failures.add(new ValidationFailure(node, "Thrown value " + value + " is not of type " + throwableType));
	//		}

				return Collections.emptyList();
			}
		};
	}



}