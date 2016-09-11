package com.sealionsoftware.bali.compiler.tree;


public interface Visitor {

    void visit(LogicLiteralNode node);

    void visit(TextLiteralNode node);

    void visit(IntegerLiteralNode node);

    void visit(ArrayLiteralNode node);

    void visit(VariableNode node);

    void visit(AssignmentNode node);

    void visit(TypeNode node);

    void visit(CodeBlockNode node);

    void visit(ReferenceNode node);

    void visit(ConditionalStatementNode node);

    void visit(ConditionalLoopNode node);

    void visit(InvocationNode node);

    void visit(OperationNode node);

    void visit(ExpressionStatementNode node);

    void visit(ExistenceCheckNode node);

}
