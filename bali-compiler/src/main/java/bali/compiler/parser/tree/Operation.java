package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/07/13
 */
public class Operation extends Expression {

	private Expression one;
	private Expression two;
	private String operator;

	private Type type;
	private String method;

	public Operation() {
	}

	public Operation(Integer line, Integer character) {
		super(line, character);
	}

	public void setOne(Expression one) {
		this.one = one;
	}

	public void setTwo(Expression two) {
		this.two = two;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Expression getOne() {
		return one;
	}

	public Expression getTwo() {
		return two;
	}

	public String getOperator() {
		return operator;
	}

	public String getMethod() {
		return method;
	}

	public Type getType() {
		return type;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(one);
		children.add(two);
		return children;
	}
}
