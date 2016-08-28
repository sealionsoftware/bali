package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class OperatorTest {

    private Site returnType = mock(Site.class);
    private Site parameterType = mock(Site.class);
    private Operator subject = new Operator("aMethod", returnType, asList(new Parameter(null, parameterType)), "+", null);

    @Test
    public void testGetSymbol() throws Exception {
        assertThat(subject.getSymbol(), equalTo("+"));
    }
}