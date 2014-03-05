package bali.compiler.parser.tree;

/**
 * User: Richard
 * Date: 03/03/14
 */
public class ArgumentNode extends Node {

	private String name;
	private ExpressionNode value;

	public ArgumentNode(Integer line, Integer character) {
		super(line, character);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ExpressionNode getValue() {
		return value;
	}

	public void setValue(ExpressionNode value) {
		this.value = value;
		children.add(value);
	}

	public String toString() {
		return name == null ? value.toString() : name + ": " + value;
	}
}
