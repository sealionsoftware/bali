package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.BooleanLiteralExpressionNode;
import bali.compiler.type.Site;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class BooleanLiteralValidator implements Validator<BooleanLiteralExpressionNode> {

	private Site site;

	public BooleanLiteralValidator(TypeLibrary library) {
		this.site = new Site(library.getType(Boolean.class.getName()), new ArrayList<Site>());
	}

	public List<ValidationFailure> validate(BooleanLiteralExpressionNode literal) {
		//TODO: validate boolean literals
		literal.setType(site);
		return new ArrayList<>();
	}
}
