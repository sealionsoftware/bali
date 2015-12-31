package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import java.util.Map;

import static bali.number.Primitive.convert;
import static com.sealionsoftware.Matchers.containingError;
import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.bali.compiler.Matchers.withCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;

public class InvocationIT {

    private Interpreter interpreter = new StandardInterpreter();

    @Test
    public void testInvokingValidMethod() {

        Map<String, Object> output = interpreter.run("var size = \"Hello World\".size()");
        assertThat(output, hasEntry("size", convert(11)));
    }

    @Test
    public void testInvokingValidVoidMethod() {

        Map<String, Object> output = interpreter.run("var size = \"Hello World\".size()");
        assertThat(output, hasEntry("size", convert(11)));
    }

    @Test
    public void testAssigningInvalidReturnType() {
        Runnable invocation = () -> interpreter.run("\"var Boolean size = \"Hello World\".size()");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.INVALID_TYPE))));
    }

    @Test
    public void testInvokingInvalidMethod() {
        Runnable invocation = () -> interpreter.run("\"var Boolean size = \"Hello World\".size()");
        assertThat(invocation, throwsException(containingError(withCode(ErrorCode.METHOD_NOT_FOUND))));
    }

}
