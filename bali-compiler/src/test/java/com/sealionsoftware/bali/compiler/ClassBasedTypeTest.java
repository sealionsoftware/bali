package com.sealionsoftware.bali.compiler;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.sealionsoftware.Matchers.containsOneValue;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClassBasedTypeTest {

    private Type argumentType = mock(Type.class);
    private Parameter argument = new Parameter("T", argumentType);
    private Class template = mock(Class.class);
    private ClassBasedType subject = new ClassBasedType(template, asList(argument));

    @Before
    public void setUp(){
        when(template.getClassName()).thenReturn("com.sealionsoftware.Test");
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

        Type classSuperType = mock(Type.class);

        when(template.getSuperType()).thenReturn(classSuperType);
        when(classSuperType.getClassName()).thenReturn("com.sealionsoftware.Super");

        Type superType = subject.getSuperType();
        assertThat(superType, notNullValue());
        assertThat(superType.getClassName(), equalTo("com.sealionsoftware.Super"));
    }

    @Test
    public void testGetParameterisedSuperType() throws Exception {

        Type classTypeParameterType = mock(Type.class);
        Parameter classTypeParameter = new Parameter("T", classTypeParameterType);

        Type classSuperType = mock(Type.class);
        Type classSuperTypeTypeArgumentType = mock(Type.class);
        Parameter classSuperTypeTypeArgument = new Parameter("T", classSuperTypeTypeArgumentType);

        when(template.getSuperType()).thenReturn(classSuperType);
        when(classSuperType.getTypeArguments()).thenReturn(asList(classSuperTypeTypeArgument));
        when(template.getTypeParameters()).thenReturn(asList(classTypeParameter));
        when(argumentType.toString()).thenReturn("C");

        Type superType = subject.getSuperType();
        assertThat(superType, notNullValue());
        assertThat(superType.getTypeArguments(), containsOneValue(hasToString("C T")));
    }

    @Test
    public void testGetInterface() throws Exception {

        Type classInterfaceType = mock(Type.class);

        when(template.getInterfaces()).thenReturn(asList(classInterfaceType));
        when(classInterfaceType.getClassName()).thenReturn("com.sealionsoftware.Interface");

        List<Type> interfaces = subject.getInterfaces();

        assertThat(interfaces, containsOneValue(notNullValue(Type.class)));
        assertThat(interfaces.get(0).getClassName(), equalTo("com.sealionsoftware.Interface"));
    }

    @Test
    public void testGetParameterisedInterfaces() throws Exception {
        Type classTypeParameterType = mock(Type.class);
        Parameter classTypeParameter = new Parameter("T", classTypeParameterType);

        Type classInterfaceType = mock(Type.class);
        Type classInterfaceTypeArgumentType = mock(Type.class);
        Parameter classInterfaceTypeArgument = new Parameter("T", classInterfaceTypeArgumentType);

        when(template.getInterfaces()).thenReturn(asList(classInterfaceType));
        when(classInterfaceType.getTypeArguments()).thenReturn(asList(classInterfaceTypeArgument));
        when(template.getTypeParameters()).thenReturn(asList(classTypeParameter));
        when(argumentType.toString()).thenReturn("C");

        List<Type> interfaces = subject.getInterfaces();
        assertThat(interfaces, containsOneValue(notNullValue(Type.class)));
        assertThat(interfaces.get(0).getTypeArguments(), containsOneValue(hasToString("C T")));
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
        when(other.getClassName()).thenReturn("com.sealionsoftware.Test");
        when(other.getTypeArguments()).thenReturn(asList(argument));
        when(argumentType.isAssignableTo(argumentType)).thenReturn(true);

        assertThat(subject.isAssignableTo(other), is(true));
    }

    @Test
    public void testIsNotAssignableWhenArgumentsDoNotMatch() throws Exception {

        Type other = mock(Type.class);
        Type otherArgumentType = mock(Type.class);
        Parameter otherArgument = new Parameter("T", otherArgumentType);
        when(other.getClassName()).thenReturn("com.sealionsoftware.Test");
        when(other.getTypeArguments()).thenReturn(asList(otherArgument));
        when(argumentType.isAssignableTo(otherArgumentType)).thenReturn(false);

        assertThat(subject.isAssignableTo(other), is(false));
    }

    @Test
    public void testIsAssignableToSuperType() throws Exception {

        Class superClass = mock(Class.class);
        when(superClass.getClassName()).thenReturn("com.sealionsoftware.Super");

        Type superType = mock(Type.class);
        when(superType.getTypeArguments()).thenReturn(asList(argument));
        when(argumentType.isAssignableTo(argumentType)).thenReturn(true);
        when(template.getSuperType()).thenReturn(superType);
        when(superType.getTemplate()).thenReturn(superClass);

        Type other = mock(Type.class);
        when(other.getClassName()).thenReturn("com.sealionsoftware.Super");
        when(other.getTypeArguments()).thenReturn(asList(argument));

        assertThat(subject.isAssignableTo(other), is(true));
    }

    @Test
    public void testIsAssignableToInterface() throws Exception {

        Class interfaceClass = mock(Class.class);
        when(interfaceClass.getClassName()).thenReturn("com.sealionsoftware.Interface");

        Type interfaceType = mock(Type.class);
        when(interfaceType.getTypeArguments()).thenReturn(asList(argument));
        when(argumentType.isAssignableTo(argumentType)).thenReturn(true);
        when(template.getInterfaces()).thenReturn(asList(interfaceType));
        when(interfaceType.getTemplate()).thenReturn(interfaceClass);

        Type other = mock(Type.class);
        when(other.getClassName()).thenReturn("com.sealionsoftware.Interface");
        when(other.getTypeArguments()).thenReturn(asList(argument));

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
        assertThat(subject.toString(), containsString("com.sealionsoftware.Test"));
    }

    @Test
    public void testMultiArgumentToString() throws Exception {
        subject = new ClassBasedType(template, asList(argument, argument));
        subject.toString();
    }

}