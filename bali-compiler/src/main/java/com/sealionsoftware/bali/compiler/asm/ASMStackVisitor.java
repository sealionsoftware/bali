package com.sealionsoftware.bali.compiler.asm;

import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import com.sealionsoftware.bali.compiler.tree.Visitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMStackVisitor implements Visitor, Opcodes {

    private MethodVisitor methodVisitor;

    public ASMStackVisitor(MethodVisitor methodVisitor) {
        this.methodVisitor = methodVisitor;
    }

    public void visit(BooleanLiteralNode node) {
        methodVisitor.visitFieldInsn(GETSTATIC, "bali/Boolean", node.isTrue() ? "TRUE" : "FALSE", "Lbali/Boolean;");
    }

    public void visit(VariableNode node) {

    }
}
