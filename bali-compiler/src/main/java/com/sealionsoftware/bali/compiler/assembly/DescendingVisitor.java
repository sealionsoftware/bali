package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.tree.Node;
import com.sealionsoftware.bali.compiler.tree.Visitor;

public abstract class DescendingVisitor implements Visitor {

    public void visitChildren(Node node) {
        for(Node child : node.getChildren()){
            child.accept(this);
        }
    }
}
