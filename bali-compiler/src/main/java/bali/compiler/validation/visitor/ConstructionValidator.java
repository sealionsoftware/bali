package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.ConstructionExpressionNode;
import bali.compiler.type.Type;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ConstructionValidator implements Validator<ConstructionExpressionNode> {

	private TypeLibrary library = new TypeLibrary();

	public ConstructionValidator(TypeLibrary library) {
		this.library = library;
	}

	public List<ValidationFailure> validate(ConstructionExpressionNode expression) {

		// TODO: Check constructor type signature

		Type expressionType = library.getType(expression.getClassName());

		if (expression.getType().getType().isAbstract()) {
			Collections.singletonList(new ValidationFailure(expression, "Cannot instancate an interface type"));
		}


		return Collections.emptyList();
	}

}
