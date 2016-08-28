package com.sealionsoftware.bali.compiler.type;

import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Operator;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.Type;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeVariableTest {

    private Type bound = mock(Type.class);
    private TypeVariable subject = new TypeVariable("T", bound);

    @Test
    public void testIsAssignableToSelf() throws Exception {
        assertThat(subject.isAssignableTo(subject), is(true));
    }

    @Test
    public void testIsAssignableToTypeSatisfyingBound() throws Exception {

        Type other = mock(Type.class);
        when(bound.isAssignableTo(other)).thenReturn(true);

        assertThat(subject.isAssignableTo(other), is(true));
    }

    @Test
    public void testIsAssignableToTypeVariable() throws Exception {

        TypeVariable other = mock(TypeVariable.class);
        when(other.getName()).thenReturn("T");

        assertThat(subject.isAssignableTo(other), is(true));
    }

    @Test
    public void testIsNotAssignableToDifferentClassBasedType() throws Exception {

        Type other = mock(Type.class);
        when(other.getClassName()).thenReturn("com.sealionsoftware.OtherType");

        assertThat(subject.isAssignableTo(other), is(false));
    }

    @Test
    public void testGetClassName() throws Exception {

        String boundClassName = "com.sealionsoftware.LowerBound";
        when(bound.getClassName()).thenReturn(boundClassName);

        assertThat(subject.getClassName(), equalTo(boundClassName));
    }

    @Test
    public void testGetTemplate() throws Exception {

        Class template = mock(Class.class);
        when(bound.getTemplate()).thenReturn(template);

        assertThat(subject.getTemplate(), is(template));
    }

    @Test
    public void testGetSuperType() throws Exception {

        Type superType = mock(Type.class);
        when(bound.getSuperType()).thenReturn(superType);

        assertThat(subject.getSuperType(), is(superType));
    }

    @Test
    public void testGetInterfaces() throws Exception {

        Type interfaceType = mock(Type.class);
        when(bound.getInterfaces()).thenReturn(asList(interfaceType));

        assertThat(subject.getInterfaces(), contains(interfaceType));
    }

    @Test
    public void testGetTypeArguments() throws Exception {

        Site typeArgument = mock(Site.class);
        Parameter parameter = new Parameter("aTypeArgument", typeArgument);
        when(bound.getTypeArguments()).thenReturn(asList(parameter));

        assertThat(subject.getTypeArguments(), contains(parameter));
    }

    @Test
    public void testGetMethods() throws Exception {

        Method method = mock(Method.class);
        when(bound.getMethods()).thenReturn(asList(method));

        assertThat(subject.getMethods(), contains(method));
    }

    @Test
    public void testGetMethod() throws Exception {

        Method method = mock(Method.class);
        when(bound.getMethod("aMethod")).thenReturn(method);

        assertThat(subject.getMethod("aMethod"), is(method));
    }

    @Test
    public void testGetOperators() throws Exception {

        Operator operator = mock(Operator.class);
        when(bound.getOperators()).thenReturn(asList(operator));

        assertThat(subject.getOperators(), contains(operator));
    }

    @Test
    public void testGetOperator() throws Exception {

        Operator operator = mock(Operator.class);
        when(bound.getOperator("+")).thenReturn(operator);

        assertThat(subject.getOperator("+"), is(operator));
    }

    @Test
    public void testGetUnaryOperators() throws Exception {

        Operator operator = mock(Operator.class);
        when(bound.getUnaryOperators()).thenReturn(asList(operator));

        assertThat(subject.getUnaryOperators(), contains(operator));
    }

    @Test
    public void testGetUnaryOperator() throws Exception {

        Operator operator = mock(Operator.class);
        when(bound.getUnaryOperator("+")).thenReturn(operator);

        assertThat(subject.getUnaryOperator("+"), is(operator));
    }

    @Test
    public void testGetName() throws Exception {

        assertThat(subject.getName(), equalTo("T"));
    }

    @Test
    public void testToString() throws Exception {

        assertThat(subject, hasToString("T"));
    }
}