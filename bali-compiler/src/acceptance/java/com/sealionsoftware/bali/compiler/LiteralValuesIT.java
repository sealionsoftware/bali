package com.sealionsoftware.bali.compiler;

import bali.collection.Array;
import org.junit.Test;

import static bali.logic.Primitive.convert;
import static bali.number.Primitive.convert;
import static bali.text.Primitive.convert;
import static com.sealionsoftware.bali.compiler.Matchers.hasValues;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

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

    @Test
    public void testScriptContainingArray() {

        Object output = interpreter.evaluate("[true, 45, \"Be my guest\"]");

        assertThat(output, instanceOf(Array.class));
        @SuppressWarnings("unchecked")
        Array<Object> outputArray = (Array<Object>) output;
        assertThat(outputArray, hasValues(convert(true), convert(45), convert("Be my guest")));
    }

    @Test
    public void testScriptContainingMatrix(){

        interpreter.run(
                "[[\"Hello\"], [\"Pretty\", \"Lady\"]]"
        );

    }

}
