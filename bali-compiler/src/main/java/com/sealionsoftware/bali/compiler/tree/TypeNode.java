package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;

import java.util.List;

public class TypeNode extends Node {

    private String name;
    private List<TypeNode> arguments;

    private MonitoredProperty<Type> resolvedType;

    public TypeNode(CompilationThreadManager monitor, Integer line, Integer character) {
        super(line, character);
        resolvedType = new MonitoredProperty<>(this, "resolvedType", monitor);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TypeNode> getArguments() {
        return arguments;
    }

    public void setArguments(List<TypeNode> arguments) {
        children.addAll(arguments);
        this.arguments = arguments;
    }

    public Type getResolvedType() {
        return resolvedType.get();
    }

    public void setResolvedType(Type resolvedType) {
        this.resolvedType.set(resolvedType);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this, new ListControl(children, visitor));
    }

    public String toString() {
        return "TypeNode{" +
                "name='" + name + '\'' +
                ", arguments=" + arguments +
                '}';
    }
}
