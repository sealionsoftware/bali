package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.assembly.VariableData;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

public class IterationNode extends StatementNode {

    private String identifier;
    private ExpressionNode target;
    private StatementNode statement;
    private final MonitoredProperty<VariableData> itemData;

    public IterationNode(Integer line, Integer character, CompilationThreadManager monitor) {
        super(line, character);
        itemData = new MonitoredProperty<>(this, "itemData", monitor);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public ExpressionNode getTarget() {
        return target;
    }

    public void setTarget(ExpressionNode target) {
        this.target = target;
        this.children.add(target);
    }

    public StatementNode getStatement() {
        return statement;
    }

    public void setStatement(StatementNode statement) {
        this.statement = statement;
        this.children.add(statement);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void setItemData(VariableData data) {
        itemData.set(data);
    }

    public VariableData getItemData() {
        return itemData.get();
    }
}
