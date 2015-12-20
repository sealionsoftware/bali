package com.sealionsoftware.bali.compiler.asm;

import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.assembly.VariableData;
import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.Control;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ASMStackVisitorTest implements Opcodes {

    private CompilationThreadManager monitor = mock(CompilationThreadManager.class);
    private MethodVisitor visitor = mock(MethodVisitor.class);
    private ASMStackVisitor subject = new ASMStackVisitor(visitor);

    @Test
    public void testVisitLiteralTrue() throws Exception {

        BooleanLiteralNode node = new BooleanLiteralNode(0, 0, monitor);
        node.setValue(true);

        subject.visit(node, mock(Control.class));

        verify(visitor).visitFieldInsn(GETSTATIC, "bali/Boolean", "TRUE", "Lbali/Boolean;");
    }

    @Test
    public void testVisitLiteralFalse() throws Exception {

        BooleanLiteralNode node = new BooleanLiteralNode(0, 0, monitor);
        node.setValue(false);

        subject.visit(node, mock(Control.class));

        verify(visitor).visitFieldInsn(GETSTATIC, "bali/Boolean", "FALSE", "Lbali/Boolean;");
    }

    @Test
    public void testVisitTextLiteral() throws Exception {
        TextLiteralNode node = mock(TextLiteralNode.class);
        subject.visit(node, mock(Control.class));
        verify(visitor).visitLdcInsn(node.getValue());
        verify(visitor).visitMethodInsn(INVOKESTATIC, "bali/text/Primitive", "convert", "(Ljava/lang/String;)Lbali/Text;", false);

    }

    @Test
    public void testVisitVariableDeclaration() throws Exception {

        ExpressionNode expressionNode = mock(ExpressionNode.class);

        VariableNode node = new VariableNode(0, 0);
        node.setName("aVariable");
        node.setValue(expressionNode);

        subject.visit(node, mock(Control.class));

        verify(visitor).visitLabel(any(Label.class));
        verify(visitor).visitVarInsn(ASTORE, 1);

        List<VariableInfo> variables = subject.getVariables();
        assertThat(variables, notNullValue());
        assertThat(variables.size(), equalTo(1));
        VariableInfo variableInfo = variables.get(0);
        assertThat(variableInfo.node.getName(), equalTo("aVariable"));
    }

    @Test
    public void testVisitAssignment() throws Exception {

        ExpressionNode expressionNode = mock(ExpressionNode.class);
        ReferenceNode referenceNode = mock(ReferenceNode.class);

        VariableNode setupNode = new VariableNode(0, 0);
        setupNode.setName("aVariable");
        setupNode.setValue(mock(ExpressionNode.class));

        subject.visit(setupNode, mock(Control.class));

        VariableNode variableNode = mock(VariableNode.class);
        when(variableNode.getId()).thenReturn(setupNode.getId());

        VariableData data = new VariableData(
                "aVariable", mock(Type.class), setupNode.getId()
        );
        when(referenceNode.getVariableData()).thenReturn(data);

        AssignmentNode node = new AssignmentNode(0, 0);
        node.setValue(expressionNode);
        node.setTarget(referenceNode);

        subject.visit(node, mock(Control.class));

        verify(visitor, times(2)).visitVarInsn(ASTORE, 1);
    }

    @Test
     public void testVisitCodeBlock() throws Exception {
        Control mockControl = mock(Control.class);
        CodeBlockNode mockNode = mock(CodeBlockNode.class);
        subject.visit(mockNode, mockControl);
        verify(mockControl).visitChildren();
    }

    @Test
    public void testVisitTypeNode() throws Exception {
        Control mockControl = mock(Control.class);
        TypeNode mockNode = mock(TypeNode.class);
        subject.visit(mockNode, mockControl);
        verify(mockControl).visitChildren();
    }

    @Test
    public void testVisitReferecenceNode() throws Exception {
        ReferenceNode mockNode = mock(ReferenceNode.class);
        subject.visit(mockNode, mock(Control.class));
        verifyZeroInteractions(mockNode);
    }

    @Test
    public void testVisitConditionalNode() throws Exception {
        Control mockControl = mock(Control.class);
        ConditionalStatementNode mockNode = mock(ConditionalStatementNode.class);
        when(mockNode.getCondition()).thenReturn(mock(ExpressionNode.class));
        when(mockNode.getConditional()).thenReturn(mock(ExpressionNode.class));
        subject.visit(mockNode, mockControl);
        verifyZeroInteractions(mockControl);
    }
}