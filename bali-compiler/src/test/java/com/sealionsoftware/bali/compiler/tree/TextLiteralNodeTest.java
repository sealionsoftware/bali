package com.sealionsoftware.bali.compiler.tree;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static com.sealionsoftware.Matchers.isEmpty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TextLiteralNodeTest {

    private TextLiteralNode subject = new TextLiteralNode(2, 3);

    @Test
    public void testSetValue() throws Exception {
        subject.setValue("Hello World");
        assertThat(subject.getValue(), equalTo("Hello World"));
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
        assertThat(subject.getChildren(), isEmpty());
    }

    @Test
    public void testAccept() throws Exception {
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(subject);
    }
}