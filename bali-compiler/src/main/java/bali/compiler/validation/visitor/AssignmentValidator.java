package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.AssignmentNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.FieldNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ReferenceNode;
import bali.compiler.parser.tree.SiteNode;
import bali.compiler.validation.ValidationFailure;
import bali.compiler.validation.type.Site;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class AssignmentValidator implements Validator<CompilationUnitNode> {

	private AssignmentAgent assignmentAgent = new AssignmentAgent();
	private FieldAgent fieldAgent = new FieldAgent();

	public List<ValidationFailure> validate(CompilationUnitNode unit) {
		return walk(unit);
	}

	public List<ValidationFailure> walk(Node node) {
		List<ValidationFailure> failures = new ArrayList<>();
		if (node instanceof AssignmentNode) {
			failures.addAll(assignmentAgent.validate((AssignmentNode) node));
		}
		if (node instanceof FieldNode) {
			failures.addAll(fieldAgent.validate((FieldNode) node));
		}
		for (Node child : node.getChildren()) {
			failures.addAll(walk(child));
		}
		return failures;
	}

	public static class AssignmentAgent implements Validator<AssignmentNode> {

		public List<ValidationFailure> validate(AssignmentNode statement) {

			List<ValidationFailure> failures = new ArrayList<>();

			ReferenceNode reference = statement.getReference();
			Site site = reference.getType();
			Site value = statement.getValue().getType();

			if (value == null || !value.isAssignableTo(site)) {
				failures.add(new ValidationFailure(statement, "Cannot assign expression of type " + value + " to reference of type " + site));
			}
			if (reference.getDeclaration().getFinal()){
				failures.add(new ValidationFailure(statement, "Cannot assign an expression to a constant reference"));
			}

			return failures;
		}

	}

	public static class FieldAgent implements Validator<FieldNode> {

		public List<ValidationFailure> validate(FieldNode field) {

			List<ValidationFailure> failures = new ArrayList<>();


			Site value = field.getValue().getType();

			if (value != null){
				Site site = field.getType().getSite();

				if (!value.isAssignableTo(site)) {
					failures.add(new ValidationFailure(field, "Cannot assign expression of type " + value + " to reference of type " + site));
				}
			}

			return failures;
		}

	}
}
