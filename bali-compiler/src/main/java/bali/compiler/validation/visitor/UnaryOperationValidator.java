package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.UnaryOperationNode;
import bali.compiler.type.Site;
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
public class UnaryOperationValidator implements Validator<UnaryOperationNode> {

	public List<ValidationFailure> validate(UnaryOperationNode node) {

		List<ValidationFailure> ret = new ArrayList<>();
		Site targetType = node.getTarget().getType();
		String operatorName = node.getOperator();

		UnaryOperator operator = getUnaryOperatorWithName(operatorName, targetType);

		if (operator == null) {
			ret.add(new ValidationFailure(node, "Type " + targetType + " has no method for operator " + operator));
			return ret;
		}

		node.setResolvedOperator(operator);
		return ret;
	}

	public UnaryOperator getUnaryOperatorWithName(String name, Site site) {
		for (UnaryOperator operator : site.getUnaryOperators()) {
			if (operator.getName().equals(name)) {
				return operator;
			}
		}
		for (Site iface : site.getInterfaces()){
			UnaryOperator ret = getUnaryOperatorWithName(name, iface);
			if (ret != null){
				return ret;
			}
		}
		return null;
	}

}
