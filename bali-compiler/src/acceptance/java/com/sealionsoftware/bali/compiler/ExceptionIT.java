package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.execution.ReflectiveExecutor;
import org.junit.Test;

import static bali.text.Primitive.convert;
import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.withPayload;
import static com.sealionsoftware.bali.compiler.Matchers.wrote;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ExceptionIT {

    private ListTextBufferWriter console = new ListTextBufferWriter();
    private Interpreter interpreter = new StandardInterpreter(null, null, null, new ReflectiveExecutor(console));


    @Test
    public void testThrowMessage() {

        Runnable invocation = () -> interpreter.run(
                "throw \"Ouch!\""
        );

        assertThat(invocation, throwsException(withPayload(equalTo(convert("Ouch!")))));
    }

    @Test
    public void testCatchException() {

        interpreter.run(
                "{" +
                        "throw \"Ouch!\"" +
                "} catch {" +
                        "console << error + \" That hurt!\"" +
                "}"
        );

        assertThat(console, wrote("Ouch! That hurt!"));
    }

}
