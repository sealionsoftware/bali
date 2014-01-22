package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Site;

/**
 * User: Richard
 * Date: 29/04/13
 */
public abstract class AssignmentNode extends StatementNode {

	private ExpressionNode value;

	public AssignmentNode() {
	}

	public AssignmentNode(Integer line, Integer character) {
		super(line, character);
	}

	public ExpressionNode getValue() {
		return value;
	}

	public void setValue(ExpressionNode value) {
		this.value = value;
		children.add(value);
	}

	public abstract Site getType();

}
