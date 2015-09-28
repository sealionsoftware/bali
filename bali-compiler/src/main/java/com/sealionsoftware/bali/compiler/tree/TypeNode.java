package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;

import java.util.List;

public class TypeNode extends Node {

    private String name;
    private List<TypeNode> arguments;

    private Type resolvedType;

    public TypeNode(Integer line, Integer character) {
        super(line, character);
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
        return resolvedType;
    }

    public void setResolvedType(Type resolvedType) {
        this.resolvedType = resolvedType;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        super.accept(visitor);
    }
}
