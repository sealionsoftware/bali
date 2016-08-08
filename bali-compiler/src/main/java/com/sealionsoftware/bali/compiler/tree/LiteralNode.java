package com.sealionsoftware.bali.compiler.tree;

public abstract class LiteralNode<T> extends ExpressionNode {

    private T value;

    public LiteralNode(Integer line, Integer character) {
        super(line, character);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
