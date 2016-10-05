package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import static bali.text.Primitive.convert;
import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.withPayload;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ExceptionIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testThrowMessage() {

        Runnable invocation = () -> interpreter.run(
                "throw \"Ouch!\""
        );

        assertThat(invocation, throwsException(withPayload(equalTo(convert("Ouch!")))));
    }

}
