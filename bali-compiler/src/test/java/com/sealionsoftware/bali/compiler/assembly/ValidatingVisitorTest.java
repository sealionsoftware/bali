package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalLoopNode;
import com.sealionsoftware.bali.compiler.tree.ConditionalStatementNode;
import com.sealionsoftware.bali.compiler.tree.IntegerLiteralNode;
import com.sealionsoftware.bali.compiler.tree.ReferenceNode;
import com.sealionsoftware.bali.compiler.tree.TextLiteralNode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ValidatingVisitorTest {

    private ValidatingVisitor subject = new ValidatingVisitor(){};

    @Test
    public void testVisitBooleanLiteralNode() throws Exception {
        subject.visit(mock(BooleanLiteralNode.class));
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
    public void testGetFailures() throws Exception {
        assertThat(subject.getFailures(), notNullValue());
    }
}