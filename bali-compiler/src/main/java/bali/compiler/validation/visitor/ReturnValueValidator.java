package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ReturnStatementNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.parser.tree.StatementNode;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ReturnValueValidator implements Validator<MethodDeclarationNode> {

	// Engages methods, ensures that their return values are correct
	public List<ValidationFailure> validate(MethodDeclarationNode method) {

		ReturnValueValidatorAgent agent = new ReturnValueValidatorAgent(method.getType());
		agent.validate(method.getBody());
		List<ValidationFailure> failures = agent.getFailures();

		List<StatementNode> statements = method.getBody().getStatements();
		StatementNode lastStatement = statements.size() > 0 ? statements.get(statements.size() - 1) : null;

		if (method.getType() != null) {
			if (!(lastStatement instanceof ReturnStatementNode)) {
				failures.add(new ValidationFailure(lastStatement, "Method does not return a " + method.getType()));
			}
		} else if (!(lastStatement instanceof ReturnStatementNode)) {
			statements.add(new ReturnStatementNode());
		}

		return failures;
	}

	public static class ReturnValueValidatorAgent {

		private SiteNode type;
		private List<ValidationFailure> failures = new ArrayList<>();

		public ReturnValueValidatorAgent(SiteNode type) {
			this.type = type;
		}

		public void validate(Node node) {
			if (node instanceof ReturnStatementNode) {
				ReturnStatementNode ret = (ReturnStatementNode) node;
				ExpressionNode value = ret.getValue();
				if (type == null) {
					if (value != null) {
						failures.add(new ValidationFailure(node, "Method does not return a value"));
					}
				} else {
					if (value == null) {
						failures.add(new ValidationFailure(node, "Method requires a return value of type " + type));
					} else {
						if (!value.getType().isAssignableTo(type.getSite())) {
							failures.add(new ValidationFailure(node, "Return value is of incorrect type: " + value.getType() + ", should be: " + type));
						}
					}
				}
			}

			for (Node child : node.getChildren()) {
				validate(child);
			}
		}

		public List<ValidationFailure> getFailures() {
			return failures;
		}
	}


}
