package com.sealionsoftware.bali.compiler.tree;


import java.util.UUID;

public class VariableNode extends StatementNode {

    private String name;
    private TypeNode type;
    private ExpressionNode value;
    private UUID id = UUID.randomUUID();

    public VariableNode(Integer line, Integer character) {
        super(line, character);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExpressionNode getValue() {
        return value;
    }

    public void setValue(ExpressionNode value) {
        children.add(value);
        this.value = value;
    }

    public TypeNode getType() {
        return type;
    }

    public void setType(TypeNode type) {
        children.add(type);
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
