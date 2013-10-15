package bali.compiler.validation.visitor;

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
public class NumberLiteralValidator implements Validator {

	private Site site;

	public NumberLiteralValidator(TypeLibrary library) {
		this.site = new VanillaSite(library.getType(Number.class.getName()));
	}

	public List<ValidationFailure> validate(Node node, Control control) {
		if (node instanceof NumberLiteralExpressionNode){
			NumberLiteralExpressionNode literal = (NumberLiteralExpressionNode) node;
			//TODO: validate number literals
			literal.setType(site);
		}
		return Collections.emptyList();
	}

	public void onCompletion() {
	}
}
