package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BooleanLiteralNodeTest {

    private BooleanLiteralNode subject = new BooleanLiteralNode(2, 3, mock(CompilationThreadManager.class));

    @Test
    public void testSetValue() throws Exception {
        subject.setValue(true);
        assertThat(subject.isTrue(), equalTo(true));
    }

    @Test
    public void testAccept() throws Exception {
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(same(subject));
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
        assertThat(children, is(empty()));
    }

    @Test
    public void testGetType() throws Exception {
        Type type = mock(Type.class);
        subject.setType(type);
        assertThat(subject.getType(), is(type));
    }
}