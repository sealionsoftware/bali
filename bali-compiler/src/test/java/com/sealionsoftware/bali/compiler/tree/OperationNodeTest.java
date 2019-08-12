package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;

public class OperationNodeTest {

    private OperationNode subject = new OperationNode(2, 3, mock(CompilationThreadManager.class));

    @Test
    public void testGetOperatorName(){
        String name = "anOperation";
        subject.setOperatorName(name);
        assertThat(subject.getOperatorName(), equalTo(name));
    }

    @Test
    public void testAccept() throws Exception {
        Visitor visitor = mock(Visitor.class);
        subject.accept(visitor);
        verify(visitor).visit(same(subject));
    }

    @Test
    public void testToString(){

        ExpressionNode target = mock(ExpressionNode.class);
        when(target.toString()).thenReturn("foo");
        ExpressionNode argument = mock(ExpressionNode.class);
        when(argument.toString()).thenReturn("bar");


        subject.setTarget(target);
        subject.setArguments(asList(argument));
        subject.setOperatorName("%");

        assertThat(subject.toString(), equalTo("foo % bar"));
    }
}