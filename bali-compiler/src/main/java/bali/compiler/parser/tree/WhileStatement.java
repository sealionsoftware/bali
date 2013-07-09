package bali.compiler.parser.tree;

import java.util.List;

/**
 * User: Richard
 * Date: 09/05/13
 */
public class WhileStatement extends LoopStatement {

	private Expression condition;

	public WhileStatement(Integer line, Integer character) {
		super(line, character);
	}

	public Expression getCondition() {
		return condition;
	}

	public void setCondition(Expression condition) {
		this.condition = condition;
	}

	public List<Node> getChildren() {
		List<Node> children = super.getChildren();
		children.add(condition);
		children.add(body);
		return children;
	}
}
