package bali.compiler.validation.validator;

import bali.Boolean;
import bali.compiler.parser.tree.BooleanLiteralExpressionNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.type.ParameterisedSite;
import bali.compiler.type.Site;
import bali.compiler.validation.ValidationFailure;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class BooleanLiteralValidatorFactory implements ValidatorFactory {

	public Visitor createValidator(final ClassLibrary library, final ConstantLibrary constantLibrary) {

		return new Visitor() {

			private Site site = new ParameterisedSite(library.getReference(Boolean.class.getName()), false, true);

			public List<ValidationFailure> validate(Node node, Control control) {
				if (node instanceof BooleanLiteralExpressionNode) {
					BooleanLiteralExpressionNode literal = (BooleanLiteralExpressionNode) node;
					//TODO: visit boolean literals
					literal.setType(site);
				} else {
					control.validateChildren();
				}
				return Collections.emptyList();
			}

		};
	}
}
