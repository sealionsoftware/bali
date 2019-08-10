package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.tree.*;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ValidatingVisitorTest {

    private ValidatingVisitor subject = new ValidatingVisitor(){};

    @Test
    public void testVisitLogicLiteralNode() throws Exception {
        subject.visit(mock(LogicLiteralNode.class));
    }

    @Test
    public void testVisitTextLiteralNode() throws Exception {
        subject.visit(mock(TextLiteralNode.class));
    }

    @Test
    public void testVisitIntegerLiteralNode() throws Exception {
        subject.visit(mock(IntegerLiteralNode.class));
    }

    @Test
    public void testVisitArrayLiteralNode() throws Exception {
        subject.visit(mock(ArrayLiteralNode.class));
    }

    @Test
    public void testVisitCodeBlockNode() throws Exception {
        subject.visit(mock(CodeBlockNode.class));
    }

    @Test
    public void testVisitTypeNode() throws Exception {
        subject.visit(mock(TypeNode.class));
    }

    @Test
    public void testVisitVariableNode() throws Exception {
        subject.visit(mock(VariableNode.class));
    }

    @Test
    public void testVisitReferenceNode() throws Exception {
        subject.visit(mock(ReferenceNode.class));
    }

    @Test
    public void testVisitAssignmentNode() throws Exception {
        subject.visit(mock(AssignmentNode.class));
    }

    @Test
    public void testVisitConditionalStatementNode() throws Exception {
        subject.visit(mock(ConditionalStatementNode.class));
    }

    @Test
    public void testVisitConditionalLoopNode() throws Exception {
        subject.visit(mock(ConditionalLoopNode.class));
    }

    @Test
    public void testVisitInvocationNode() throws Exception {
        subject.visit(mock(InvocationNode.class));
    }

    @Test
    public void testVisitExpressionStatementNode() throws Exception {
        subject.visit(mock(ExpressionStatementNode.class));
    }

    @Test
    public void testVisitOperationNode() throws Exception {
        subject.visit(mock(OperationNode.class));
    }

    @Test
    public void testVisitExistenceCheckNode() throws Exception {
        subject.visit(mock(ExistenceCheckNode.class));
    }

    @Test
    public void testVisitIterationNode() throws Exception {
        subject.visit(mock(IterationNode.class));
    }

    @Test
    public void testVisiThrowNode() throws Exception {
        subject.visit(mock(ThrowNode.class));
    }

    @Test
    public void testVisitCatchStatementNode() throws Exception {
        subject.visit(mock(CatchStatementNode.class));
    }

    @Test
    public void testGetFailures() throws Exception {
        assertThat(subject.getFailures(), notNullValue());
    }

}