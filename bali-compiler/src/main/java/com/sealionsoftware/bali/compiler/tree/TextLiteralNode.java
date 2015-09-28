package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;

public class TextLiteralNode extends ExpressionNode {

    private String value;
    private Type type;

    public TextLiteralNode(Integer line, Integer character) {
        super(line, character);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
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
