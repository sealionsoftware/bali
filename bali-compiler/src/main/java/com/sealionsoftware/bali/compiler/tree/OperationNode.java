package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;

public class OperationNode extends InvocationNode {

    private String operatorName;

    public OperationNode(Integer line, Integer character, CompilationThreadManager monitor) {
        super(line, character, monitor);
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
