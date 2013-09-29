package bali.compiler.validation.visitor;

import bali.Number;
import bali.compiler.parser.tree.NumberLiteralExpressionNode;
import bali.compiler.type.ParametrizedSite;
import bali.compiler.type.Site;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class NumberLiteralValidator implements Validator<NumberLiteralExpressionNode> {

	private Site site;

	public NumberLiteralValidator(TypeLibrary library) {
		this.site = new ParametrizedSite(library.getType(Number.class.getName()), Collections.<Site>emptyList());
	}

	public List<ValidationFailure> validate(NumberLiteralExpressionNode literal) {
		//TODO: validate boolean literals
		literal.setType(site);
		return new ArrayList<>();
	}
}
