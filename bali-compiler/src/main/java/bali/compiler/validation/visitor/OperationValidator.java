package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.OperationNode;
import bali.compiler.type.Operator;
import bali.compiler.type.Site;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 03/07/13
 */
public class OperationValidator implements Validator<OperationNode> {

	public List<ValidationFailure> validate(OperationNode node) {

		List<ValidationFailure> ret = new ArrayList<>();
		Site targetType = node.getOne().getType();
		Site operandType = node.getTwo().getType();
		String operatorName = node.getOperator();

		Operator operator = targetType.getOperatorWithName(operatorName);

		if (operator == null) {
			ret.add(new ValidationFailure(node, "Type " + targetType + " has no operator " + operatorName));
			return ret;
		}

		if (!operandType.isAssignableTo(operator.getParameter())) {
			ret.add(new ValidationFailure(node, "Operator " + operator + " requires an operand of type " + operandType));
		}

		node.setResolvedOperator(operator);
		return ret;
	}


}
