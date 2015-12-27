package com.sealionsoftware.bali.compiler.tree;

public class ConditionalStatementNode extends ConditionalNode {

    public ConditionalStatementNode(Integer line, Integer character) {
		super(line, character);
	}

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
