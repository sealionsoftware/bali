package bali.compiler.validation.validator;

import bali.compiler.parser.tree.ConstructionExpressionNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.type.Type;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ConstructionValidatorFactory implements ValidatorFactory {

	private TypeLibrary library = new TypeLibrary();

	public ConstructionValidatorFactory(TypeLibrary library) {
		this.library = library;
	}

	public Validator createValidator() {
		return new Validator() {
			public List<ValidationFailure> validate(Node node, Control control) {

				List<ValidationFailure> failures;
				if (node instanceof ConstructionExpressionNode){
					ConstructionExpressionNode expression = (ConstructionExpressionNode) node;
					Type expressionType = library.getType(expression.getType().getName());
					// TODO: Check constructor type signature

					if (expressionType.isAbstract()) {
						failures = Collections.singletonList(new ValidationFailure(expression, "Cannot instancate an interface type"));
					} else {
						failures = Collections.emptyList();
					}
				} else {
					failures = Collections.emptyList();
				}

				control.validateChildren();

				return failures;
			}
		};
	}

}
