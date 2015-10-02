package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.tree.Node;

public class CompileError {

    public final ErrorCode code;
    public final Node node;

    public CompileError(ErrorCode code, Node node) {
        this.code = code;
        this.node = node;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompileError that = (CompileError) o;
        return code == that.code;
    }

    public String toString() {
        return code.name();
    }
}
