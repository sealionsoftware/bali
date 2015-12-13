package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

public class BooleanLiteralNode extends ExpressionNode {

    private Boolean value;
    private final MonitoredProperty<Type> type;

    public BooleanLiteralNode(Integer line, Integer character, CompilationThreadManager monitor) {
        super(line, character);
        this.type =  new MonitoredProperty<>(this, "type", monitor);
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this, new ListControl(children, visitor));
    }

    public boolean isTrue() {
        return Boolean.TRUE.equals(value);
    }

    public void setType(Type type) {
        this.type.set(type);
    }

    public Type getType() {
        return type.get();
    }
}
