package com.sealionsoftware.bali.compiler.tree;

import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

}