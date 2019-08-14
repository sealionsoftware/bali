package com.sealionsoftware.bali.compiler.bytecode;

import org.objectweb.asm.Label;

public class CatchInfo {

    public final Label start;
    public final Label end;

    public CatchInfo(Label start, Label end) {
        this.start = start;
        this.end = end;
    }
}
