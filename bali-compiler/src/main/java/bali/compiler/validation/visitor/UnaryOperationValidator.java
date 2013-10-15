package bali.compiler.validation.visitor;

import bali.Boolean;
import bali.compiler.parser.tree.Node;
import bali.compiler.parser.tree.UnaryOperationNode;
import bali.compiler.type.Site;
import bali.compiler.type.TypeLibrary;
import bali.compiler.type.UnaryOperator;
import bali.compiler.type.VanillaSite;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
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
public class UnaryOperationValidator implements Validator {

	public static final String NULL_CHECK_OPERATOR_NAME = "?";

	private UnaryOperator nullCheck;

	public UnaryOperationValidator(TypeLibrary library) {
		this.nullCheck = new UnaryOperator(
				"?",
				new VanillaSite(library.getType(Boolean.class.getName())),
				null
		);
	}

	public List<ValidationFailure> validate(Node node, Control control) {

		if (node instanceof UnaryOperationNode){

			UnaryOperationNode unary = (UnaryOperationNode) node;
			List<ValidationFailure> ret = new ArrayList<>();
			Site targetType = unary.getTarget().getType();
			String operatorName = unary.getOperator();

			UnaryOperator operator = operatorName.equals(NULL_CHECK_OPERATOR_NAME) ? nullCheck : getUnaryOperatorWithName(operatorName, targetType);

			if (operator == null) {
				ret.add(new ValidationFailure(node, "Type " + targetType + " has no method for operator " + operatorName));
				return ret;
			}

			unary.setResolvedOperator(operator);
		}

		return Collections.emptyList();
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

	public void onCompletion() {
	}

}
