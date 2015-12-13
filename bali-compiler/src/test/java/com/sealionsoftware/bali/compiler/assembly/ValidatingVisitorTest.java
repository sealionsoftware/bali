package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.tree.AssignmentNode;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.Control;
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
        subject.visit(mock(BooleanLiteralNode.class), mock(Control.class));
    }

    @Test
    public void testVisitTextLiteralNode() throws Exception {
        subject.visit(mock(TextLiteralNode.class), mock(Control.class));
    }

    @Test
    public void testVisitCodeBlockNode() throws Exception {
        subject.visit(mock(CodeBlockNode.class), mock(Control.class));
    }

    @Test
    public void testVisitTypeNode() throws Exception {
        subject.visit(mock(TypeNode.class), mock(Control.class));
    }

    @Test
    public void testVisitVariableNode() throws Exception {
        subject.visit(mock(VariableNode.class), mock(Control.class));
    }

    @Test
    public void testVisitReferenceNode() throws Exception {
        subject.visit(mock(ReferenceNode.class), mock(Control.class));
    }

    @Test
    public void testVisitAssignmentNode() throws Exception {
        subject.visit(mock(AssignmentNode.class), mock(Control.class));
    }

    @Test
    public void testGetFailures() throws Exception {
        assertThat(subject.getFailures(), notNullValue());
    }
}