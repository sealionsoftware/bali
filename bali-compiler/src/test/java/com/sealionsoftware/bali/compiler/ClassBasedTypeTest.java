package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import java.util.List;

import static com.sealionsoftware.Matchers.containsOneValue;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClassBasedTypeTest {

    private Type argumentType = mock(Type.class);
    private Parameter argument = new Parameter("T", argumentType);
    private Class clazz = mock(Class.class);
    private ClassBasedType subject = new ClassBasedType(clazz, asList(argument));

    @Test
    public void testGetClassName() throws Exception {
        when(clazz.getClassName()).thenReturn("com.sealionsoftware.Test");
        assertThat(subject.getClassName(), equalTo("com.sealionsoftware.Test"));
    }

    @Test
    public void testGetSuperType() throws Exception {

        Type classSuperType = mock(Type.class);

        when(clazz.getSuperType()).thenReturn(classSuperType);
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

        when(clazz.getSuperType()).thenReturn(classSuperType);
        when(classSuperType.getTypeArguments()).thenReturn(asList(classSuperTypeTypeArgument));
        when(clazz.getTypeParameters()).thenReturn(asList(classTypeParameter));
        when(argumentType.toString()).thenReturn("C");

        Type superType = subject.getSuperType();
        assertThat(superType, notNullValue());
        assertThat(superType.getTypeArguments(), containsOneValue(hasToString("C T")));
    }

    @Test
    public void testGetInterface() throws Exception {

        Type classInterfaceType = mock(Type.class);

        when(clazz.getInterfaces()).thenReturn(asList(classInterfaceType));
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

        when(clazz.getInterfaces()).thenReturn(asList(classInterfaceType));
        when(classInterfaceType.getTypeArguments()).thenReturn(asList(classInterfaceTypeArgument));
        when(clazz.getTypeParameters()).thenReturn(asList(classTypeParameter));
        when(argumentType.toString()).thenReturn("C");

        List<Type> interfaces = subject.getInterfaces();
        assertThat(interfaces, containsOneValue(notNullValue(Type.class)));
        assertThat(interfaces.get(0).getTypeArguments(), containsOneValue(hasToString("C T")));
    }

//    @Test
//    public void testIsAssignableTo() throws Exception {
//
//        subject;
//
//    }
//
//    @Test
//    public void testToString() throws Exception {
//
//        subject;
//
//    }

}