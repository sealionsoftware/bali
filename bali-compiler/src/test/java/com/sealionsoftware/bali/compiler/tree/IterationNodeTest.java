package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.assembly.VariableData;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class IterationNodeTest {

    private CompilationThreadManager monitor = mock(CompilationThreadManager.class);
    private IterationNode subject = new IterationNode(2, 3, monitor);

    @Test
    public void testAccept() throws Exception {
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(same(subject));
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
    public void testGetIdentifier() throws Exception {
        String identifier = "aName";
        subject.setIdentifier(identifier);
        assertThat(subject.getIdentifier(), is(identifier));
    }

    @Test
    public void testGetTarget() throws Exception {
        ExpressionNode target = mock(ExpressionNode.class);
        subject.setTarget(target);
        assertThat(subject.getTarget(), is(target));
    }

    @Test
    public void testGetStatement() throws Exception {
        StatementNode statement = mock(StatementNode.class);
        subject.setStatement(statement);
        assertThat(subject.getStatement(), is(statement));
    }

    @Test
    public void testGetItemData() throws Exception {
        VariableData data = mock(VariableData.class);
        subject.setItemData(data);
        assertThat(subject.getItemData(), is(data));
    }


}