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

	private String method;
	private Type type;

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

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(target);
		return children;
	}
}
