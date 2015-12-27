package com.sealionsoftware.bali.compiler.tree;


public class AssignmentNode extends StatementNode {

    private ReferenceNode target;
    private ExpressionNode value;

    public AssignmentNode(Integer line, Integer character) {
        super(line, character);
    }

    public ReferenceNode getTarget() {
        return target;
    }

    public void setTarget(ReferenceNode name) {
        children.add(name);
        this.target = name;
    }

    public ExpressionNode getValue() {
        return value;
    }

    public void setValue(ExpressionNode value) {
        children.add(value);
        this.value = value;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
