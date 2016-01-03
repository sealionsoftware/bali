package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;

public abstract class ExpressionNode extends Node {

    public ExpressionNode(Integer line, Integer character) {
        super(line, character);
    }

    public abstract Type getType();

}
