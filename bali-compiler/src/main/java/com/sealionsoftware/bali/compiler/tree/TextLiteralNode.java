package com.sealionsoftware.bali.compiler.tree;

public class TextLiteralNode extends ExpressionNode {

    private String value;

    public TextLiteralNode(Integer line, Integer character) {
        super(line, character);
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        super.accept(visitor);
    }

    public String getValue() {
        return value;
    }
}
