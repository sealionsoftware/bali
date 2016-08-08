package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

public class TextLiteralNode extends LiteralNode<String> {

    private final MonitoredProperty<Type> type;

    public TextLiteralNode(Integer line, Integer character, CompilationThreadManager monitor) {
        super(line, character);
        this.type = new MonitoredProperty<>(this, "type", monitor);
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
}
