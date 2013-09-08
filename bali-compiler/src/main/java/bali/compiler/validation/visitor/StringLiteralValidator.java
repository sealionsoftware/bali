package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.StringLiteralExpressionNode;
import bali.compiler.validation.TypeLibrary;
import bali.compiler.validation.ValidationFailure;
import bali.compiler.validation.type.Site;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class StringLiteralValidator implements Validator<StringLiteralExpressionNode> {

	private Site stringSite;

	public StringLiteralValidator(TypeLibrary library) {
		this.stringSite = new Site(library.getType(String.class.getName()), new ArrayList<Site>());
	}

	public List<ValidationFailure> validate(StringLiteralExpressionNode literal) {
		//TODO: validate string literals
		literal.setType(stringSite);
		return new ArrayList<>();
	}
}
