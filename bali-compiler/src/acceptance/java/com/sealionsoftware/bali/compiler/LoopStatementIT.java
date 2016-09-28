package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.execution.ReflectiveExecutor;
import org.junit.Test;

import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.containingError;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static com.sealionsoftware.bali.compiler.Matchers.wrote;
import static com.sealionsoftware.bali.compiler.Matchers.wroteNothing;
import static org.hamcrest.MatcherAssert.assertThat;

public class LoopStatementIT {

    private ListTextBufferWriter console = new ListTextBufferWriter();
    private Interpreter interpreter = new StandardInterpreter(null, null, null, new ReflectiveExecutor(console));

    @Test
    public void testLoopBodyWhenMet() {

        interpreter.run(
                "var Integer i = 0 " +
                "while (i < 3) { " +
                    "console << \"loop\" " +
                    "i = ++i  " +
                "}"
        );

        assertThat(console, wrote("loop", "loop", "loop"));
    }

    @Test
    public void testLoopBodyWhenNotMet() {

        interpreter.run(
                "var Logic loop = false " +
                "while (loop) { " +
                    "console << \"loop\" " +
                "}"
        );

        assertThat(console, wroteNothing());
    }

    @Test
    public void testLoopWithNonBooleanCondition() {

        Runnable invocation = () -> interpreter.run("while (\"true\") {}");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

}
