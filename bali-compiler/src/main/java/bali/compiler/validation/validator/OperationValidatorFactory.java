package bali.compiler.validation.validator;

import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.OperationNode;
import bali.compiler.type.Operator;
import bali.compiler.type.Site;
import bali.compiler.type.Type;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 03/07/13
 */
public class OperationValidatorFactory implements ValidatorFactory {

	public Validator createValidator() {

		return new Validator() {

			public List<ValidationFailure> validate(Node node, Control control) {

				control.validateChildren();

				if (node instanceof OperationNode){

					OperationNode operation = (OperationNode) node;
					List<ValidationFailure> ret = new ArrayList<>();
					Site targetType = operation.getOne().getType();
					Site operandType = operation.getTwo().getType();
					String operatorName = operation.getOperator();

					if (targetType == null){
						ret.add(new ValidationFailure(node, "The target of operator " + operatorName + " is void"));
						return ret;
					}

					Operator operator = getOperatorWithName(operatorName, targetType);

					if (operator == null) {
						ret.add(new ValidationFailure(node, "Class " + targetType + " has no operator " + operatorName));
						return ret;
					}

					if (!operandType.isAssignableTo(operator.getParameter())) {
						ret.add(new ValidationFailure(node, "Operator " + operator + " requires an operand of type " + operandType));
					}

					operation.setResolvedOperator(operator);
				}

				return Collections.emptyList();
			}

			public Operator getOperatorWithName(String name, Type site) {
				for (Operator operator : site.getOperators()) {
					if (operator.getName().equals(name)) {
						return operator;
					}
				}
				for (Type iface : site.getInterfaces()){
					Operator ret = getOperatorWithName(name, iface);
					if (ret != null){
						return ret;
					}
				}
				return null;
			}
		};
	}

}
