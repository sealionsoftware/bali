package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.Type;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import org.hamcrest.core.Is;
import org.junit.Test;

import static com.sealionsoftware.bali.compiler.Matchers.isSiteOfType;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ExistenceCheckNodeTest {

    private ExistenceCheckNode subject = new ExistenceCheckNode(2, 3, mock(CompilationThreadManager.class));

    @Test
    public void testGetTarget() throws Exception {
        ExpressionNode target = mock(ExpressionNode.class);
        subject.setTarget(target);
        assertThat(subject.getTarget(), is(target));
    }

    @Test
    public void testGetSite() throws Exception {
        Type type = mock(Type.class);
        subject.setType(type);
        assertThat(subject.getSite(), isSiteOfType(type));
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
        assertThat(subject.getChildren(), Is.is(empty()));
    }

    @Test
    public void testAccept() throws Exception {
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(same(subject));
    }

}