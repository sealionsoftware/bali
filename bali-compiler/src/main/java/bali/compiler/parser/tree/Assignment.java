package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class Assignment extends Statement {

	private Reference reference;
	private Expression value;

	public Assignment(Integer line, Integer character) {
		super(line, character);
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public Expression getValue() {
		return value;
	}

	public void setValue(Expression value) {
		this.value = value;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(value);
		children.add(reference);
		return children;
	}
}
