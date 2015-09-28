package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;

public class BooleanLiteralNode extends ExpressionNode {

    private Boolean value;
    private Type type;

    public BooleanLiteralNode(Integer line, Integer character) {
        super(line, character);
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        super.accept(visitor);
    }

    public boolean isTrue() {
        return Boolean.TRUE.equals(value);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
