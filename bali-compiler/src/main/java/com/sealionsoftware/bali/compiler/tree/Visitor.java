package com.sealionsoftware.bali.compiler.tree;


public interface Visitor {

    void visit(BooleanLiteralNode node, Control control);

    void visit(TextLiteralNode node, Control control);

    void visit(VariableNode node, Control control);

    void visit(AssignmentNode node, Control control);

    void visit(TypeNode node, Control control);

    void visit(CodeBlockNode node, Control control);

    void visit(ReferenceNode node, Control control);

    void visit(ConditionalStatementNode node, Control control);

}
