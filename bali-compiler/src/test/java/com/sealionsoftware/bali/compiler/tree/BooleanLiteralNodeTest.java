package com.sealionsoftware.bali.compiler.tree;

import org.junit.Test;

import java.util.List;

import static com.sealionsoftware.Matchers.isEmpty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BooleanLiteralNodeTest {

    private BooleanLiteralNode subject = new BooleanLiteralNode(2, 3);

    @Test
    public void testSetValue() throws Exception {
        subject.setValue(true);
        assertThat(subject.isTrue(), equalTo(true));
    }

    @Test
    public void testAccept() throws Exception {
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(subject);
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
    public void testGetChildren() throws Exception {
        List<Node> children = subject.getChildren();
        assertThat(children, isEmpty());
    }

}