package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.Expression;
import bali.compiler.parser.tree.ThrowStatement;
import bali.compiler.parser.tree.Type;
import bali.compiler.validation.TypeDeclarationLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks that throw statements are attempting to throw Throwable expressions
 * <p/>
 * User: Richard
 * Date: 07/07/13
 */
public class ThrowStatementValidator implements Validator<ThrowStatement> {

	private Type throwableType;

	public ThrowStatementValidator(TypeDeclarationLibrary library) {
		Type throwableType = new Type();
		throwableType.setDeclaration(library.getTypeDeclaration(Throwable.class));
		this.throwableType = throwableType;
	}

	public List<ValidationFailure> validate(ThrowStatement node) {

		List<ValidationFailure> failures = new ArrayList<>();
		Expression value = node.getValue();
		if (node.getValue().getType().isAssignableTo(throwableType)) {
			failures.add(new ValidationFailure(node, "Thrown value " + value + " is not of type " + throwableType));
		}

		return failures;
	}
}
