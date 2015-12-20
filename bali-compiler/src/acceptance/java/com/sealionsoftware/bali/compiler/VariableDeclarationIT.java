package com.sealionsoftware.bali.compiler;

import bali.Boolean;
import org.junit.Test;

import java.util.Map;

import static com.sealionsoftware.Matchers.containsOneEntry;
import static com.sealionsoftware.Matchers.containsOneValue;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class VariableDeclarationIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testScriptContainingVariable() {

        Map<String, Object> output = interpreter.run("var aVariable = true");

        assertThat(output, containsOneEntry("aVariable", Boolean.TRUE));
    }

    @Test
    public void testScriptContainingTypedVariable() {

        Map<String, Object> output = interpreter.run("var Boolean aVariable = true");

        assertThat(output, containsOneEntry("aVariable", Boolean.TRUE));
    }

    @Test
    public void testScriptContainingIncorrectlyTypedVariable() {

        try {
            interpreter.run("var Text aVariable = true");
        } catch (CompilationException e) {
            assertThat(e.errorList, containsOneValue(withCode(ErrorCode.INVALID_TYPE)));
            return;
        }
        fail("The required compilation exception was not thrown");
    }

}
