package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MethodTest {

    private String name = "aMethod";
    private Site returnType = mock(Site.class);
    private Site parameterType = mock(Site.class);
    private List<Parameter> parameters = asList(
            new Parameter("aParameter", parameterType),
            new Parameter("anotherParameter", parameterType)
    );
    private Method subject = new Method(name, returnType, parameters);

    @Test
    public void testGetReturnType() throws Exception {

        assertThat(subject.getReturnType(), is(returnType));
    }

    @Test
    public void testGetName() throws Exception {

        assertThat(subject.getName(), equalTo(name));
    }

    @Test
    public void testGetParameters() throws Exception {

        assertThat(subject.getParameters(), equalTo(parameters));
    }

    @Test
    public void testGetTemplateMethod() throws Exception {

        assertThat(subject.getTemplateMethod(), nullValue());
    }

    @Test
    public void testToString() throws Exception {

        when(parameterType.toString()).thenReturn("T");
        assertThat(subject.toString(), equalTo("aMethod(T aParameter, T anotherParameter)"));
    }
}