package com.sealionsoftware.bali.compiler;

import bali.Logic;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.Callable;

import static com.sealionsoftware.Matchers.isEmptyMap;
import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.containingError;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;

public class ConditionalStatementIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testConditional() {

        Map<String, Object> output = interpreter.run(
                "if (true) {}"
        );

        assertThat(output, isEmptyMap());
    }

    @Test
    public void testConditionalBodyWhenMet() {

        Map<String, Object> output = interpreter.run(
                "var ret = false " +
                "if (true) {ret = true}"
        );

        assertThat(output, hasEntry("ret", Logic.TRUE));
    }

    @Test
    public void testConditionalBodyWhenNotMet() {

        Map<String, Object> output = interpreter.run(
                "var ret = false " +
                "if (false) {ret = true}"
        );

        assertThat(output, hasEntry("ret", Logic.FALSE));
    }

    @Test
    public void testConditionalWithNonBooleanCondition() {

        Callable invocation = () -> interpreter.run("if (\"true\") {}");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testContraConditionalBodyWhenMet() {

        Map<String, Object> output = interpreter.run(
                "var ret = true " +
                "if (true) {} else {ret = false}"
        );

        assertThat(output, hasEntry("ret", Logic.TRUE));
    }

    @Test
    public void testContraConditionalBodyWhenNotMet() {

        Map<String, Object> output = interpreter.run(
                "var ret = true " +
                "if (false) {} else {ret = false} "
        );

        assertThat(output, hasEntry("ret", Logic.FALSE));
    }

    @Test
    public void testFlowTypingOptionalVariable() {

        Map<String, Object> output = interpreter.run(
                "var Logic? aTrue = true " +
                "var Logic output = false " +
                "if (?isTrue) {" +
                "   output = isTrue" +
                "}"
        );

        assertThat(output, hasEntry("output", Logic.TRUE));
    }

    @Test
    public void testFlowTypingTypedVariable() {

        Map<String, Object> output = interpreter.run(
                "var aVariable = true " +
                "var Logic? output" +
                "if (aVariable ? Logic) {" +
                "   output = isTrue" +
                "}"
        );

        assertThat(output, hasEntry("output", Logic.TRUE));
    }


}
