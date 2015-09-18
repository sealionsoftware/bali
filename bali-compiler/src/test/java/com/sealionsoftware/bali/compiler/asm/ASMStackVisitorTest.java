package com.sealionsoftware.bali.compiler.asm;

import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.junit.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ASMStackVisitorTest implements Opcodes {

    private MethodVisitor visitor = mock(MethodVisitor.class);
    private ASMStackVisitor subject = new ASMStackVisitor(visitor);

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

    @Test
    public void testVisitVariableDeclaration() throws Exception {

        ExpressionNode expressionNode = mock(ExpressionNode.class);

        VariableNode node = new VariableNode(0, 0);
        node.setName("aVariable");
        node.setValue(expressionNode);

        subject.visit(node);

        verify(visitor).visitLabel(any(Label.class));
        verify(visitor).visitVarInsn(ASTORE, 1);

        List<VariableInfo> variables = subject.getVariables();
        assertThat(variables, notNullValue());
        assertThat(variables.size(), equalTo(1));
        VariableInfo variableInfo = variables.get(0);
        assertThat(variableInfo.getName(), equalTo("aVariable"));
    }
}