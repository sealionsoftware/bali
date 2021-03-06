package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.type.Class;
import com.sealionsoftware.bali.compiler.type.ClassBasedType;
import com.sealionsoftware.bali.compiler.type.TypeVariable;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClassBasedTypeTest {

    private Class template = mock(Class.class);
    private Type templateParameterType = mock(Type.class);
    private Parameter templateParameterOne = new Parameter("T", new Site(templateParameterType));
    private Parameter templateParameterTwo = new Parameter("U", new Site(templateParameterType));

    private Type templateSuperType = mock(Type.class);
    private Type templateInterfaceType = mock(Type.class);

    private Type typeArgumentType = mock(Type.class);
    private Method templateMethod = mock(Method.class);
    private Type templateMethodParameterType = mock(Type.class);

    private Operator templateOperator = mock(Operator.class);
    private Type templateOperatorParameterType = mock(Type.class);

    private Operator templateUnaryOperator = mock(Operator.class);

    private ClassBasedType subject = new ClassBasedType(template, asList(new Site(typeArgumentType), new Site(typeArgumentType)));

    @Before
    public void setUp(){
        when(template.getSuperType()).thenReturn(templateSuperType);
        when(template.getTypeParameters()).thenReturn(asList(templateParameterOne, templateParameterTwo));
        when(template.getClassName()).thenReturn("com.sealionsoftware.Test");
        when(template.getInterfaces()).thenReturn(asList(templateInterfaceType));
        when(template.getMethods()).thenReturn(asList(templateMethod));
        when(template.getOperators()).thenReturn(asList(templateOperator));
        when(template.getUnaryOperators()).thenReturn(asList(templateUnaryOperator));

        when(templateSuperType.getClassName()).thenReturn("com.sealionsoftware.Super");
        when(templateInterfaceType.getClassName()).thenReturn("com.sealionsoftware.Interface");
        when(templateMethod.getName()).thenReturn("aMethod");
        when(templateMethod.getParameters()).thenReturn(asList(
                new Parameter("aParameter",  new Site(templateMethodParameterType)),
                new Parameter("anUnboundedParameter", new Site(new TypeVariable("T", null))))
        );

        when(templateOperator.getName()).thenReturn("anOperator");
        when(templateOperator.getSymbol()).thenReturn("+");
        when(templateOperator.getParameters()).thenReturn(asList(
                new Parameter("aParameter", new Site(templateOperatorParameterType))
        ));

        when(templateUnaryOperator.getName()).thenReturn("anUnaryOperator");
        when(templateUnaryOperator.getSymbol()).thenReturn("+");
        when(templateUnaryOperator.getParameters()).thenReturn(emptyList());

        when(typeArgumentType.toString()).thenReturn("com.sealionsoftware.TypeArgument");
    }

    @Test
    public void testConvenienceConstructor() throws Exception {
        subject = new ClassBasedType(template);
        assertThat(subject.getTemplate(), is(template));
    }

    @Test
    public void testGetClassName() throws Exception {
        assertThat(subject.getClassName(), equalTo("com.sealionsoftware.Test"));
    }

    @Test
    public void testGetTemplate() throws Exception {
        assertThat(subject.getTemplate(), is(template));
    }

    @Test
    public void testGetSuperType() throws Exception {
        Type superType = subject.getSuperType();
        assertThat(superType, notNullValue());
        assertThat(superType.getClassName(), equalTo("com.sealionsoftware.Super"));
    }

    @Test
    public void testGetParameterisedSuperType() throws Exception {

        Type templateSuperTypeTypeArgument = new TypeVariable("T", null);
        when(templateSuperType.getTypeArguments()).thenReturn(asList(new Parameter("X", new Site(templateSuperTypeTypeArgument))));

        Class templateSuperTypeTemplate = mock(Class.class);
        when(templateSuperType.getTemplate()).thenReturn(templateSuperTypeTemplate);
        when(templateSuperTypeTemplate.getTypeParameters()).thenReturn(asList(new Parameter("X", null)));

        Type superType = subject.getSuperType();
        assertThat(superType, notNullValue());
        assertThat(superType.getTypeArguments(), hasItem(hasToString("com.sealionsoftware.TypeArgument X")));
    }

    @Test
    public void testGetInterface() throws Exception {

        List<Type> interfaces = subject.getInterfaces();

        assertThat(interfaces, hasItem(notNullValue(Type.class)));
        assertThat(interfaces.get(0).getClassName(), equalTo("com.sealionsoftware.Interface"));
    }

    @Test
    public void testGetParameterisedInterfaces() throws Exception {

        Type templateInterfaceTypeTypeArgument = new TypeVariable("T", null);
        when(templateInterfaceType.getTypeArguments()).thenReturn(asList(new Parameter("X", new Site(templateInterfaceTypeTypeArgument))));

        Class templateInterfaceTypeTemplate = mock(Class.class);
        when(templateInterfaceType.getTemplate()).thenReturn(templateInterfaceTypeTemplate);
        when(templateInterfaceTypeTemplate.getTypeParameters()).thenReturn(asList(new Parameter("X", null)));

        List<Type> interfaces = subject.getInterfaces();
        assertThat(interfaces, hasItem(notNullValue(Type.class)));
        assertThat(interfaces.get(0).getTypeArguments(), hasItem(hasToString("com.sealionsoftware.TypeArgument X")));
    }

    @Test
    public void testIsAssignableToNull() throws Exception {
        assertThat(subject.isAssignableTo(null), is(true));
    }

    @Test
    public void testIsAssignableToSelf() throws Exception {
        assertThat(subject.isAssignableTo(subject), is(true));
    }

    @Test
    public void testIsAssignableToSameType() throws Exception {

        Type other = mock(Type.class);
        Type otherArgumentType = mock(Type.class);

        when(other.getClassName()).thenReturn("com.sealionsoftware.Test");
        when(other.getTypeArguments()).thenReturn(asList(new Parameter("T", new Site(otherArgumentType))));
        when(typeArgumentType.isAssignableTo(otherArgumentType)).thenReturn(true);

        assertThat(subject.isAssignableTo(other), is(true));
    }

    @Test
    public void testIsNotAssignableWhenArgumentsDoNotMatch() throws Exception {

        Type other = mock(Type.class);
        Type otherArgumentType = mock(Type.class);
        Parameter otherArgument = new Parameter("T", new Site(otherArgumentType));
        when(other.getClassName()).thenReturn("com.sealionsoftware.Test");
        when(other.getTypeArguments()).thenReturn(asList(otherArgument));
        when(typeArgumentType.isAssignableTo(otherArgumentType)).thenReturn(false);

        assertThat(subject.isAssignableTo(other), is(false));
    }

    @Test
    public void testIsAssignableToSuperType() throws Exception {

        Type other = mock(Type.class);

        when(other.getClassName()).thenReturn("com.sealionsoftware.Super");
        when(templateSuperType.isAssignableTo(other)).thenReturn(true);

        assertThat(subject.isAssignableTo(other), is(true));
    }

    @Test
    public void testIsAssignableToInterface() throws Exception {

        Type other = mock(Type.class);

        when(other.getClassName()).thenReturn("com.sealionsoftware.Interface");
        when(templateInterfaceType.isAssignableTo(other)).thenReturn(true);

        assertThat(subject.isAssignableTo(other), is(true));
    }

    @Test
    public void testNotEqualToUnrelatedType() throws Exception {

        Type other = mock(Type.class);
        when(other.getClassName()).thenReturn("com.sealionsoftware.Unrelated");

        assertThat(subject.isAssignableTo(other), is(false));
    }

    @Test
    public void testToString() throws Exception {

        assertThat(subject.toString(), equalTo("com.sealionsoftware.Test<com.sealionsoftware.TypeArgument, com.sealionsoftware.TypeArgument>"));
    }

    @Test
    public void testGetMethod() throws Exception {

        Method typeMethod = subject.getMethod("aMethod");

        assertThat(typeMethod, notNullValue());
        assertThat(typeMethod.getName(), equalTo("aMethod"));
        assertThat(typeMethod.getReturnType(), nullValue());
        assertThat(typeMethod.getParameters(), hasSize(2));

        Iterator<Parameter> parameters = typeMethod.getParameters().iterator();
        Parameter one = parameters.next();
        assertThat(one.name, equalTo("aParameter"));
        assertThat(one.site.type, is(templateMethodParameterType));

        Parameter two = parameters.next();
        assertThat(two.name, equalTo("anUnboundedParameter"));
        assertThat(two.site.type, is(typeArgumentType));

    }

    @Test
    public void testGetMethods() throws Exception {

        List<Method> methods = subject.getMethods();

        assertThat(methods, hasSize(1));
    }

    @Test
    public void testGetOperator() throws Exception {

        Operator typeOperator = subject.getOperator("+");

        assertThat(typeOperator, notNullValue());
        assertThat(typeOperator.getName(), equalTo("anOperator"));
        assertThat(typeOperator.getReturnType(), nullValue());
        assertThat(typeOperator.getParameters(), hasSize(1));

        Iterator<Parameter> parameters = typeOperator.getParameters().iterator();
        Parameter one = parameters.next();
        assertThat(one.name, equalTo("aParameter"));
        assertThat(one.site.type, is(templateOperatorParameterType));

    }

    @Test
    public void testGetOperators() throws Exception {

        List<Operator> operators = subject.getOperators();

        assertThat(operators, hasSize(1));
    }

    @Test
    public void testGetUnaryOperator() throws Exception {

        Operator typeOperator = subject.getUnaryOperator("+");

        assertThat(typeOperator, notNullValue());
        assertThat(typeOperator.getName(), equalTo("anUnaryOperator"));
        assertThat(typeOperator.getReturnType(), nullValue());
        assertThat(typeOperator.getParameters(), empty());
    }

    @Test
    public void testGetUnaryOperators() throws Exception {

        List<Operator> operators = subject.getUnaryOperators();

        assertThat(operators, hasSize(1));
    }

}