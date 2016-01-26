package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import static bali.logic.Primitive.convert;
import static bali.number.Primitive.convert;
import static bali.text.Primitive.convert;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class LiteralValuesIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testScriptContainingBoolean() {

        Object output = interpreter.evaluate("true");

        assertThat(output, equalTo(convert(true)));
    }

    @Test
    public void testScriptContainingText() {

        Object output = interpreter.evaluate("\"A String\"");

        assertThat(output, equalTo(convert("A String")));
    }

    @Test
    public void testScriptContainingInteger() {

        Object output = interpreter.evaluate("123");

        assertThat(output, equalTo(convert(123)));
    }

}
