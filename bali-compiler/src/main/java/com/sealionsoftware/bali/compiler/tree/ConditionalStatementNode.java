package com.sealionsoftware.bali.compiler.tree;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ConditionalStatementNode extends StatementNode {

	private ExpressionNode condition;
	private StatementNode conditional;

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

	public StatementNode getConditional() {
		return conditional;
	}

	public void setConditional(StatementNode conditional) {
		children.add(conditional);
		this.conditional = conditional;
	}

    public void accept(Visitor visitor) {
        visitor.visit(this, new ListControl(children, visitor));
    }

}
