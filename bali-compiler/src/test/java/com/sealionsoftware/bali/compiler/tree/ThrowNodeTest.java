package com.sealionsoftware.bali.compiler.tree;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ThrowNodeTest {

    private ThrowNode subject = new ThrowNode(2, 3);

    @Test
    public void testGetPayload() throws Exception {

        ExpressionNode expressionMock = mock(ExpressionNode.class);

        subject.setPayload(expressionMock);
        assertThat(subject.getPayload(), is(expressionMock));

    }

    @Test
    public void testGetLine() throws Exception {
        assertThat(subject.getLine(), CoreMatchers.equalTo(2));
    }

    @Test
    public void testGetCharacter() throws Exception {
        assertThat(subject.getCharacter(), CoreMatchers.equalTo(3));
    }

    @Test
    public void testGetChildren() throws Exception {

        ExpressionNode expressionMock = mock(ExpressionNode.class);

        subject.setPayload(expressionMock);

        assertThat(subject.getChildren(), hasItems(expressionMock));
    }

    @Test
    public void testAccept() throws Exception {
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(same(subject));
    }


}