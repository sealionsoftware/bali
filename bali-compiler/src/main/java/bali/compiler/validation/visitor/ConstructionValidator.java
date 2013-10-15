package bali.compiler.validation.visitor;

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
public class ConstructionValidator implements Validator {

	private TypeLibrary library = new TypeLibrary();

	public ConstructionValidator(TypeLibrary library) {
		this.library = library;
	}

	public List<ValidationFailure> validate(Node node, Control control) {

		if (node instanceof ConstructionExpressionNode){
			ConstructionExpressionNode expression = (ConstructionExpressionNode) node;
			Type expressionType = library.getType(expression.getType().getName());
			// TODO: Check constructor type signature

			if (expressionType.isAbstract()) {
				return Collections.singletonList(new ValidationFailure(expression, "Cannot instancate an interface type"));
			}
		}

		return Collections.emptyList();
	}

	public void onCompletion() {
	}

}
