package com.sealionsoftware.bali.compiler.tree;

public class ConditionalStatementNode extends ConditionalNode {

    private StatementNode contraConditional;

    public ConditionalStatementNode(Integer line, Integer character) {
		super(line, character);
	}

    public StatementNode getContraConditional() {
        return contraConditional;
    }

    public void setContraConditional(StatementNode conditional) {
        children.add(conditional);
        this.contraConditional = conditional;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
