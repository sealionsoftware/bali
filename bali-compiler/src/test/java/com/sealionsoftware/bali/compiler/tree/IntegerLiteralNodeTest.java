package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import org.junit.Test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class IntegerLiteralNodeTest {

    private IntegerLiteralNode subject = new IntegerLiteralNode(2, 3, mock(CompilationThreadManager.class));

    @Test
    public void testSetValue() throws Exception {
        subject.setValue(5);
        assertThat(subject.getValue(), equalTo(5));
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
        assertThat(subject.getChildren(), is(empty()));
    }

    @Test
    public void testAccept() throws Exception {
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(same(subject));
    }

    @Test
    public void testGetSite() throws Exception {
        Site type = mock(Site.class);
        subject.setSite(type);
        assertThat(subject.getSite(), is(type));
    }
}