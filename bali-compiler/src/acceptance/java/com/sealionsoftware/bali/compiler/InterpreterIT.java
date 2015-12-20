package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import java.util.Map;

import static com.sealionsoftware.Matchers.isEmptyMap;
import static org.hamcrest.MatcherAssert.assertThat;

public class InterpreterIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testRunEmptyScript() {

        Map<String, Object> output = interpreter.run("");

        assertThat(output, isEmptyMap());
    }

}
