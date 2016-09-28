package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.execution.ReflectiveExecutor;
import org.junit.Test;

import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.containingError;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static com.sealionsoftware.bali.compiler.Matchers.wrote;
import static com.sealionsoftware.bali.compiler.Matchers.wroteNothing;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConditionalStatementIT {

    private ListTextBufferWriter console = new ListTextBufferWriter();
    private Interpreter interpreter = new StandardInterpreter(null, null, null, new ReflectiveExecutor(console));

    @Test
    public void testConditional() {

        interpreter.run(
                "if (true) { console << \"success\" }"
        );

        assertThat(console, wrote("success"));
    }

    @Test
    public void testConditionalWhenNotMet() {

        interpreter.run(
                "if (false) { console << \"fail\" }"
        );

        assertThat(console, wroteNothing());
    }

    @Test
    public void testConditionalWithNonBooleanCondition() {

        Runnable invocation = () -> interpreter.run("if (\"true\") {}");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testContraConditionalBodyWhenMet() {

        interpreter.run(
                "if (true) {} else { console << \"fail\" }"
        );

        assertThat(console, wroteNothing());
    }

    @Test
    public void testContraConditionalBodyWhenNotMet() {

        interpreter.run(
                "if (false) {} else { console << \"success\" } "
        );

        assertThat(console, wrote("success"));
    }

    @Test
    public void testFlowTypingOptionalVariable() {

        interpreter.run(
                "var Logic? aTrue = true " +
                "if (?aTrue) {" +
                "   var Logic mandatory = aTrue " +
                "   console << \"success\" " +
                "}"
        );

        assertThat(console, wrote("success"));
    }

    @Test
    public void testDoubleConditional() {

        interpreter.run(
                "if (true) if (true) {" +
                "   console << \"success\" " +
                "}"
        );

        assertThat(console, wrote("success"));
    }

}
