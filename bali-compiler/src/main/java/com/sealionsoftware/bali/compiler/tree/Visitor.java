package com.sealionsoftware.bali.compiler.tree;


public interface Visitor {

    void visit(BooleanLiteralNode node);

    void visit(TextLiteralNode node);

    void visit(IntegerLiteralNode node);

    void visit(VariableNode node);

    void visit(AssignmentNode node);

    void visit(TypeNode node);

    void visit(CodeBlockNode node);

    void visit(ReferenceNode node);

    void visit(ConditionalStatementNode node);

    void visit(ConditionalLoopNode node);

}
