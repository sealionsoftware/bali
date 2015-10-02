package com.sealionsoftware.bali.compiler.assembly;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class InterpreterAssemblySetFactoryTest {

    private InterpreterAssemblySetFactory subject = new InterpreterAssemblySetFactory();

    @Test
    @SuppressWarnings("unchecked")
    public void testAssemblers() throws Exception {
        assertThat(subject.assemblers(), contains(
                instanceOf(TypeAssigningVisitor.class),
                instanceOf(TypeCheckVisitor.class)
        ));
    }
}