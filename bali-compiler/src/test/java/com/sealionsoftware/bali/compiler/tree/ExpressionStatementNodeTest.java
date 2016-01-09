package com.sealionsoftware.bali.compiler.tree;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ExpressionStatementNodeTest {

    private ExpressionNode expressionNode = mock(ExpressionNode.class);
    private ExpressionStatementNode subject = new ExpressionStatementNode(2, 3, expressionNode);

    @Test
    public void testAccept() throws Exception {
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(same(subject));
    }

    @Test
    public void testGetExpressionNode(){
        assertThat(subject.getExpressionNode(), is(expressionNode));
    }

    @Test
    public void testGetLine() throws Exception {
        assertThat(subject.getLine(), CoreMatchers.equalTo(2));
    }

    @Test
    public void testGetCharacter() throws Exception {
        assertThat(subject.getCharacter(), CoreMatchers.equalTo(3));
    }

}