package com.sealionsoftware.bali.compiler.bytecode;

import org.objectweb.asm.Label;

public class VariableInfo {

    public final String name;
    public final Label start;
    public final Label end;

    public VariableInfo(String name, Label start, Label end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

}
