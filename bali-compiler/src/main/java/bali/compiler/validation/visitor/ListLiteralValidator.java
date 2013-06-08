package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Expression;
import bali.compiler.parser.tree.ListLiteralExpression;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.Type;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ListLiteralValidator implements Validator<CompilationUnit> {

	private Agent agent = new Agent();

	public List<ValidationFailure> validate(CompilationUnit unit) {
		return walk(unit);
	}

	public List<ValidationFailure> walk(Node node) {
		List<ValidationFailure> failures = new ArrayList<>();
		if (node instanceof ListLiteralExpression) {
			failures.addAll(agent.validate((ListLiteralExpression) node));
		}
		for (Node child : node.getChildren()) {
			failures.addAll(walk(child));
		}
		return failures;
	}

	public static class Agent implements Validator<ListLiteralExpression> {

		public List<ValidationFailure> validate(ListLiteralExpression literal) {

			List<ValidationFailure> failures = new ArrayList<>();

			Type listType = null;
			List<Expression> values = literal.getValues();
			if (values.isEmpty()) {
				failures.add(new ValidationFailure(literal, "List literals must have at least one entry"));
			}

			for (Expression listElement : literal.getValues()) {
				Type listElementType = listElement.getType();
				if (listElementType != null) {
					if (listType == null || !listElementType.isAssignableTo(listType)) {
						if (listType == null || listType.isAssignableTo(listElementType)) {
							listType = listElementType;
							listType.setErase(true);
						} else {
							failures.add(new ValidationFailure(listElement, "Element is not of a compatible type for the list"));
						}
					}
				}
			}

			literal.setListType(listType);

			return failures; //TODO: validate list literals
		}

	}
}
