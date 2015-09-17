package com.sealionsoftware.bali.compiler.tree;

public class BooleanLiteralNode extends StatementNode {

    private Boolean value;

    public BooleanLiteralNode(Integer line, Integer character) {
        super(line, character);
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public boolean isTrue() {
        return Boolean.TRUE.equals(value);
    }
}
