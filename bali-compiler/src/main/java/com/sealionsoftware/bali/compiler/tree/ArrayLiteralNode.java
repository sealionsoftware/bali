package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

import java.util.List;

public class ArrayLiteralNode extends ExpressionNode {

    private List<ExpressionNode> items;
    private final MonitoredProperty<Type> type;

    public ArrayLiteralNode(Integer line, Integer character, CompilationThreadManager monitor) {
        super(line, character);
        this.type = new MonitoredProperty<>(this, "type", monitor);
    }

    public List<ExpressionNode> getItems() {
        return items;
    }

    public void setItems(List<ExpressionNode> items) {
        this.items = items;
        children.addAll(items);
    }

    public void setType(Type type){
        this.type.set(type);
    }

    public Type getType() {
        return type.get();
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
