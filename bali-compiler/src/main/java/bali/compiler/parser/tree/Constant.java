package bali.compiler.parser.tree;

import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class Constant extends Declaration {

	private Value value;

	public Constant(Integer line, Integer character) {
		super(line, character);
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		children.add(value);
		return children;
	}
}
