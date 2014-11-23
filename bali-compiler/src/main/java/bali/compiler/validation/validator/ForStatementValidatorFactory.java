package bali.compiler.validation.validator;

import bali.compiler.parser.tree.ExpressionNode;
import bali.compiler.parser.tree.ForStatementNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.type.Site;
import bali.compiler.type.Type;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class ForStatementValidatorFactory implements ValidatorFactory {

	private static final String ITERABLE_CLASS_NAME = bali.Iterable.class.getName();

	public Visitor createValidator(final ClassLibrary library, final ConstantLibrary constantLibrary) {

		return new Visitor() {

			public List<ValidationFailure> validate(Node node, Control control) {
				List<ValidationFailure> failures = new ArrayList<>();
				if (node instanceof ForStatementNode) {
					failures.addAll(validate((ForStatementNode) node));
				}
				control.validateChildren();

				return failures;
			}

			public List<ValidationFailure> validate(ForStatementNode statement) {

				List<ValidationFailure> failures = new ArrayList<>();

				ExpressionNode collection = statement.getCollection();
				Site iterableType = collection.getType();
				Type iterableInterface = getIterableInterface(iterableType);

				if (iterableInterface == null) {
					failures.add(new ValidationFailure(collection, "For statement argument is not an Iterable"));
					return failures;
				}

				Site elementType = statement.getElement().getType().getSite();
				Site iteratedType = iterableInterface.getTypeArguments().get(0);

				if (!iteratedType.isAssignableTo(elementType)) {
					failures.add(new ValidationFailure(collection, "Collection elements are not assignable to the declared variable"));
					return failures;
				}

				return failures;
			}

			private Type getIterableInterface(Type in){
				if (ITERABLE_CLASS_NAME.equals(in.getTemplate().getName())){
					return in;
				}
				for (Type ifaceType : in.getSuperTypes()){
					if (ITERABLE_CLASS_NAME.equals(ifaceType.getTemplate().getName())){
						return ifaceType;
					}
				}
				for (Type ifaceType : in.getInterfaces()){
					if (ITERABLE_CLASS_NAME.equals(ifaceType.getTemplate().getName())){
						return ifaceType;
					}
				}
				return null;
			}

		};
	}
}
