package bali.compiler.parser.tree;

import java.util.List;

/**
 * User: Richard
 * Date: 09/05/13
 */
public class WhileStatementNode extends LoopStatementNode {

	private ExpressionNode condition;

	public WhileStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public ExpressionNode getCondition() {
		return condition;
	}

	public void setCondition(ExpressionNode condition) {
		this.condition = condition;
	}

	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		children.add(condition);
		children.add(body);
		return children;
	}
}
