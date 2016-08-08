package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.tree.ArrayLiteralNode;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalLoopNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionStatementNode;
import com.sealionsoftware.bali.compiler.tree.IntegerLiteralNode;
import com.sealionsoftware.bali.compiler.tree.InvocationNode;
import com.sealionsoftware.bali.compiler.tree.LogicLiteralNode;
import com.sealionsoftware.bali.compiler.tree.OperationNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;

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

    public List<CompileError> getFailures() {
        return failures;
    }
}
