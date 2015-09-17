package com.sealionsoftware.bali.compiler.execution;

import com.sealionsoftware.bali.compiler.ClasspathLoader;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static com.sealionsoftware.Constant.immutableMap;
import static com.sealionsoftware.Constant.put;
import static com.sealionsoftware.bali.compiler.Interpreter.FRAGMENT_CLASS_NAME;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ByteArrayClassLoaderTest {

    private ClasspathLoader loader = new ClasspathLoader();
    private ClassLoader subject;

    @Before
    public void setUp() throws Exception {
        URL file = getClass().getResource(FRAGMENT_CLASS_NAME + ".class");
        subject = new ByteArrayClassLoader(ByteArrayClassLoaderTest.class.getClassLoader(), immutableMap(
                put(FRAGMENT_CLASS_NAME, loader.resourceAsBytes(file))
        ));
    }

    @Test
    public void testDefineFromByteArray() throws Exception {

        Class clazz = subject.loadClass(FRAGMENT_CLASS_NAME);

        assertThat(clazz, notNullValue());
        assertThat(clazz.getName(), equalTo(FRAGMENT_CLASS_NAME));
    }

    @Test
    public void testDefineFromDelegate() throws Exception {

        Class clazz = subject.loadClass(Object.class.getName());

        assertThat(clazz, notNullValue());
        assertThat(clazz.getName(), equalTo(Object.class.getName()));
    }

}