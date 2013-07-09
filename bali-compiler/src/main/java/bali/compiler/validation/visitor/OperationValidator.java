package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.Operation;
import bali.compiler.parser.tree.TypeDeclaration;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 03/07/13
 */
public class OperationValidator implements Validator<Operation> {

	public List<ValidationFailure> validate(Operation node) {

		List<ValidationFailure> ret = new ArrayList<>();
		TypeDeclaration<?> expressionType = node.getOne().getType().getDeclaration();
		String operator = node.getOperator();

		MethodDeclaration methodDeclaration = getDeclarationForOperator(expressionType, operator);

		if (methodDeclaration == null) {
			ret.add(new ValidationFailure(node, "Type " + expressionType + " has no method for operator " + operator));
			return ret;
		}

		node.setMethod(methodDeclaration.getName());
		node.setType(methodDeclaration.getType());

		return ret;
	}

	private MethodDeclaration getDeclarationForOperator(TypeDeclaration<?> typeDeclaration, String operator) {
		for (MethodDeclaration methodDeclaration : typeDeclaration.getMethods()) {
			if (methodDeclaration.getArguments().size() == 1 && operator.equals(methodDeclaration.getOperator())) {
				return methodDeclaration;
			}
		}
		return null;
	}


}
