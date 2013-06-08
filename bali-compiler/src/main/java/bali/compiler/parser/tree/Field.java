package bali.compiler.parser.tree;

import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class Field extends Declaration {

	private Expression value;

	public Field() {
	}

	public Field(Integer line, Integer character) {
		super(line, character);
	}

	public Expression getValue() {
		return value;
	}

	public void setValue(Expression value) {
		this.value = value;
	}

	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		if (value != null) {
			children.add(value);
		}
		return children;
	}
}
