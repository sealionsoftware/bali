package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import java.util.Map;

import static com.sealionsoftware.Matchers.isEmptyMap;
import static org.hamcrest.MatcherAssert.assertThat;

public class LiteralValuesIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testScriptContainingBoolean() {

        Map<String, Object> output = interpreter.run("true");

        assertThat(output, isEmptyMap());
    }

    @Test
    public void testScriptContainingText() {

        Map<String, Object> output = interpreter.run("\"A String\"");

        assertThat(output, isEmptyMap());
    }

    @Test
    public void testScriptContainingInteger() {

        Map<String, Object> output = interpreter.run("123");

        assertThat(output, isEmptyMap());
    }

}
