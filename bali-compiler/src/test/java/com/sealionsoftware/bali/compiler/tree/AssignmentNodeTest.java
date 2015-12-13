package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import org.junit.Test;

import static com.sealionsoftware.Matchers.isEmpty;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AssignmentNodeTest {

    private CompilationThreadManager monitor = mock(CompilationThreadManager.class);
    private AssignmentNode subject = new AssignmentNode(2, 3);

    @Test
    public void testGetValue() throws Exception {
        ExpressionNode value = mock(ExpressionNode.class);
        subject.setValue(value);
        assertThat(subject.getValue(), equalTo(value));
    }

    @Test
    public void testGetReference() throws Exception {
        ReferenceNode referenceNode = mock(ReferenceNode.class);
        subject.setTarget(referenceNode);
        assertThat(subject.getTarget(), is(referenceNode));
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
        assertThat(subject.getChildren(), isEmpty());
    }

    @Test
    public void testAccept() throws Exception {
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(same(subject), isA(Control.class));
    }
}