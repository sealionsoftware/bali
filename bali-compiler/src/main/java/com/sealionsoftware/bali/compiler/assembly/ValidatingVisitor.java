package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.Control;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import com.sealionsoftware.bali.compiler.tree.Visitor;

import java.util.ArrayList;
import java.util.List;

public abstract class ValidatingVisitor implements Visitor {

    protected List<CompileError> failures = new ArrayList<>();

    public void visit(BooleanLiteralNode node, Control control) {
        control.visitChildren();
    }

    public void visit(TextLiteralNode node, Control control) {
        control.visitChildren();
    }

    public void visit(VariableNode node, Control control) {
        control.visitChildren();
    }

    public void visit(AssignmentNode node, Control control) {
        control.visitChildren();
    }

    public void visit(CodeBlockNode node, Control control) {
        control.visitChildren();
    }

    public void visit(TypeNode node, Control control) {
        control.visitChildren();
    }

    public void visit(ReferenceNode node, Control control) {
        control.visitChildren();
    }

    public void visit(ConditionalStatementNode node) {
    }

    public List<CompileError> getFailures() {
        return failures;
    }
}
