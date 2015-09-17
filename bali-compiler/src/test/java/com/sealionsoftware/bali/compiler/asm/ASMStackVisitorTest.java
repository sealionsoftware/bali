package com.sealionsoftware.bali.compiler.asm;

import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.Visitor;
import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ASMStackVisitorTest implements Opcodes {

    private MethodVisitor visitor = mock(MethodVisitor.class);
    private Visitor subject = new ASMStackVisitor(visitor);

    @Test
    public void testVisitLiteralTrue() throws Exception {

        BooleanLiteralNode node = new BooleanLiteralNode(0, 0);
        node.setValue(true);

        subject.visit(node);

        verify(visitor).visitFieldInsn(GETSTATIC, "bali/Boolean", "TRUE", "Lbali/Boolean;");
    }

    @Test
    public void testVisitLiteralFalse() throws Exception {

        BooleanLiteralNode node = new BooleanLiteralNode(0, 0);
        node.setValue(false);

        subject.visit(node);

        verify(visitor).visitFieldInsn(GETSTATIC, "bali/Boolean", "FALSE", "Lbali/Boolean;");
    }
}