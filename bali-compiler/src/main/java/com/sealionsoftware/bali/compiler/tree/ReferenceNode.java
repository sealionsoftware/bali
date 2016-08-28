package com.sealionsoftware.bali.compiler.tree;


import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.assembly.VariableData;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

public class ReferenceNode extends ExpressionNode {

    private String name;
    private MonitoredProperty<VariableData> variableData;

    public ReferenceNode(Integer line, Integer character, CompilationThreadManager monitor) {
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

    public Site getSite() {
        return variableData.get().type;
    }
}
