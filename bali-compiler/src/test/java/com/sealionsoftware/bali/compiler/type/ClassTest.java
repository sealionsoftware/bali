package com.sealionsoftware.bali.compiler.type;

import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Type;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ClassTest {

    private Class subject = new Class("com.sealionsoftware.Test");

    @Test(expected = RuntimeException.class)
    public void testGetTypeParametersNotInitialised() throws Exception {
        subject.getTypeParameters();
    }

    @Test
    public void testGetTypeParameters() throws Exception {
        Parameter typeParameter = mock(Parameter.class);
        subject.initialise(asList(typeParameter), null, emptyList(), emptyList());
        assertThat(subject.getTypeParameters(), hasItem(is(typeParameter)));
    }

    @Test(expected = RuntimeException.class)
    public void testGetSuperTypeNotInitialised() throws Exception {
        subject.getSuperType();
    }

    @Test
    public void testGetSuperType() throws Exception {
        Type type = mock(Type.class);
        subject.initialise(emptyList(), type, emptyList(), emptyList());
        assertThat(subject.getSuperType(), is(type));
    }

    @Test(expected = RuntimeException.class)
    public void testGetInterfacesNotInitialised() throws Exception {
        subject.getInterfaces();
    }

    @Test
    public void testGetInterfaces() throws Exception {
        Type iface = mock(Type.class);
        subject.initialise(emptyList(), null, asList(iface), emptyList());
        assertThat(subject.getInterfaces(), hasItem(is(iface)));
    }

    @Test
    public void testGetMethods() throws Exception {
        Type returnType = mock(Type.class);
        Parameter parameter = mock(Parameter.class);
        Method method = new Method("aMethod", returnType, asList(parameter));
        subject.initialise(emptyList(), null, emptyList(), asList(method));
        assertThat(subject.getMethods(), hasItem(is(method)));
    }

    @Test
    public void testGetClassName() throws Exception {
        assertThat(subject.getClassName(), equalTo("com.sealionsoftware.Test"));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(subject.toString(), containsString("com.sealionsoftware.Test"));
    }
}