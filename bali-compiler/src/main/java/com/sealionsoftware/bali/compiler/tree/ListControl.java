package com.sealionsoftware.bali.compiler.tree;

import java.util.List;

public class ListControl implements Control {

    private final List<Node> children;
    private final Visitor visitor;

    public ListControl(List<Node> children, Visitor visitor) {
        this.children = children;
        this.visitor = visitor;
    }

    public void visitChildren() {
        for (Node child : children){
            child.accept(visitor);
        }
    }
}
