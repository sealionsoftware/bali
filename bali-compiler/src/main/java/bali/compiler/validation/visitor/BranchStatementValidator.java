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
public class BranchStatementValidator implements Validator {

	public List<ValidationFailure> validate(Node node, Control control) {

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

		return failures;
	}

	public void onCompletion() {
	}
}
