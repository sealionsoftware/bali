package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.antlr.ANTLRParseEngine;
import com.sealionsoftware.bali.compiler.asm.ASMBytecodeEngine;
import com.sealionsoftware.bali.compiler.assembly.MultithreadedAssemblyEngine;
import com.sealionsoftware.bali.compiler.execution.ReflectiveExecutor;
import org.junit.Test;

import java.util.Map;

import static com.sealionsoftware.Constant.immutableMap;
import static com.sealionsoftware.Constant.put;
import static com.sealionsoftware.bali.Matchers.isEmptyMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class InterpreterIT {

    private Interpreter interpreter = new StandardInterpreter(
            new ANTLRParseEngine(),
            new MultithreadedAssemblyEngine(),
            new ASMBytecodeEngine(),
            new ReflectiveExecutor()
    );

    @Test
    public void testRunEmptyScript() throws Exception {

        Map<String, Object> output = interpreter.run("");

        assertThat(output, notNullValue());
        assertThat(output, isEmptyMap());
    }

    @Test
    public void testScriptContainingBoolean() throws Exception {

        Map<String, Object> output = interpreter.run("true");

        assertThat(output, notNullValue());
        assertThat(output, isEmptyMap());
    }

    @Test
    public void testScriptContainingVariable() throws Exception {

        Map<String, Object> output = interpreter.run("var aVariable = true");

        assertThat(output, notNullValue());
        assertThat(output, equalTo(immutableMap(
                put("aVariable", bali.Boolean.TRUE)
        )));
    }

}
