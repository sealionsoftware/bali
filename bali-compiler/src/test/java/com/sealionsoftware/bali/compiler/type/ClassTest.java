package com.sealionsoftware.bali.compiler.type;

import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Operator;
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
    public void testGetTypeParametersNotInitialised() {
        subject.getTypeParameters();
    }

    @Test
    public void testGetTypeParameters() {
        Parameter typeParameter = mock(Parameter.class);
        subject.initialise(asList(typeParameter), null, emptyList(), emptyList(), emptyList());
        assertThat(subject.getTypeParameters(), hasItem(is(typeParameter)));
    }

    @Test(expected = RuntimeException.class)
    public void testGetSuperTypeNotInitialised() {
        subject.getSuperType();
    }

    @Test
    public void testGetSuperType() {
        Type type = mock(Type.class);
        subject.initialise(emptyList(), type, emptyList(), emptyList(), emptyList());
        assertThat(subject.getSuperType(), is(type));
    }

    @Test(expected = RuntimeException.class)
    public void testGetInterfacesNotInitialised() {
        subject.getInterfaces();
    }

    @Test
    public void testGetInterfaces() {
        Type iface = mock(Type.class);
        subject.initialise(emptyList(), null, asList(iface), emptyList(), emptyList());
        assertThat(subject.getInterfaces(), hasItem(is(iface)));
    }

    @Test
    public void testGetMethods() {
        Type returnType = mock(Type.class);
        Parameter parameter = mock(Parameter.class);
        Method method = new Method("aMethod", returnType, asList(parameter));
        subject.initialise(emptyList(), null, emptyList(), asList(method), emptyList());
        assertThat(subject.getMethods(), hasItem(is(method)));
    }

    @Test
    public void testGetOperators() {
        Type returnType = mock(Type.class);
        Parameter parameter = mock(Parameter.class);
        Operator operator = new Operator("aMethod", returnType, asList(parameter), "+");
        subject.initialise(emptyList(), null, emptyList(), emptyList(), asList(operator));
        assertThat(subject.getOperators(), hasItem(is(operator)));
    }

    @Test
    public void testGetClassName() {
        assertThat(subject.getClassName(), equalTo("com.sealionsoftware.Test"));
    }

    @Test
    public void testToString() {
        assertThat(subject.toString(), containsString("com.sealionsoftware.Test"));
    }
}