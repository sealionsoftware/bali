package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.SiteNode;
import bali.compiler.parser.tree.ThrowStatementNode;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks that throw statements are attempting to throw Throwable expressions
 * <p/>
 * User: Richard
 * Date: 07/07/13
 */
public class ThrowStatementValidator implements Validator<ThrowStatementNode> {

	private SiteNode throwableType;

	public ThrowStatementValidator(TypeLibrary library) {
//		Type throwableType = new Type();
//		throwableType.setSite(library.getTypeDeclaration(Throwable.class.getClassName()));
//		this.throwableType = throwableType;
	}

	//TODO
	public List<ValidationFailure> validate(ThrowStatementNode node) {

		List<ValidationFailure> failures = new ArrayList<>();
//		Expression value = node.getValue();
//		if (node.getValue().getType().isAssignableTo(throwableType)) {
//			failures.add(new ValidationFailure(node, "Thrown value " + value + " is not of type " + throwableType));
//		}

		return failures;
	}
}
