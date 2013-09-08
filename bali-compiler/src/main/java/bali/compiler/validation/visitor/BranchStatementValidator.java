package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.BreakStatementNode;
import bali.compiler.parser.tree.ContinueStatementNode;
import bali.compiler.parser.tree.LoopStatementNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 08/07/13
 */
public class BranchStatementValidator implements Validator<MethodDeclarationNode> {

	public List<ValidationFailure> validate(MethodDeclarationNode node) {
		return walk(node);
	}

	private List<ValidationFailure> walk(Node node) {
		List<ValidationFailure> failures = new ArrayList<>();
		if (node instanceof LoopStatementNode) {
			return failures;
		}
		if (node instanceof BreakStatementNode) {
			failures.add(new ValidationFailure(node, "Break statements cannot be used outside of loops"));
		}
		if (node instanceof ContinueStatementNode) {
			failures.add(new ValidationFailure(node, "Continue statements cannot be used outside of loops"));
		}
		for (Node child : node.getChildren()) {
			walk(child);
		}

		return failures;
	}
}
