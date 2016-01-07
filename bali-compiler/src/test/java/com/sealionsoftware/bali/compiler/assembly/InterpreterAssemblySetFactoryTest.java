package com.sealionsoftware.bali.compiler.assembly;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class InterpreterAssemblySetFactoryTest {

    private InterpreterAssemblySetFactory subject = new InterpreterAssemblySetFactory();

    @Test
    @SuppressWarnings("unchecked")
    public void testAssemblers() throws Exception {
        assertThat(subject.assemblers(), containsInAnyOrder(
                instanceOf(TypeAssigningVisitor.class),
                instanceOf(TypeCheckVisitor.class),
                instanceOf(ReferenceMatchingVisitor.class),
                instanceOf(InvocationMethodResolver.class)
        ));
    }
}