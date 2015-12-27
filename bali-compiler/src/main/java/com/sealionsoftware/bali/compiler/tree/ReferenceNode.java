package com.sealionsoftware.bali.compiler.tree;


import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.assembly.VariableData;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

public class ReferenceNode extends Node {

    private String name;

    private MonitoredProperty<VariableData> variableData;

    public ReferenceNode(CompilationThreadManager monitor, Integer line, Integer character) {
        super(line, character);
        variableData = new MonitoredProperty<>(this, "variableData", monitor);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VariableData getVariableData() {
        return variableData.get();
    }

    public void setVariableData(VariableData variableData) {
        this.variableData.set(variableData);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
