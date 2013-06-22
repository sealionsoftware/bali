package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.ConstructionExpression;
import bali.compiler.parser.tree.Node;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ConstructionValidator implements Validator<CompilationUnit> {

	private Agent agent = new Agent();

	public List<ValidationFailure> validate(CompilationUnit unit) {
		return walk(unit);
	}

	public List<ValidationFailure> walk(Node node) {
		List<ValidationFailure> failures = new ArrayList<>();
		if (node instanceof ConstructionExpression) {
			failures.addAll(agent.validate((ConstructionExpression) node));
		}
		for (Node child : node.getChildren()) {
			failures.addAll(walk(child));
		}
		return failures;
	}

	public static class Agent implements Validator<ConstructionExpression> {

		public List<ValidationFailure> validate(ConstructionExpression expression) {

			if (expression.getType().getDeclaration().getAbstract()){
				Collections.singletonList(new ValidationFailure(expression, "Cannot instancate an interface type"));
			}

			return Collections.emptyList();
		}

	}

}
