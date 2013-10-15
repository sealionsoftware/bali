package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.OperationNode;
import bali.compiler.type.Operator;
import bali.compiler.type.Site;
import bali.compiler.type.TypeLibrary;
import bali.compiler.type.VanillaSite;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 03/07/13
 */
public class OperationValidator implements Validator {

	public List<ValidationFailure> validate(Node node, Control control) {

		control.validateChildrenNow();

		if (node instanceof OperationNode){

			OperationNode operation = (OperationNode) node;
			List<ValidationFailure> ret = new ArrayList<>();
			Site targetType = operation.getOne().getType();
			Site operandType = operation.getTwo().getType();
			String operatorName = operation.getOperator();

			Operator operator = getOperatorWithName(operatorName, targetType);

			if (operator == null) {
				ret.add(new ValidationFailure(node, "Type " + targetType + " has no operator " + operatorName));
				return ret;
			}

			if (!operandType.isAssignableTo(operator.getParameter())) {
				ret.add(new ValidationFailure(node, "Operator " + operator + " requires an operand of type " + operandType));
			}

			operation.setResolvedOperator(operator);
		}

		return Collections.emptyList();
	}

	public Operator getOperatorWithName(String name, Site site) {
		for (Operator operator : site.getOperators()) {
			if (operator.getName().equals(name)) {
				return operator;
			}
		}
		for (Site iface : site.getInterfaces()){
			Operator ret = getOperatorWithName(name, iface);
			if (ret != null){
				return ret;
			}
		}
		return null;
	}

	public void onCompletion() {
	}

}
