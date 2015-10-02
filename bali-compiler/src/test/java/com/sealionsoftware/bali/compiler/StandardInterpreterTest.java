package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StandardInterpreterTest {

    private ParseEngine parseEngine = mock(ParseEngine.class);
    private AssemblyEngine assemblyEngine = mock(AssemblyEngine.class);
    private BytecodeEngine bytecodeEngine = mock(BytecodeEngine.class);
    private Executor executor = mock(Executor.class);

    private Interpreter subject = new StandardInterpreter(
            parseEngine,
            assemblyEngine,
            bytecodeEngine,
            executor
    );

    @Test
    public void testStandardSetup() throws Exception {
        subject = new StandardInterpreter();
    }

    @Test
    public void testRun() throws Exception {

        String fragment = "Hello World";
        CodeBlockNode ast = mock(CodeBlockNode.class);
        GeneratedPackage bytecode = mock(GeneratedPackage.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> variables = mock(Map.class);

        when(parseEngine.parse(fragment)).thenReturn(ast);
        when(bytecodeEngine.generate(ast)).thenReturn(bytecode);
        when(executor.execute(bytecode)).thenReturn(variables);

        Map<String, Object> ret = subject.run(fragment);

        verify(parseEngine).parse(fragment);
        verify(assemblyEngine).assemble(ast);
        verify(bytecodeEngine).generate(ast);
        verify(executor).execute(bytecode);

        assertThat(ret, is(variables));
    }
}