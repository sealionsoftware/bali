package bali.compiler.validation.visitor;

import bali.String;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.NumberLiteralExpressionNode;
import bali.compiler.parser.tree.StringLiteralExpressionNode;
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
public class StringLiteralValidator implements Validator {

	private Site site;

	public StringLiteralValidator(TypeLibrary library) {
		this.site = new VanillaSite(library.getType(String.class.getName()));
	}

	public List<ValidationFailure> validate(Node node, Control control) {
		if (node instanceof StringLiteralExpressionNode){
			StringLiteralExpressionNode literal = (StringLiteralExpressionNode) node;
			//TODO: validate string literals
			literal.setType(site);
		}
		return Collections.emptyList();
	}

	public void onCompletion() {
	}
}
