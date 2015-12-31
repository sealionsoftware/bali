package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ClasspathClassFactoryTest {

    private Map<String, Class> library = new HashMap<>();
    private ClasspathClassFactory subject = new ClasspathClassFactory(library);

    @Test
    public void testAddTrivialClassToLibrary(){

        class A {}
        java.lang.Class<A> a = A.class;

        subject.addToLibrary(a);

        Class constructed = library.get(a.getName());

        assertThat(constructed, notNullValue());
        assertThat(constructed.toString(), equalTo(a.getName()));
        assertThat(constructed.getSuperType(), nullValue());
    }

    @Test
    public void testAddExtendedClassToLibrary(){

        class B {}
        class A extends B {}
        java.lang.Class<A> a = A.class;

        subject.addToLibrary(a);

        Class constructed = library.get(a.getName());

        assertThat(constructed, notNullValue());

        Type superType = constructed.getSuperType();
        assertThat(superType, notNullValue());
        assertThat(superType.toString(), equalTo(B.class.getName()));
    }

    @Test
    public void testAddExtendedGenericClassToLibrary(){

        class D{ }
        class C extends D{ }
        class B<T extends D> {}
        class A extends B<C> {}
        java.lang.Class<A> a = A.class;

        subject.addToLibrary(a);

        Class constructed = library.get(a.getName());

        assertThat(constructed, notNullValue());

        Type superType = constructed.getSuperType();
        assertThat(superType.toString(), hasToString(B.class.getName() + "<" + C.class.getName() + ">"));
    }

    @Test
    public void testAddInterfacedClassToLibrary(){

        class A implements IA {}
        java.lang.Class<A> a = A.class;

        subject.addToLibrary(a);

        Class constructed = library.get(a.getName());

        assertThat(constructed, notNullValue());
        assertThat(constructed.getInterfaces(), hasItem(hasToString(IA.class.getName())));
    }

    @Test
    public void testAddExtendedInterfacedClassToLibrary(){

        class A implements IB {}
        java.lang.Class<A> a = A.class;

        subject.addToLibrary(a);

        Class constructed = library.get(a.getName());

        assertThat(constructed, notNullValue());
        assertThat(constructed.getInterfaces(), hasItem(hasToString(IB.class.getName())));
    }

    @Test
    public void testAddGenericInterfacedClassToLibrary(){
        class B extends IBBT{}
        class A implements IC<B> {}
        java.lang.Class<A> a = A.class;

        subject.addToLibrary(a);

        Class constructed = library.get(a.getName());

        assertThat(constructed, notNullValue());
        assertThat(constructed.getInterfaces(), hasItem(hasToString(IC.class.getName() + "<" + B.class.getName() + ">")));
    }

    @Test
    public void testAddGenericClassToLibrary(){

        class B {}
        class A<T extends B> {}
        java.lang.Class<A> a = A.class;

        subject.addToLibrary(a);

        Class constructed = library.get(a.getName());

        assertThat(constructed, notNullValue());
        assertThat(constructed.getTypeParameters(), hasItem(hasToString(B.class.getName() + " T")));
    }

    @Test(expected = RuntimeException.class)
    public void testAddUnsupportedTypeToLibrary(){

        class B<V> {}
        class A<T extends B<?>> {}
        java.lang.Class<A> a = A.class;

        subject.addToLibrary(a);
    }

    interface IA {}
    interface IB extends IBA {}
    interface IBA {}
    interface IC<T extends IBBT> {}

    class IBBT{}

}