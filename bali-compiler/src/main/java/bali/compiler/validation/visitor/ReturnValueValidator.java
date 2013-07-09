package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.Expression;
import bali.compiler.parser.tree.Method;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ReturnStatement;
import bali.compiler.parser.tree.Statement;
import bali.compiler.parser.tree.Type;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ReturnValueValidator implements Validator<Method> {

	// Engages methods, ensures that their return values are correct
	public List<ValidationFailure> validate(Method method) {

		ReturnValueValidatorAgent agent = new ReturnValueValidatorAgent(method.getType());
		agent.validate(method.getBody());
		List<ValidationFailure> failures = agent.getFailures();

		List<Statement> statements = method.getBody().getStatements();
		Statement lastStatement = statements.size() > 0 ? statements.get(statements.size() - 1) : null;

		if (method.getType() != null) {
			if (!(lastStatement instanceof ReturnStatement)) {
				failures.add(new ValidationFailure(lastStatement, "Method does not return a " + method.getType()));
			}
		} else if (!(lastStatement instanceof ReturnStatement)) {
			statements.add(new ReturnStatement());
		}

		return failures;
	}

	public static class ReturnValueValidatorAgent {

		private Type type;
		private List<ValidationFailure> failures = new ArrayList<>();

		public ReturnValueValidatorAgent(Type type) {
			this.type = type;
		}

		public void validate(Node node) {
			if (node instanceof ReturnStatement) {
				ReturnStatement ret = (ReturnStatement) node;
				Expression value = ret.getValue();
				if (type == null) {
					if (value != null) {
						failures.add(new ValidationFailure(node, "Method does not return a value"));
					}
				} else {
					if (value == null) {
						failures.add(new ValidationFailure(node, "Method requires a return value of type " + type));
					} else {
						if (!value.getType().equals(type)) {
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
