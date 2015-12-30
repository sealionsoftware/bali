package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import java.util.Map;

import static com.sealionsoftware.Matchers.containsOneEntry;
import static com.sealionsoftware.Matchers.containsOneValue;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class AssignmentIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testAssigningUntypedVariable() {

        Map<String, Object> output = interpreter.run("var aVariable = false aVariable = true");

        assertThat(output, notNullValue());
        assertThat(output, containsOneEntry("aVariable", bali.Boolean.TRUE));
    }

    @Test
    public void testAssigningTypedVariable() {

        Map<String, Object> output = interpreter.run("var Boolean aVariable = false aVariable = true");

        assertThat(output, notNullValue());
        assertThat(output, containsOneEntry("aVariable", bali.Boolean.TRUE));
    }

    @Test
    public void testScriptContainingIncorrectlyTypedVariable() {

        try {
            interpreter.run("var Text aVariable = \"Hello World\" aVariable = true");
        } catch (CompilationException e) {
            assertThat(e.errorList, containsOneValue(withCode(ErrorCode.INVALID_TYPE)));
            return;
        }
        fail("The required compilation exception was not thrown");
    }

}
