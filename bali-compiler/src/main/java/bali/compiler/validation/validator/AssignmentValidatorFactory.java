package bali.compiler.validation.validator;

import bali.compiler.parser.tree.AssignmentNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.FieldNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ReferenceAssignmentNode;
import bali.compiler.parser.tree.ReferenceNode;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.type.Site;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class AssignmentValidatorFactory implements ValidatorFactory {

	public Visitor createValidator(final ClassLibrary library, final ConstantLibrary constantLibrary) {

		return new Visitor() {

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

				if (reference.getFinal()) {
					failures.add(new ValidationFailure(statement, "Cannot assign an expression to a constant reference"));
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
