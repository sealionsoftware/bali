package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class Invocation extends Expression {

	private Expression target;
	private String method;
	private List<Expression> arguments = new ArrayList<>();

	private Type returnType;

	public Invocation(Integer line, Integer character) {
		super(line, character);
	}

	public Type getType() {
		return returnType;
	}

	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}

	public void setTarget(Expression target) {
		this.target = target;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void addArgument(Expression argument) {
		arguments.add(argument);
	}

	public Expression getTarget() {
		return target;
	}

	public String getMethod() {
		return method;
	}

	public List<Expression> getArguments() {
		return arguments;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		if (target != null) {
			children.add(target);
		}
		children.addAll(arguments);
		return children;
	}
}
