package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

public class IntegerLiteralNode extends ExpressionNode {

    private bali.Integer value;
    private final MonitoredProperty<Type> type;

    public IntegerLiteralNode(Integer line, Integer character, CompilationThreadManager monitor) {
        super(line, character);
        this.type =  new MonitoredProperty<>(this, "type", monitor);
    }

    public bali.Integer getValue() {
        return value;
    }

    public void setValue(bali.Integer value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void setType(Type type) {
        this.type.set(type);
    }

    public Type getType() {
        return type.get();
    }
}
