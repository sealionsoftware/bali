package com.sealionsoftware.bali.compiler.tree;

public class VariableNode extends StatementNode {

    private String name;
    private ExpressionNode value;

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
        this.value = value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
        super.accept(visitor);
    }
}
