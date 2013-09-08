package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.NumberLiteralExpressionNode;
import bali.compiler.validation.TypeLibrary;
import bali.compiler.validation.ValidationFailure;
import bali.compiler.validation.type.Site;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class NumberLiteralValidator implements Validator<NumberLiteralExpressionNode> {

	private Site site;

	public NumberLiteralValidator(TypeLibrary library) {
		this.site = new Site<>(library.getType(Number.class.getName()), new ArrayList<Site>());
	}

	public List<ValidationFailure> validate(NumberLiteralExpressionNode literal) {
		//TODO: validate boolean literals
		literal.setType(site);
		return new ArrayList<>();
	}
}
