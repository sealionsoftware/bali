package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

public class TextLiteralNode extends ExpressionNode {

    private String value;
    private final MonitoredProperty<Type> type;

    public TextLiteralNode(Integer line, Integer character, CompilationThreadManager monitor) {
        super(line, character);
        this.type = new MonitoredProperty<>(this, "type", monitor);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(Type type) {
        this.type.set(type);
    }

    public Type getType() {
        return type.get();
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String getValue() {
        return value;
    }
}
