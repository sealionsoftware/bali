package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class ConstructionValue extends Value {

	private Type type;
	private List<Value> arguments = new ArrayList<>();

	public ConstructionValue(Integer line, Integer character) {
		super(line, character);
	}

	public String getValueTypeName() {
		return type.getQualifiedClassName();
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void addArgument(Value argument){
		arguments.add(argument);
	}

	public Type getType() {
		return type;
	}

	public List<Value> getArguments() {
		return arguments;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(type);
		children.addAll(arguments);
		return children;
	}
}
