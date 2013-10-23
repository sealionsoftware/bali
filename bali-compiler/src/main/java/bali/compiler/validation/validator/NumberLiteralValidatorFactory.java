package bali.compiler.validation.validator;

import bali.Number;
import bali.compiler.parser.tree.BooleanLiteralExpressionNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.NumberLiteralExpressionNode;
import bali.compiler.type.ParametrizedSite;
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
public class NumberLiteralValidatorFactory implements ValidatorFactory {

	private Site site;

	public NumberLiteralValidatorFactory(TypeLibrary library) {
		this.site = new VanillaSite(library.getType(Number.class.getName()));
	}

	public Validator createValidator() {
		return new Validator() {
			public List<ValidationFailure> validate(Node node, Control control) {
				if (node instanceof NumberLiteralExpressionNode){
					NumberLiteralExpressionNode literal = (NumberLiteralExpressionNode) node;
					//TODO: validate number literals
					literal.setType(site);
				} else {
					control.validateChildren();
				}
				return Collections.emptyList();
			}
		};
	}

}
