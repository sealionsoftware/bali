package com.sealionsoftware.bali.compiler;

import bali.Boolean;
import org.junit.Test;

import java.util.Map;

import static com.sealionsoftware.Matchers.containsOneEntry;
import static com.sealionsoftware.Matchers.containsOneValue;
import static com.sealionsoftware.Matchers.isEmptyMap;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

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

        assertThat(output, containsOneEntry("ret", Boolean.TRUE));
    }

    @Test
    public void testConditionalBodyWhenNotMet() {

        Map<String, Object> output = interpreter.run(
                "var ret = false " +
                "if (false) {ret = true}"
        );

        assertThat(output, containsOneEntry("ret", Boolean.FALSE));
    }

    @Test
    public void testConditionalWithNonBooleanCondition() {

        try {
            interpreter.run("if (\"true\") {}");
        } catch (CompilationException e) {
            assertThat(e.errorList, containsOneValue(withCode(ErrorCode.INVALID_TYPE)));
            return;
        }
        fail("The required compilation exception was not thrown");
    }

}
