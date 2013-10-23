package bali.compiler.validation.validator;

import bali.Boolean;
import bali.compiler.parser.tree.BooleanLiteralExpressionNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.type.Site;
import bali.compiler.type.TypeLibrary;
import bali.compiler.type.VanillaSite;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class BooleanLiteralValidatorFactory implements ValidatorFactory {

	private Site site;

	public BooleanLiteralValidatorFactory(TypeLibrary library) {
		this.site = new VanillaSite(library.getType(Boolean.class.getName()));
	}

	public Validator createValidator() {

		return new Validator() {

			public List<ValidationFailure> validate(Node node, Control control) {
				if (node instanceof BooleanLiteralExpressionNode) {
					BooleanLiteralExpressionNode literal = (BooleanLiteralExpressionNode) node;
					//TODO: validate boolean literals
					literal.setType(site);
				} else {
					control.validateChildren();
				}
				return Collections.emptyList();
			}

		};
	}
}
