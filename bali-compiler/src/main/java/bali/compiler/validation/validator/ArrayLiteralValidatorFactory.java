package bali.compiler.validation.validator;

import bali.collection.ValueCollection;
import bali.compiler.parser.tree.ArrayLiteralExpressionNode;
import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.reference.Reference;
import bali.compiler.type.Class;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
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

	public Visitor createValidator(final ClassLibrary library, final ConstantLibrary constantLibrary) {
		return new Visitor(){

			private Reference<Class> arrayClass = library.getReference(ValueCollection.class.getName());

			public List<ValidationFailure> validate(Node node, Control control) {

				control.validateChildren();

				if (node instanceof ArrayLiteralExpressionNode) {

					ArrayLiteralExpressionNode literal = (ArrayLiteralExpressionNode) node;

					List<ValidationFailure> failures = new ArrayList<>();

					List<ExpressionNode> values = literal.getValues();
					if (values.isEmpty()) {
						failures.add(new ValidationFailure(literal, "ValueCollection literals must have at least one entry"));
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
