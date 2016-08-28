package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Site;

public abstract class ExpressionNode extends Node {

    public ExpressionNode(Integer line, Integer character) {
        super(line, character);
    }

    public abstract Site getSite();

}
