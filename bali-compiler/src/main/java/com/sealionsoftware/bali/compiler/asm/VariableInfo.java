package com.sealionsoftware.bali.compiler.asm;

import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.objectweb.asm.Label;

public class VariableInfo {

    public final VariableNode node;
    public final Label start;
    public final Label end;

    public VariableInfo(VariableNode node, Label start, Label end) {
        this.node = node;
        this.start = start;
        this.end = end;
    }

}
