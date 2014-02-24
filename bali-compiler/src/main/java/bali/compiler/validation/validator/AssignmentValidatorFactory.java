package bali.compiler.validation.validator;

import bali.compiler.parser.tree.AssignmentNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.FieldNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ReferenceAssignmentNode;
import bali.compiler.parser.tree.ReferenceNode;
import bali.compiler.type.Site;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class AssignmentValidatorFactory implements ValidatorFactory {

	public Validator createValidator() {

		return new Validator() {

			public List<ValidationFailure> validate(Node node, Control control) {
				List<ValidationFailure> failures = new ArrayList<>();
				if (node instanceof AssignmentNode) {
					failures.addAll(validate((AssignmentNode) node));
				} else if (node instanceof FieldNode) {
					failures.addAll(validate((FieldNode) node));
				}
				control.validateChildren();

				return failures;
			}

			public List<ValidationFailure> validate(AssignmentNode statement) {

				List<ValidationFailure> failures = new ArrayList<>();

				Site site = statement.getType();
				ExpressionNode value = statement.getValue();

				if (value != null){
					if (!value.getType().isAssignableTo(site)){
						failures.add(new ValidationFailure(statement, "Cannot assign expression of type " + value.getType() + " to reference of type " + site));
					}
				}

				if (statement instanceof ReferenceAssignmentNode){
					failures.addAll(validate((ReferenceAssignmentNode) statement));
				}

				return failures;
			}

			public List<ValidationFailure> validate(ReferenceAssignmentNode statement) {

				List<ValidationFailure> failures = new ArrayList<>();
				ReferenceNode reference = statement.getReference();
				String name = reference.getName();

				if (reference.getFinal()) {
					failures.add(new ValidationFailure(statement, "Cannot assign an expression to a constant reference"));
				}
				if (reference.getTarget() != null){
					statement.setSetterName("set" + name.substring(0, 1).toUpperCase() + name.substring(1));
				}

				return failures;
			}

			public List<ValidationFailure> validate(FieldNode field) {

				List<ValidationFailure> failures = new ArrayList<>();

				ExpressionNode value = field.getValue();

				if (value != null) {
					Site site = field.getType().getSite();
					if (!value.getType().isAssignableTo(site)) {
						failures.add(new ValidationFailure(field, "Cannot assign expression of type " + value + " to reference of type " + site));
					}
				}

				return failures;
			}
		};
	}
}
