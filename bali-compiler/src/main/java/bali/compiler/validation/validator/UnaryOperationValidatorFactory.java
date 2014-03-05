package bali.compiler.validation.validator;

import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.NullCheckNode;
import bali.compiler.parser.tree.UnaryOperationNode;
import bali.compiler.reference.Reference;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.type.ParameterisedSite;
import bali.compiler.type.Site;
import bali.compiler.type.Type;
import bali.compiler.type.UnaryOperator;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates a Unary Operation Declaration
 * <p/>
 * Requires the target to have a type set
 * <p/>
 * Resolves the operator name to an inferred method
 * Sets the return type of the expression
 * <p/>
 * User: Richard
 * Date: 03/07/13
 */
public class UnaryOperationValidatorFactory implements ValidatorFactory {

	public Validator createValidator(final ClassLibrary library, ConstantLibrary constantLibrary) {

		return new Validator() {

			private Reference<bali.compiler.type.Class> nullCheckClass = library.getReference(bali.Boolean.class.getName());

			public List<ValidationFailure> validate(Node node, Control control) {

				control.validateChildren();
				List<ValidationFailure> ret = new ArrayList<>();

				if (node instanceof UnaryOperationNode){

					UnaryOperationNode unary = (UnaryOperationNode) node;

					Site targetType = unary.getTarget().getType();
					String operatorName = unary.getOperator();

					if (targetType == null){
						ret.add(new ValidationFailure(node, "The target of operator " + operatorName + " is void"));
						return ret;
					}

					UnaryOperator operator = getUnaryOperatorWithName(operatorName, targetType);

					if (operator == null) {
						ret.add(new ValidationFailure(node, "Class " + targetType + " has no method for operator " + operatorName));
						return ret;
					}

					unary.setResolvedOperator(operator);
				} else if (node instanceof NullCheckNode){
					NullCheckNode nullCheckNode = (NullCheckNode) node;
					nullCheckNode.setType(new ParameterisedSite(nullCheckClass, false, nullCheckNode.getTarget().getType().isThreadSafe()));
				}

				return ret;
			}

			public UnaryOperator getUnaryOperatorWithName(String name, Type site) {
				for (UnaryOperator operator : site.getUnaryOperators()) {
					if (operator.getName().equals(name)) {
						return operator;
					}
				}
				return null;
			}
		};
	}

}
