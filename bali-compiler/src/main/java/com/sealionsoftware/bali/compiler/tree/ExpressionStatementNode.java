package com.sealionsoftware.bali.compiler.tree;


public class ExpressionStatementNode extends StatementNode {

    private final ExpressionNode expressionNode;

    public ExpressionStatementNode(Integer line, Integer character, ExpressionNode node) {
        super(line, character);
        children.add(node);
        this.expressionNode = node;
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
