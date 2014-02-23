package bali.compiler.validation.validator;

import bali.String;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.StringLiteralExpressionNode;
import bali.compiler.reference.SimpleReference;
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
public class StringLiteralValidatorFactory implements ValidatorFactory {

	private Site site;

	public StringLiteralValidatorFactory(ClassLibrary library) {
		this.site = new ParameterisedSite(library.getReference(String.class.getName()), false, true);
	}

	public Validator createValidator() {
		return new Validator() {
			public List<ValidationFailure> validate(Node node, Control control) {
				if (node instanceof StringLiteralExpressionNode){
					StringLiteralExpressionNode literal = (StringLiteralExpressionNode) node;
					//TODO: validate string literals
					literal.setType(site);
				} else {
					control.validateChildren();
				}
				return Collections.emptyList();
			}
		};
	}

}
