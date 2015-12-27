package com.sealionsoftware.bali.compiler.tree;

public class ConditionalLoopNode extends ConditionalNode {

    public ConditionalLoopNode(Integer line, Integer character) {
        super(line, character);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
