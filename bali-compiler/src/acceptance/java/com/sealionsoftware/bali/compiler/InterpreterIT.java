package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.execution.ReflectiveExecutor;
import org.junit.Test;

import static com.sealionsoftware.bali.compiler.Matchers.wrote;
import static org.hamcrest.MatcherAssert.assertThat;

public class InterpreterIT {

    private ListTextBufferWriter console = new ListTextBufferWriter();
    private Interpreter interpreter = new StandardInterpreter(null, null, null, new ReflectiveExecutor(console));

    @Test
    public void testRunEmptyScript() {

        interpreter.run("");
    }

    @Test
    public void testHelloWorld() {

        interpreter.run("console << \"Hello World\"");
        assertThat(console, wrote("Hello World"));
    }

}
