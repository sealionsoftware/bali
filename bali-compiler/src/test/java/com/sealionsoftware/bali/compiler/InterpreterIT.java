package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.antlr.ANTLRParseEngine;
import com.sealionsoftware.bali.compiler.asm.ASMBytecodeEngine;
import com.sealionsoftware.bali.compiler.assembly.MultithreadedAssemblyEngine;
import com.sealionsoftware.bali.compiler.execution.ReflectiveExecutor;
import org.junit.Test;

import java.util.Map;

import static com.sealionsoftware.bali.Matchers.isEmpty;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class InterpreterIT {

    @Test
    public void testRunEmptyScript() throws Exception {

        Interpreter interpreter = new StandardInterpreter(
                new ANTLRParseEngine(),
                new MultithreadedAssemblyEngine(),
                new ASMBytecodeEngine(),
                new ReflectiveExecutor()
        );

        Map<String, Object> output = interpreter.run("");

        assertThat(output, notNullValue());
        assertThat(output, isEmpty());
    }

}
