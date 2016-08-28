package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.tree.TypeNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.junit.Test;

import static com.sealionsoftware.bali.compiler.Matchers.containsNoFailures;
import static com.sealionsoftware.bali.compiler.Matchers.containsOneFailure;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequiredVariableVisitorTest {

    private RequiredVariableVisitor subject = new RequiredVariableVisitor();

    @Test
    public void testVisitRequiredVariableWithNoValue(){

        TypeNode typeNode = mock(TypeNode.class);
        when(typeNode.getOptional()).thenReturn(false);

        VariableNode node = new VariableNode(2, 3);
        node.setName("aName");
        node.setType(typeNode);

        subject.visit(node);

        assertThat(subject, containsOneFailure(ErrorCode.VALUE_REQUIRED));
    }

    @Test
    public void testVisitOptionalVariableWithNoValue(){

        TypeNode typeNode = mock(TypeNode.class);
        when(typeNode.getOptional()).thenReturn(true);

        VariableNode node = new VariableNode(2, 3);
        node.setName("aName");
        node.setType(typeNode);

        assertThat(subject, containsNoFailures());
    }

}