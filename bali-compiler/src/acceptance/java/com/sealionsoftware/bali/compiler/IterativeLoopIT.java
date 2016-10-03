package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.execution.ReflectiveExecutor;
import org.junit.Test;

import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.containingError;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static com.sealionsoftware.bali.compiler.Matchers.wrote;
import static com.sealionsoftware.bali.compiler.Matchers.wroteNothing;
import static org.hamcrest.MatcherAssert.assertThat;

public class IterativeLoopIT {

    private ListTextBufferWriter console = new ListTextBufferWriter();
    private Interpreter interpreter = new StandardInterpreter(null, null, null, new ReflectiveExecutor(console));

    @Test
    public void testLoop() {

        interpreter.run(
                "for (word : [\"one\", \"two\", \"three\"]) { " +
                    "console << word " +
                "}"
        );

        assertThat(console, wrote("one", "two", "three"));
    }


    @Test
    public void testLoopBodyWhenNotMet() {

        interpreter.run(
                "for (word : []) { " +
                    "console << word " +
                "}"
        );

        assertThat(console, wroteNothing());
    }

    @Test
    public void testLoopWithNonIterativeArgument() {

        Runnable invocation = () -> interpreter.run("for (item : true) {}");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testLoopWithNameClash() {

        Runnable invocation = () -> interpreter.run(
                "var item " +
                "for (item : []) {}"
        );
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.NAME_ALREADY_USED))));
    }

    @Test
    public void testLiteralTypingInLoop() {

        Runnable invocation = () -> interpreter.run(
                "for (word : [true]) { " +
                    "console << word " +
                "}"
        );

        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

}
