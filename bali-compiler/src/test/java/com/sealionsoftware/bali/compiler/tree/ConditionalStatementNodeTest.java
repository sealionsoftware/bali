package com.sealionsoftware.bali.compiler.tree;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConditionalStatementNodeTest {

    private ConditionalStatementNode subject = new ConditionalStatementNode(2,3);

    @Test
    public void testAccept() throws Exception {
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(same(subject), isA(Control.class));
    }

    @Test
    public void testGetLine() throws Exception {
        assertThat(subject.getLine(), equalTo(2));
    }

    @Test
    public void testGetCharacter() throws Exception {
        assertThat(subject.getCharacter(), equalTo(3));
    }


    @Test
    public void testGetCondition() throws Exception {
        ExpressionNode expressionNode = mock(ExpressionNode.class);
        subject.setCondition(expressionNode);
        assertThat(subject.getCondition(), is(expressionNode));
    }


    @Test
    public void testGetConditional() throws Exception {
        StatementNode statementNode = mock(StatementNode.class);
        subject.setConditional(statementNode);
        assertThat(subject.getConditional(), is(statementNode));
    }

}