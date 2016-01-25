package com.sealionsoftware.bali.compiler;

import bali.Boolean;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.Callable;

import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.containingError;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;

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

        assertThat(output, hasEntry("loop", Boolean.FALSE));
    }

    @Test
    public void testLoopBodyWhenNotMet() {

        Map<String, Object> output = interpreter.run(
                "var Boolean loop = false " +
                "while (loop) { " +
                    "loop = true " +
                "}"
        );

        assertThat(output, hasEntry("loop", Boolean.FALSE));
    }

    @Test
    public void testLoopWithNonBooleanCondition() {

        Callable invocation = () -> interpreter.run("while (\"true\") {}");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

}
