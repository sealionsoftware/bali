package bali.compiler.parser.tree;

import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class Field extends Declaration {

	private Value value;

	public Field() {
	}

	public Field(Integer line, Integer character) {
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
		if (value != null){
			children.add(value);
		}
		return children;
	}
}
