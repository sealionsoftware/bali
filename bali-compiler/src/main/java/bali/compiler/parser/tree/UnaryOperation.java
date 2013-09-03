package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/07/13
 */
public class UnaryOperation extends Expression {

	private Expression target;
	private String operator;

	private Method resolvedMethod;

	public UnaryOperation() {
	}

	public UnaryOperation(Integer line, Integer character) {
		super(line, character);
	}

	public Expression getTarget() {
		return target;
	}

	public void setTarget(Expression target) {
		this.target = target;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Method getMethod() {
		return resolvedMethod;
	}

	public void setMethod(Method method) {
		this.resolvedMethod = method;
	}

	public TypeReference getType() {
		return null;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(target);
		return children;
	}
}
