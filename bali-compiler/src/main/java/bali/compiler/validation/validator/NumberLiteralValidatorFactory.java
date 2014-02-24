package bali.compiler.validation.validator;

import bali.Integer;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.NumberLiteralExpressionNode;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ParameterisedSite;
import bali.compiler.type.Site;
import bali.compiler.validation.ValidationFailure;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class NumberLiteralValidatorFactory implements ValidatorFactory {

	private Site site;

	public NumberLiteralValidatorFactory(ClassLibrary library) {
		this.site = new ParameterisedSite(library.getReference(Integer.class.getName()), false, true);
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
