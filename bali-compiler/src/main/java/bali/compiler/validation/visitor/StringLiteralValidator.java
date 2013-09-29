package bali.compiler.validation.visitor;

import bali.String;
import bali.compiler.parser.tree.StringLiteralExpressionNode;
import bali.compiler.type.ParametrizedSite;
import bali.compiler.type.Site;
import bali.compiler.type.TypeLibrary;
import bali.compiler.type.VanillaSite;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class StringLiteralValidator implements Validator<StringLiteralExpressionNode> {

	private Site stringSite;

	public StringLiteralValidator(TypeLibrary library) {
		this.stringSite = new VanillaSite(library.getType(String.class.getName()));
	}

	public List<ValidationFailure> validate(StringLiteralExpressionNode literal) {
		//TODO: validate string literals
		literal.setType(stringSite);
		return new ArrayList<>();
	}
}
