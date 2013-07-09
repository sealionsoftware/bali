package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.BreakStatement;
import bali.compiler.parser.tree.ContinueStatement;
import bali.compiler.parser.tree.LoopStatement;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.Node;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 08/07/13
 */
public class BranchStatementValidator implements Validator<Method> {

	public List<ValidationFailure> validate(Method node) {
		return walk(node);
	}

	private List<ValidationFailure> walk(Node node) {
		List<ValidationFailure> failures = new ArrayList<>();
		if (node instanceof LoopStatement) {
			return failures;
		}
		if (node instanceof BreakStatement) {
			failures.add(new ValidationFailure(node, "Break statements cannot be used outside of loops"));
		}
		if (node instanceof ContinueStatement) {
			failures.add(new ValidationFailure(node, "Continue statements cannot be used outside of loops"));
		}
		for (Node child : node.getChildren()) {
			walk(child);
		}

		return failures;
	}
}
