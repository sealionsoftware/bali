package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
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

        when(parseEngine.parseFragment(fragment)).thenReturn(ast);
        when(bytecodeEngine.generate(ast)).thenReturn(bytecode);
        when(executor.executeFragment(bytecode)).thenReturn(variables);

        Map<String, Object> ret = subject.run(fragment);

        verify(parseEngine).parseFragment(fragment);
        verify(assemblyEngine).assemble(ast);
        verify(bytecodeEngine).generate(ast);
        verify(executor).executeFragment(bytecode);

        assertThat(ret, is(variables));
    }

    @Test
    public void testMain() throws Exception {

        PrintStream out = setUpSystemIn(new String[]{"var three = 3", "exit" });

        StandardInterpreter.main();

        verify(out).println("Compilation successful");
        verify(out).println("three: 3");
    }

    @Test
    public void testMainException() throws Exception {

        PrintStream out = setUpSystemIn(new String[]{ "var Text three = 3", "exit" });

        StandardInterpreter.main();

        verify(out).println("com.sealionsoftware.bali.compiler.CompilationException: Compilation Failed: [INVALID_TYPE(1:0)]");
    }

    private PrintStream setUpSystemIn(String[] input){
        StringBuilder sb = new StringBuilder();
        for (String inputLine : input) {
            sb.append(inputLine).append(System.lineSeparator());
        }

        PrintStream out = mock(PrintStream.class);
        InputStream in = new ByteArrayInputStream(sb.toString().getBytes());

        System.setIn(in);
        System.setOut(out);

        return out;
    }
}