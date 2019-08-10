package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.tree.*;

import java.util.ArrayList;
import java.util.List;

public abstract class ValidatingVisitor extends DescendingVisitor {

    protected List<CompileError> failures = new ArrayList<>();

    public void visit(LogicLiteralNode node) {
        visitChildren(node);
    }

    public void visit(TextLiteralNode node) {
        visitChildren(node);
    }

    public void visit(IntegerLiteralNode node) {
        visitChildren(node);
    }

    public void visit(ArrayLiteralNode node) {
        visitChildren(node);
    }

    public void visit(VariableNode node) {
        visitChildren(node);
    }

    public void visit(AssignmentNode node) {
        visitChildren(node);
    }

    public void visit(CodeBlockNode node) {
        visitChildren(node);
    }

    public void visit(TypeNode node) {
        visitChildren(node);
    }

    public void visit(ReferenceNode node) {
        visitChildren(node);
    }

    public void visit(ConditionalStatementNode node) {
        visitChildren(node);
    }

    public void visit(ConditionalLoopNode node) {
        visitChildren(node);
    }

    public void visit(InvocationNode node) {
        visitChildren(node);
    }

    public void visit(OperationNode node) {
        visitChildren(node);
    }

    public void visit(ExpressionStatementNode node) {
        visitChildren(node);
    }

    public void visit(ExistenceCheckNode node) {
        visitChildren(node);
    }

    public void visit(IterationNode node) {
        visitChildren(node);
    }

    public void visit(ThrowNode node) { visitChildren(node); }

    public void visit(CatchStatementNode node) { visitChildren(node); }

    public List<CompileError> getFailures() {
        return failures;
    }
}
