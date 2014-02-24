package bali.compiler.validation.validator;

import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.UnaryOperationNode;
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

	public Validator createValidator() {

		return new Validator() {

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
				}

				return ret;
			}

			public UnaryOperator getUnaryOperatorWithName(String name, Type site) {
				for (UnaryOperator operator : site.getUnaryOperators()) {
					if (operator.getName().equals(name)) {
						return operator;
					}
				}
				for (Type iface : site.getInterfaces()){
					UnaryOperator ret = getUnaryOperatorWithName(name, iface);
					if (ret != null){
						return ret;
					}
				}
				return null;
			}
		};
	}

}
