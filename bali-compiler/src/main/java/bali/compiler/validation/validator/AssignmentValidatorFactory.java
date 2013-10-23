package bali.compiler.validation.validator;

import bali.compiler.parser.tree.AssignmentNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.FieldNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.ReferenceNode;
import bali.compiler.type.Site;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class AssignmentValidatorFactory implements ValidatorFactory {

	public Validator createValidator() {

		return new Validator() {

			public List<ValidationFailure> validate(Node node, Control control) {
				if (node instanceof AssignmentNode) {
					return validate((AssignmentNode) node);
				} else if (node instanceof FieldNode) {
					return validate((FieldNode) node);
				}

				control.validateChildren();
				return Collections.emptyList();
			}

			public List<ValidationFailure> validate(AssignmentNode statement) {

				List<ValidationFailure> failures = new ArrayList<>();

				ReferenceNode reference = statement.getReference();
				Site site = reference.getType();
				Site value = statement.getValue().getType();

				if (value == null || !value.isAssignableTo(site)) {
					failures.add(new ValidationFailure(statement, "Cannot assign expression of type " + value + " to reference of type " + site));
				}
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
