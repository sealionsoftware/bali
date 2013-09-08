package bali.compiler.parser.tree;

import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class FieldNode extends DeclarationNode {

	private ExpressionNode value;

	public FieldNode() {
	}

	public FieldNode(Integer line, Integer character) {
		super(line, character);
	}

	public ExpressionNode getValue() {
		return value;
	}

	public void setValue(ExpressionNode value) {
		this.value = value;
	}

	public Boolean getFinal() {
		return false;
	}

	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		if (value != null) {
			children.add(value);
		}
		return children;
	}
}
