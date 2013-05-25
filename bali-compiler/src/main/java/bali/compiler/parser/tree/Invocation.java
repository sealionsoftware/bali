package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class Invocation extends Value {

	private Value target;
	private String method;
	private List<Value> arguments = new ArrayList<>();

	private String valueTypeName;

	public Invocation(Integer line, Integer character) {
		super(line, character);
	}

	public void setValueTypeName(String valueTypeName) {
		this.valueTypeName = valueTypeName;
	}

	public String getValueTypeName() {
		return valueTypeName;
	}

	public void setTarget(Value target) {
		this.target = target;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void addArgument(Value argument){
		arguments.add(argument);
	}

	public Value getTarget() {
		return target;
	}

	public String getMethod() {
		return method;
	}

	public List<Value> getArguments() {
		return arguments;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(target);
		children.addAll(arguments);
		return children;
	}
}
