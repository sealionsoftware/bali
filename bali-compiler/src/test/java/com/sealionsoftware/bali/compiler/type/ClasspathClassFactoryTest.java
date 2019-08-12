package com.sealionsoftware.bali.compiler.type;

import com.sealionsoftware.bali.compiler.Type;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClasspathClassFactoryTest {

    private ClassLoader loader = Thread.currentThread().getContextClassLoader();
    private Map<String, Class> library = new HashMap<>();
    private ClasspathClassFactory subject = new ClasspathClassFactory(library, loader);

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
        assertThat(superType, hasToString(B.class.getName() + "<" + C.class.getName() + ">"));
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
    public void testAddUnsupportedClassWithUnboundedTypeToLibrary(){

        class B<V> {}
        class A<T extends B<?>> {}
        java.lang.Class<A> a = A.class;

        subject.addToLibrary(a);
    }

    @Test(expected = RuntimeException.class)
    public void testAddUnsupportedClassWithContravariantTypeToLibrary(){

        class B<V> {}
        class C{}

        class A<T extends B<? extends C>> {}
        java.lang.Class<A> a = A.class;

        subject.addToLibrary(a);
    }

    @Test(expected = RuntimeException.class)
    public void testAddNotFoundClassToLibrary(){

        class A {}
        java.lang.Class<A> a = A.class;

        ClassLoader loader = mock(ClassLoader.class);
        when(loader.getResourceAsStream(anyString())).thenReturn(null);

        subject = new ClasspathClassFactory(library, loader);

        subject.addToLibrary(a);
    }

    interface IA {}
    interface IB extends IBA {}
    interface IBA {}
    interface IC<T extends IBBT> {}

    class IBBT{}

}