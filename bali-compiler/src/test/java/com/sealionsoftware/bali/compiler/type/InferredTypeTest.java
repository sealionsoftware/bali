package com.sealionsoftware.bali.compiler.type;

import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Operator;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Type;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InferredTypeTest {

    private Type bound = mock(Type.class);
    private Type inferral = mock(Type.class);
    private InferredType subject = new InferredType(bound);

    @Before
    public void setUp(){
        subject.isAssignableTo(inferral);
    }

    @Test
    public void testAcquireOnIsAssignable(){
        verify(bound).isAssignableTo(inferral);
    }

    @Test
    public void testGetClassNameFromInferral(){
        when(inferral.getClassName()).thenReturn("AClass");
        assertThat(subject.getClassName(), equalTo("AClass"));
    }

    @Test
    public void testGetInterfacesFromInferral(){
        Type iface = mock(Type.class);
        when(inferral.getInterfaces()).thenReturn(asList(iface));
        assertThat(subject.getInterfaces(), contains(iface));
    }

    @Test
    public void testGetSuperTypeFromInferral(){
        Type superType = mock(Type.class);
        when(inferral.getSuperType()).thenReturn(superType);
        assertThat(subject.getSuperType(), equalTo(superType));
    }

    @Test
    public void testGetTemplateFromInferral(){
        Class template = mock(Class.class);
        when(inferral.getTemplate()).thenReturn(template);
        assertThat(subject.getTemplate(), equalTo(template));
    }

    @Test
    public void testGetTypeArgumentsFromInferral(){
        Parameter typeArgument = mock(Parameter.class);
        when(inferral.getTypeArguments()).thenReturn(asList(typeArgument));
        assertThat(subject.getTypeArguments(), contains(typeArgument));
    }

    @Test
    public void testGetMethodsFromInferral(){
        Method method = mock(Method.class);
        when(inferral.getMethods()).thenReturn(asList(method));
        assertThat(subject.getMethods(), contains(method));
    }

    @Test
    public void testGetOperatorsFromInferral(){
        Operator operator = mock(Operator.class);
        when(inferral.getOperators()).thenReturn(asList(operator));
        assertThat(subject.getOperators(), contains(operator));
    }

    @Test
    public void testGetUnaryOperatorsFromInferral(){
        Operator operator = mock(Operator.class);
        when(inferral.getUnaryOperators()).thenReturn(asList(operator));
        assertThat(subject.getUnaryOperators(), contains(operator));
    }

    @Test
    public void testGetMethodFromInferral(){
        Method method = mock(Method.class);
        when(inferral.getMethod("AMethod")).thenReturn(method);
        assertThat(subject.getMethod("AMethod"), equalTo(method));
    }

    @Test
    public void testGetOperatorFromInferral(){
        Operator operator = mock(Operator.class);
        when(inferral.getOperator("AnOperator")).thenReturn(operator);
        assertThat(subject.getOperator("AnOperator"), equalTo(operator));
    }

    @Test
    public void testGetUnaryOperatorFromInferral(){
        Operator method = mock(Operator.class);
        when(inferral.getUnaryOperator("AnOperator")).thenReturn(method);
        assertThat(subject.getUnaryOperator("AnOperator"), equalTo(method));
    }

}