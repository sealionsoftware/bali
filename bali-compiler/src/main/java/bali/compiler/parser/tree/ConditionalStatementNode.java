package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ConditionalStatementNode extends ControlExpressionNode {

	private ExpressionNode condition;
	private ControlExpressionNode conditional;
	private ControlExpressionNode alternate;

	public ConditionalStatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public ExpressionNode getCondition() {
		return condition;
	}

	public void setCondition(ExpressionNode condition) {
		children.add(condition);
		this.condition = condition;
	}

	public ControlExpressionNode getConditional() {
		return conditional;
	}

	public void setConditional(ControlExpressionNode conditional) {
		children.add(conditional);
		this.conditional = conditional;
	}

	public ControlExpressionNode getAlternate() {
		return alternate;
	}

	public void setAlternate(ControlExpressionNode alternate) {
		this.alternate = alternate;
		children.add(alternate);
	}
}
