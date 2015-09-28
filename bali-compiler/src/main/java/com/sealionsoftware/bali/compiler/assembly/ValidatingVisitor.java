package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import com.sealionsoftware.bali.compiler.tree.Visitor;

import java.util.ArrayList;
import java.util.List;

public abstract class ValidatingVisitor implements Visitor {

    protected List<CompileError> failures = new ArrayList<>();

    public void visit(BooleanLiteralNode node) {
    }

    public void visit(TextLiteralNode node) {
    }

    public void visit(VariableNode node) {
    }

    public void visit(CodeBlockNode node) {
    }

    public void visit(TypeNode node) {
    }

    public List<CompileError> getFailures() {
        return failures;
    }
}
