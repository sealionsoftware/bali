package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.Assignment;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Expression;
import bali.compiler.parser.tree.Field;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.Type;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class AssignmentValidator implements Validator<CompilationUnit> {

	private AssignmentAgent assignmentAgent = new AssignmentAgent();
	private FieldAgent fieldAgent = new FieldAgent();

	public List<ValidationFailure> validate(CompilationUnit unit) {
		return walk(unit);
	}

	public List<ValidationFailure> walk(Node node) {
		List<ValidationFailure> failures = new ArrayList<>();
		if (node instanceof Assignment) {
			failures.addAll(assignmentAgent.validate((Assignment) node));
		}
		if (node instanceof Field) {
			failures.addAll(fieldAgent.validate((Field) node));
		}
		for (Node child : node.getChildren()) {
			failures.addAll(walk(child));
		}
		return failures;
	}

	public static class AssignmentAgent implements Validator<Assignment> {

		public List<ValidationFailure> validate(Assignment statement) {

			List<ValidationFailure> failures = new ArrayList<>();

			Type site = statement.getReference().getType();
			Type value = statement.getValue().getType();

			if (value == null || !value.isAssignableTo(site)) {
				failures.add(new ValidationFailure(statement, "Cannot assign expression of type " + value + " to reference of type " + site));
			}

			return failures;
		}

	}

	public static class FieldAgent implements Validator<Field> {

		public List<ValidationFailure> validate(Field field) {

			List<ValidationFailure> failures = new ArrayList<>();

			Type siteType = field.getType();
			Expression value = field.getValue();

			if (value != null){
				Type valueType = value.getType();

				if (!valueType.isAssignableTo(siteType)) {
					failures.add(new ValidationFailure(field, "Cannot assign expression of type " + valueType + " to reference of type " + siteType));
				}
			}

			return failures;
		}

	}
}
