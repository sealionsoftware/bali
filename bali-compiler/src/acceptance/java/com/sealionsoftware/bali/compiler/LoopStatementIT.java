package com.sealionsoftware.bali.compiler;

import bali.Boolean;
import org.junit.Test;

import java.util.Map;

import static com.sealionsoftware.Matchers.containsOneEntry;
import static com.sealionsoftware.Matchers.containsOneValue;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class LoopStatementIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testLoopBodyWhenMet() {

        Map<String, Object> output = interpreter.run(
                "var Boolean loop = true " +
                "while (loop) { " +
                    "loop = false " +
                "}"
        );

        assertThat(output, containsOneEntry("loop", Boolean.FALSE));
    }

    @Test
    public void testLoopBodyWhenNotMet() {

        Map<String, Object> output = interpreter.run(
                "var Boolean loop = false " +
                "while (loop) { " +
                    "loop = true " +
                "}"
        );

        assertThat(output, containsOneEntry("ret", Boolean.FALSE));
    }

    @Test
    public void testLoopWithNonBooleanCondition() {

        try {
            interpreter.run("while (\"true\") {}");
        } catch (CompilationException e) {
            assertThat(e.errorList, containsOneValue(withCode(ErrorCode.INVALID_TYPE)));
            return;
        }
        fail("The required compilation exception was not thrown");
    }

}
