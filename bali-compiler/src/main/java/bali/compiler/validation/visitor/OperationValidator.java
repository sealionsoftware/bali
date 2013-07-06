package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.Operation;
import bali.compiler.parser.tree.Type;
import bali.compiler.parser.tree.TypeDeclaration;
import bali.compiler.parser.tree.UnaryOperation;
import bali.compiler.validation.TypeDeclarationLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 03/07/13
 */
public class OperationValidator implements Validator<CompilationUnit> {

	public List<ValidationFailure> validate(CompilationUnit node) {
		return walk(node);
	}

	private List<ValidationFailure> walk(Node node) {
		List<ValidationFailure> ret = new ArrayList<>();
		for (Node child : node.getChildren()) {
			if (child instanceof Operation) {
				ret.addAll(validate((Operation) child));
			}
			ret.addAll(walk(child));
		}
		return ret;
	}


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
