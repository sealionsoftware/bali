package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.UnaryOperation;
import bali.compiler.validation.ValidationFailure;
import bali.compiler.validation.type.Method;
import bali.compiler.validation.type.MethodDeclaringType;
import bali.compiler.validation.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates a Unary Operation Declaration
 * <p/>
 * Requires the target to have a type set
 * <p/>
 * Resolves the operator name to an inferred method
 * Sets the return type of the expression
 * <p/>
 * User: Richard
 * Date: 03/07/13
 */
public class UnaryOperationValidator implements Validator<UnaryOperation> {

	public List<ValidationFailure> validate(UnaryOperation node) {

		List<ValidationFailure> ret = new ArrayList<>();
		Type expressionType = node.getTarget().getType().getDeclaration();
		String operator = node.getOperator();

		Method methodDeclaration = getDeclarationForOperator(expressionType, operator);

		if (methodDeclaration == null) {
			ret.add(new ValidationFailure(node, "Type " + expressionType + " has no method for operator " + operator));
			return ret;
		}

		node.setMethod(methodDeclaration.getName());
		node.setType(methodDeclaration.getType());

		return ret;
	}

	private Method getDeclarationForOperator(MethodDeclaringType typeDeclaration, String operator) {
		for (Method methodDeclaration : typeDeclaration.getMethods()) {
			if (methodDeclaration.getArguments().size() == 0 && operator.equals(methodDeclaration.getOperator())) {
				return methodDeclaration;
			}
		}
		return null;
	}
}
