package com.sealionsoftware.bali.compiler.asm;

import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import com.sealionsoftware.bali.compiler.tree.Visitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class ASMStackVisitor implements Visitor, Opcodes {

    private MethodVisitor methodVisitor;
    private Deque<Label> scopeHorizonStack = new LinkedList<>();
    private List<VariableInfo> variables = new LinkedList<>();

    public ASMStackVisitor(MethodVisitor methodVisitor) {
        this.methodVisitor = methodVisitor;
        scopeHorizonStack.push(null);
    }

    public void visit(BooleanLiteralNode node) {
        methodVisitor.visitFieldInsn(GETSTATIC, "bali/Boolean", node.isTrue() ? "TRUE" : "FALSE", "Lbali/Boolean;");
    }

    public void visit(VariableNode node) {
        Label varStart = new Label();
        methodVisitor.visitLabel(varStart);
        node.getValue().accept(this);
        variables.add(new VariableInfo(
                node.getName(),
                varStart,
                scopeHorizonStack.peek()
        ));
        methodVisitor.visitVarInsn(ASTORE, variables.size());
    }

    public void visit(CodeBlockNode node) {

    }

    public List<VariableInfo> getVariables(){
        return variables;
    }
}
