package com.sealionsoftware.bali.compiler.tree;

public abstract class ConditionalNode extends StatementNode {

    private ExpressionNode condition;
    private StatementNode conditional;

    public ConditionalNode(Integer line, Integer character) {
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

}
