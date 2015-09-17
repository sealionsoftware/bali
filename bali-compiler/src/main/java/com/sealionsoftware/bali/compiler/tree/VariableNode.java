package com.sealionsoftware.bali.compiler.tree;

public class VariableNode extends StatementNode {

    private String name;
    private ExpressionNode value;

    protected VariableNode(Integer line, Integer character) {
        super(line, character);
    }

    public void accept(Visitor visitor) {

    }
}
