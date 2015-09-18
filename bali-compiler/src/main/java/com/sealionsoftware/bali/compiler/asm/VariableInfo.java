package com.sealionsoftware.bali.compiler.asm;

import org.objectweb.asm.Label;

public class VariableInfo {

    private String name;
    private Label start;
    private Label end;

    public VariableInfo(String name, Label start, Label end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public Label getStart() {
        return start;
    }

    public Label getEnd() {
        return end;
    }

}
