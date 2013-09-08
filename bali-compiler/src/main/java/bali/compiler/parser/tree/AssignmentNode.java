package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class AssignmentNode extends StatementNode {

	private ReferenceNode reference;
	private ExpressionNode value;

	public AssignmentNode() {
	}

	public AssignmentNode(Integer line, Integer character) {
		super(line, character);
	}

	public ReferenceNode getReference() {
		return reference;
	}

	public void setReference(ReferenceNode reference) {
		this.reference = reference;
	}

	public ExpressionNode getValue() {
		return value;
	}

	public void setValue(ExpressionNode value) {
		this.value = value;
	}

	public List<Node> getChildren() {
		List<Node> children = new ArrayList<>();
		children.add(value);
		children.add(reference);
		return children;
	}
}
