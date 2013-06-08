package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class ConstructionExpression extends Expression {

	private Type type;
	private List<Expression> arguments = new ArrayList<>();

	public ConstructionExpression(Integer line, Integer character) {
		super(line, character);
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void addArgument(Expression argument) {
		arguments.add(argument);
	}

	public Type getType() {
		return type;
	}

	public List<Expression> getArguments() {
		return arguments;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(type);
		children.addAll(arguments);
		return children;
	}
}
