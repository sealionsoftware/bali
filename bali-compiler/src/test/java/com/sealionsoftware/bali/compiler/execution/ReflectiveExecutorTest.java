package com.sealionsoftware.bali.compiler.execution;

import com.sealionsoftware.bali.compiler.Executor;
import com.sealionsoftware.bali.compiler.GeneratedClass;
import com.sealionsoftware.bali.compiler.GeneratedPackage;
import com.sealionsoftware.bali.compiler.Interpreter;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReflectiveExecutorTest {

    private Executor subject = new ReflectiveExecutor();

    @Test
    public void testExecute() throws Exception {

        GeneratedClass generatedClass = mock(GeneratedClass.class);
        GeneratedPackage generatedPackage = mock(GeneratedPackage.class);
        when(generatedPackage.getName()).thenReturn("");
        when(generatedPackage.getClasses()).thenReturn(singletonList(generatedClass));
        when(generatedClass.getName()).thenReturn(Interpreter.FRAGMENT_CLASS_NAME);
        when(generatedClass.getCode()).thenReturn(loadResourceAsBytes(Interpreter.FRAGMENT_CLASS_NAME));

        Map<String, Object> ret = subject.execute(generatedPackage);

        assertThat(ret, notNullValue());
        assertThat(ret.size(), equalTo(0));
    }

    private byte[] loadResourceAsBytes(String name) throws Exception{
        URL url = getClass().getResource(name + ".class");
        if (url  == null){
            return null;
        }
        FileInputStream in = null;
        try {
            File file = new File(url.toURI());
            byte fileContent[] = new byte[(int) file.length()];
            in = new FileInputStream(file);
            in.read(fileContent);
            return fileContent;
        } finally {
            if (in != null) in.close();
        }
    }
}