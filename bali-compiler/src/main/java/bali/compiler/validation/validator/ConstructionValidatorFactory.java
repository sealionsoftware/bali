package bali.compiler.validation.validator;

import bali.compiler.parser.tree.ConstructionExpressionNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.validation.ValidationFailure;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ConstructionValidatorFactory implements ValidatorFactory {

	public Validator createValidator() {
		return new Validator() {
			public List<ValidationFailure> validate(Node node, Control control) {

				List<ValidationFailure> failures;
				if (node instanceof ConstructionExpressionNode){
					ConstructionExpressionNode expression = (ConstructionExpressionNode) node;
					bali.compiler.type.Class expressionClass = expression.getType().getTemplate();
					// TODO: Check constructor type signature

					if (!expressionClass.getMetaType().isConstructable()) {
						failures = Collections.singletonList(new ValidationFailure(expression, "Cannot instanciate an interface type"));
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
