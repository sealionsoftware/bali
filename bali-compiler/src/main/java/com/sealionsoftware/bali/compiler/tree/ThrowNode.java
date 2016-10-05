package com.sealionsoftware.bali.compiler.tree;

public class ThrowNode extends StatementNode {

    private ExpressionNode payload;

    public ThrowNode(Integer line, Integer character) {
        super(line, character);
    }

    public ExpressionNode getPayload() {
        return payload;
    }

    public void setPayload(ExpressionNode payload) {
        this.payload = payload;
        children.add(payload);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
