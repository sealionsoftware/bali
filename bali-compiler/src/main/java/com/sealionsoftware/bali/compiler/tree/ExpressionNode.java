package com.sealionsoftware.bali.compiler.tree;

public class ExpressionNode extends StatementNode {

    public ExpressionNode(Integer line, Integer character) {
        super(line, character);
    }

    public void accept(Visitor visitor) {

    }
}
