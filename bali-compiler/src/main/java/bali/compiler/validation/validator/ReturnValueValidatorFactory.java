package bali.compiler.validation.validator;

import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ReturnStatementNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.parser.tree.StatementNode;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.type.Site;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ReturnValueValidatorFactory implements ValidatorFactory {

	public Validator createValidator(final ClassLibrary library, final ConstantLibrary constantLibrary) {

		return new Validator() {

			private Site type;

			public List<ValidationFailure> validate(Node node, Control control) {

				if (node instanceof MethodDeclarationNode) {
					return validate((MethodDeclarationNode) node, control);
				} else if (node instanceof ReturnStatementNode) {
					return validate((ReturnStatementNode) node);
				}
				control.validateChildren();
				return Collections.emptyList();
			}

			// Engages methods, ensures that their return values are correct
			public List<ValidationFailure> validate(MethodDeclarationNode method, Control control) {

				List<ValidationFailure> failures = new ArrayList<>();

				SiteNode declaredReturnType = method.getType();
				if (declaredReturnType != null){
					type = declaredReturnType.getSite();
				}

				List<StatementNode> statements = method.getBody().getStatements();
				StatementNode lastStatement = statements.size() > 0 ? statements.get(statements.size() - 1) : null;

				if (method.getType() != null) {
					if (!(lastStatement instanceof ReturnStatementNode)) {
						failures.add(new ValidationFailure(method, "Method does not return a " + method.getType()));
					}
				} else if (!(lastStatement instanceof ReturnStatementNode)) {
					statements.add(new ReturnStatementNode());
				}

				control.validateChildren();
				type = null;

				return failures;
			}

			public List<ValidationFailure> validate(ReturnStatementNode node) {

				List<ValidationFailure> failures = new ArrayList<>();
				ExpressionNode value = node.getValue();
				if (type == null) {
					if (value != null) {
						failures.add(new ValidationFailure(node, "Method does not return a value"));
					}
				} else {
					if (value == null) {
						failures.add(new ValidationFailure(node, "Method requires a return value of type " + type));
					} else {
						if (!value.getType().isAssignableTo(type)) {
							failures.add(new ValidationFailure(node, "Return value is of incorrect type: " + value.getType() + ", should be: " + type));
						}
					}
				}
				return failures;
			}
		};
	}
}
