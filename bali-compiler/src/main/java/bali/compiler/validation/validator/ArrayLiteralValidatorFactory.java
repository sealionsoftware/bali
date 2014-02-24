package bali.compiler.validation.validator;

import bali.collection.Array;
import bali.compiler.parser.tree.ArrayLiteralExpressionNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.reference.Reference;
import bali.compiler.type.Class;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ParameterisedSite;
import bali.compiler.type.Site;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ArrayLiteralValidatorFactory implements ValidatorFactory {

	private Reference<Class> arrayClass;

	public ArrayLiteralValidatorFactory(ClassLibrary library) {
		this.arrayClass = library.getReference(Array.class.getName());
	}

	public Validator createValidator() {

		return new Validator(){
			public List<ValidationFailure> validate(Node node, Control control) {

				control.validateChildren();

				if (node instanceof ArrayLiteralExpressionNode) {

					ArrayLiteralExpressionNode literal = (ArrayLiteralExpressionNode) node;

					List<ValidationFailure> failures = new ArrayList<>();

					List<ExpressionNode> values = literal.getValues();
					if (values.isEmpty()) {
						failures.add(new ValidationFailure(literal, "Array literals must have at least one entry"));
					}

					Iterator<ExpressionNode> i = literal.getValues().iterator();
					Site listEntryType = i.next().getType();

					while (i.hasNext()) {
						ExpressionNode nextExpression = i.next();
						Site nextEntryType = nextExpression.getType();
						if (!nextEntryType.isAssignableTo(listEntryType)) {
							failures.add(new ValidationFailure(nextExpression, "Element is not of a compatible type for the list"));
						}
					}

					List<Site> params = new ArrayList<>();
					params.add(listEntryType);

					literal.setType(new ParameterisedSite(
							arrayClass,
							params,
							false,
							true
					));

					return failures;
				}

				return Collections.emptyList();
			}
		};
	}
}
