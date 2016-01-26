package com.sealionsoftware.bali.compiler.execution;

import com.sealionsoftware.bali.compiler.ClasspathLoader;
import com.sealionsoftware.bali.compiler.Executor;
import com.sealionsoftware.bali.compiler.GeneratedClass;
import com.sealionsoftware.bali.compiler.GeneratedPackage;
import org.junit.Test;

import java.util.Map;

import static com.sealionsoftware.Matchers.isEmptyMap;
import static com.sealionsoftware.bali.compiler.Interpreter.EVALUATION_CLASS_NAME;
import static com.sealionsoftware.bali.compiler.Interpreter.FRAGMENT_CLASS_NAME;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReflectiveExecutorTest {

    private Executor subject = new ReflectiveExecutor();
    private ClasspathLoader loader = new ClasspathLoader();

    @Test
    public void testExecuteFragment() throws Exception {

        GeneratedClass generatedClass = mock(GeneratedClass.class);
        GeneratedPackage generatedPackage = mock(GeneratedPackage.class);
        when(generatedPackage.getName()).thenReturn("");
        when(generatedPackage.getClasses()).thenReturn(singletonList(generatedClass));
        when(generatedClass.getName()).thenReturn(FRAGMENT_CLASS_NAME);
        when(generatedClass.getCode()).thenReturn(
                loader.resourceAsBytes(getClass().getResource(FRAGMENT_CLASS_NAME + ".class"))
        );

        Map<String, Object> ret = subject.executeFragment(generatedPackage);

        assertThat(ret, notNullValue());
        assertThat(ret, isEmptyMap());
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteClassNotFound() throws Exception {

        GeneratedClass generatedClass = mock(GeneratedClass.class);
        GeneratedPackage generatedPackage = mock(GeneratedPackage.class);
        when(generatedPackage.getName()).thenReturn("com.sealionsoftware");
        when(generatedPackage.getClasses()).thenReturn(singletonList(generatedClass));
        when(generatedClass.getName()).thenReturn(FRAGMENT_CLASS_NAME);
        when(generatedClass.getCode()).thenReturn(new byte[0]);

        subject.executeFragment(generatedPackage);
    }

    @Test
    public void testExecuteEvaluation() throws Exception {

        GeneratedClass generatedClass = mock(GeneratedClass.class);
        GeneratedPackage generatedPackage = mock(GeneratedPackage.class);
        when(generatedPackage.getName()).thenReturn("");
        when(generatedPackage.getClasses()).thenReturn(singletonList(generatedClass));
        when(generatedClass.getName()).thenReturn(EVALUATION_CLASS_NAME);
        when(generatedClass.getCode()).thenReturn(
                loader.resourceAsBytes(getClass().getResource(EVALUATION_CLASS_NAME + ".class"))
        );

        Object ret = subject.executeExpression(generatedPackage);

        assertThat(ret, notNullValue());
    }


}