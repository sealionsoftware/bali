package bali.compiler.validation.visitor;

import bali.collection.Array;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.ListLiteralExpressionNode;
import bali.compiler.validation.TypeLibrary;
import bali.compiler.validation.ValidationFailure;
import bali.compiler.validation.type.Site;
import bali.compiler.validation.type.Type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ListLiteralValidator implements Validator<ListLiteralExpressionNode> {

	private Type arrayType;

	public ListLiteralValidator(TypeLibrary library) {
		this.arrayType = library.getType(Array.class.getName());
	}

	public List<ValidationFailure> validate(ListLiteralExpressionNode literal) {

		List<ValidationFailure> failures = new ArrayList<>();

		List<ExpressionNode> values = literal.getValues();
		if (values.isEmpty()) {
			failures.add(new ValidationFailure(literal, "List literals must have at least one entry"));
		}

		Iterator<ExpressionNode> i = literal.getValues().iterator();
		Site listEntryType = i.next().getType();

		while(i.hasNext()){
			ExpressionNode nextExpression = i.next();
			Site nextEntryType = nextExpression.getType();
			if (!nextEntryType.isAssignableTo(listEntryType)) {
				failures.add(new ValidationFailure(nextExpression, "Element is not of a compatible type for the list"));
			}
		}

		List<Site> params = new ArrayList<>();
		params.add(listEntryType);

		literal.setType(new Site(
				arrayType,
				params
		));

		return failures;
	}
}
