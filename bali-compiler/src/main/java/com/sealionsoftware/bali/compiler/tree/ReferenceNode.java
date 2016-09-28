package com.sealionsoftware.bali.compiler.tree;


import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.assembly.ReferenceData;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

public class ReferenceNode extends ExpressionNode {

    private String name;
    private MonitoredProperty<ReferenceData> referenceData;

    public ReferenceNode(Integer line, Integer character, CompilationThreadManager monitor) {
        super(line, character);
        referenceData = new MonitoredProperty<>(this, "referenceData", monitor);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ReferenceData getReferenceData() {
        return referenceData.get();
    }

    public void setReferenceData(ReferenceData referenceData) {
        this.referenceData.set(referenceData);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Site getSite() {
        return referenceData.get().type;
    }
}
